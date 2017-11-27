package com.base.engine;

public class Door {
    public static final float LENGTH = 1;
    public static final float HEIGHT = 1;
    public static final float WIDTH = 0.125f;
    public static final float START = 0;
    public static final double TIME_TO_OPEN = 0.5;
    public static final double CLOSE_DELAY = 1.0;


    private static Mesh mesh;
    private Material material;
    private Transform transform;

    private Vector3f openPosition;
    private Vector3f closePosition;

    private boolean isOpening;
    private double openingStartTime;
    private double closingStartTime;
    private double openTime;
    private double closeTime;

    public Door(Transform transform, Material material, Vector3f openPosition){
        this.isOpening = false;
        this.transform = transform;
        this.material = material;
        this.closePosition = transform.getTranslation().mul(1);
        this.openPosition = openPosition;
        if(mesh == null){
            // TODO: Add top/bottom face if you set height less than level height
            Vertex[] vertices = new Vertex[]{new Vertex(new Vector3f(START,START,START), new Vector2f(0.5f,1)),
                                            new Vertex(new Vector3f(START,HEIGHT,START), new Vector2f(0.5f,0.75f)),
                                            new Vertex(new Vector3f(LENGTH,HEIGHT,START), new Vector2f(0.75f,0.75f)),
                                            new Vertex(new Vector3f(LENGTH,START,START), new Vector2f(0.75f,1)),

                                            new Vertex(new Vector3f(START,START,START), new Vector2f(0.73f,1)),
                                            new Vertex(new Vector3f(START,HEIGHT,START), new Vector2f(0.73f,0.75f)),
                                            new Vertex(new Vector3f(START,HEIGHT,WIDTH), new Vector2f(0.75f,0.75f)),
                                            new Vertex(new Vector3f(START,START,WIDTH), new Vector2f(0.75f,1)),

                                            new Vertex(new Vector3f(START,START,WIDTH), new Vector2f(0.5f,1)),
                                            new Vertex(new Vector3f(START,HEIGHT,WIDTH), new Vector2f(0.5f,0.75f)),
                                            new Vertex(new Vector3f(LENGTH,HEIGHT,WIDTH), new Vector2f(0.75f,0.75f)),
                                            new Vertex(new Vector3f(LENGTH,START,WIDTH), new Vector2f(0.75f,1)),

                                            new Vertex(new Vector3f(LENGTH,START,START), new Vector2f(0.73f,1)),
                                            new Vertex(new Vector3f(LENGTH,HEIGHT,START), new Vector2f(0.73f,0.75f)),
                                            new Vertex(new Vector3f(LENGTH,HEIGHT,WIDTH), new Vector2f(0.75f,0.75f)),
                                            new Vertex(new Vector3f(LENGTH,START,WIDTH), new Vector2f(0.75f,1))
            };

            int[] indices = new int[]{0,1,2,
                                        0,2,3,

                                        6,5,4,
                                        7,6,4,

                                        10,9,8,
                                        11,10,8,

                                        12,13,14,
                                        12,14,15

            };

            mesh = new Mesh(vertices, indices);
        }
    }

    private Vector3f vectorLerp(Vector3f startPos, Vector3f endPos, float lerpFactor){
        return startPos.add(endPos.sub(startPos).mul(lerpFactor));
    }


    public void update(){
        if(isOpening){
            doorOpening();
        }
    }

    private void doorOpening(){
        double time = (double)Time.getTime()/(double)Time.SECOND;
        if(time < openTime){
            getTransform().setTranslation(vectorLerp(closePosition, openPosition,
                                                        (float)((time - openingStartTime) / TIME_TO_OPEN)));
        }
        else if(time < closingStartTime){
            getTransform().setTranslation(openPosition);
        }
        else if(time < closeTime){
            getTransform().setTranslation(vectorLerp(openPosition, closePosition,
                                                        (float)((time - closingStartTime) / TIME_TO_OPEN)));
        }
        else {
            getTransform().setTranslation(closePosition);
            isOpening = false;
        }
    }

    public void render(){
        //TODO: Bind/update the shader
        Shader shader = Game.getLevel().getShader();
        shader.bind();

        shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
        mesh.draw();
        shader.unbind();
    }

    public Transform getTransform(){
        return transform;
    }

    public void open(){
        if(isOpening)
            return;

        openingStartTime = (double)Time.getTime()/(double)Time.SECOND;
        openTime = openingStartTime + TIME_TO_OPEN;
        closingStartTime = openTime + CLOSE_DELAY;
        closeTime = closingStartTime + TIME_TO_OPEN;
        isOpening = true;
    }

    public Vector2f getDoorSize() {
        if(getTransform().getRotation().getY() == 90)
            return new Vector2f(Door.WIDTH, Door.LENGTH);
        else
            return new Vector2f(Door.LENGTH, Door.WIDTH);
    }
}
