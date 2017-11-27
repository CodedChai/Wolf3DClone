package com.base.engine;

public class Monster {
    public static final float SCALE = 0.7f;

    public static final float SIZEY = SCALE;
    public static final float SIZEX = (float)((double)SIZEY / (1.9310344837686206896551724137931 * 2.0));
    public static final float START = 0;

    public static final float TEX_MAX_X = 1;
    public static final float TEX_MAX_Y = 1;
    public static final float TEX_MIN_X = 0;
    public static final float TEX_MIN_Y = 0;

    public static final int STATE_IDLE = 0;
    public static final int STATE_CHASE = 1;
    public static final int STATE_ATTACK = 2;
    public static final int STATE_DYING = 3;
    public static final int STATE_DEAD = 4;


    private Mesh mesh;
    private Material material;
    private Transform transform;
    private int state;

    public Monster(Transform transform){
        this.transform = transform;
        this.state = STATE_IDLE;
        material = new Material(new Texture("SSWVA1.png"));

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
    }

    private void idleUpdate(){

    }

    private void chaseUpdate(){

    }

    private void attackUpdate(){

    }

    private void dyingUpdate(){

    }

    private void deadUpdate(){

    }

    private void billboard(){
        Vector3f directionToCam = transform.getTranslation().sub(Transform.getCamera().getPos());

        float angleToFaceCamera = (float)Math.toDegrees(Math.atan(directionToCam.getZ()/directionToCam.getX()));

        if(directionToCam.getX() > 0)
            angleToFaceCamera += 180.0f;

        transform.getRotation().setY(angleToFaceCamera + 90.0f);
    }

    public void update(){
        billboard();

        switch (state){
            case STATE_IDLE:
                idleUpdate();
                break;
            case STATE_CHASE:
                chaseUpdate();
                break;
            case STATE_ATTACK:
                attackUpdate();
                break;
            case STATE_DYING:
                dyingUpdate();
                break;
            case STATE_DEAD:
                deadUpdate();
                break;
        }
    }

    public void render(){
        Shader shader = Game.getLevel().getShader();
        shader.bind();

        shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
        mesh.draw();
        shader.unbind();
    }
}
