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

    private Mesh mesh;
    private Material material;
    private Transform transform;

    public Monster(Transform transform){
        this.transform = transform;
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

    public void update(){

    }

    public void render(){
        Shader shader = Game.getLevel().getShader();
        shader.bind();

        shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
        mesh.draw();
        shader.unbind();
    }
}
