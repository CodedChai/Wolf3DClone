package com.base.engine;

public class Medkit {
    public static final float PICKUP_DISTANCE = 0.75f;
    public static final int HEAL_AMOUNT = 25;

    public static final float SCALE = 0.25f;

    public static final float SIZEY = SCALE;
    public static final float SIZEX = (float)((double)SIZEY / (0.67857142857142857142857142857143 * 2.5));
    public static final float START = 0;

    public static final float TEX_MAX_X = 1;
    public static final float TEX_MAX_Y = 1;
    public static final float TEX_MIN_X = 0;
    public static final float TEX_MIN_Y = 0;

    private static Mesh mesh;
    private Transform transform;
    private static Material material;

    public Medkit(Vector3f position){
        if(mesh == null){
            // TODO: Add top/bottom face if you set height less than level height
            Vertex[] vertices = new Vertex[]{
                    new Vertex(new Vector3f(-SIZEX,START,START), new Vector2f(TEX_MAX_X,TEX_MAX_Y)),
                    new Vertex(new Vector3f(-SIZEX,SIZEY,START), new Vector2f(TEX_MAX_X,TEX_MIN_Y)),
                    new Vertex(new Vector3f(SIZEX,SIZEY,START), new Vector2f(TEX_MIN_X,TEX_MIN_Y)),
                    new Vertex(new Vector3f(SIZEX,START,START), new Vector2f(TEX_MIN_X,TEX_MAX_Y))
            };

            int[] indices = new int[]{0,1,2,
                    0,2,3};

            mesh = new Mesh(vertices, indices);
        }

        if(material == null){
            material = new Material(new Texture("MEDIA0.png"));
        }

        transform = new Transform();
        transform.setTranslation(position);
    }

    public void render(){
        Shader shader = Game.getLevel().getShader();
        shader.bind();
        shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
        mesh.draw();
        shader.unbind();
    }

    public void update(){
        Vector3f directionToCam = Transform.getCamera().getPos().sub(transform.getTranslation());

        float angleToFaceCamera = (float)Math.toDegrees(Math.atan(directionToCam.getZ()/directionToCam.getX()));

        if(directionToCam.getX() < 0)
            angleToFaceCamera += 180.0f;

        transform.getRotation().setY(angleToFaceCamera + 90.0f);

        if(directionToCam.length() < PICKUP_DISTANCE){
            Player player = Game.getLevel().getPlayer();
            if(player.getHealth() < player.getMaxHealth()){
                Game.getLevel().removeMedkit(this);
                player.damage(-HEAL_AMOUNT);
            }
        }
    }
}
