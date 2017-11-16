package com.base.engine;

import java.util.ArrayList;

public class Level {

    private static final float SPOT_WIDTH = 1f;
    private static final float SPOT_LENGTH = 1f;
    private static final float SPOT_HEIGHT = 1f;

    private static final int NUM_TEX_EXPONENT = 4;
    private static final int NUM_TEXTURES = (int)Math.pow(2, NUM_TEX_EXPONENT);

    private Mesh mesh;
    private Bitmap level;
    private Shader shader;
    private Material material;
    private Transform transform;

    public Level(String levelName, String textureName){
        level = new Bitmap(levelName).flipY();
        shader = BasicShader.getInstance();
        material = new Material(new Texture(textureName));

        ArrayList<Vertex> vertices = new ArrayList<Vertex>();
        ArrayList<Integer> indices = new ArrayList<Integer>();

        for(int i = 0; i < level.getWidth(); i++){
            for(int j = 0; j < level.getHeight(); j++){
                // If it's a wall
                if((level.getPixel(i,j) & 0xFFFFFF) == 0){
                    continue;
                }
                int texX = ((level.getPixel(i,j) & 0x00FF00) >> 8) / NUM_TEXTURES; // Green component
                int texY = texX % NUM_TEX_EXPONENT;
                texX /= NUM_TEX_EXPONENT;

                float XHigher = 1f - (float)texX/(float)NUM_TEX_EXPONENT;
                float XLower = XHigher - 1f/(float)NUM_TEX_EXPONENT;
                float YLower= 1f - (float)texY/(float)NUM_TEX_EXPONENT;
                float YHigher = YLower - 1f/(float)NUM_TEX_EXPONENT;

                // Generate Floor
                indices.add(vertices.size() + 2);
                indices.add(vertices.size() + 1);
                indices.add(vertices.size() + 0);
                indices.add(vertices.size() + 3);
                indices.add(vertices.size() + 2);
                indices.add(vertices.size() + 0);

                vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH,0,j * SPOT_LENGTH), new Vector2f(XLower,YLower)));
                vertices.add(new Vertex(new Vector3f((i + 1) * SPOT_WIDTH,0,j * SPOT_LENGTH), new Vector2f(XHigher,YLower)));
                vertices.add(new Vertex(new Vector3f((i + 1) * SPOT_WIDTH,0,(j + 1) * SPOT_LENGTH), new Vector2f(XHigher,YHigher)));
                vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH,0,(j + 1) * SPOT_LENGTH), new Vector2f(XLower,YHigher)));

                // Generate Ceiling
                indices.add(vertices.size() + 0);
                indices.add(vertices.size() + 1);
                indices.add(vertices.size() + 2);
                indices.add(vertices.size() + 0);
                indices.add(vertices.size() + 2);
                indices.add(vertices.size() + 3);

                vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH,SPOT_HEIGHT,j * SPOT_LENGTH), new Vector2f(XLower,YLower)));
                vertices.add(new Vertex(new Vector3f((i + 1) * SPOT_WIDTH,SPOT_HEIGHT,j * SPOT_LENGTH), new Vector2f(XHigher,YLower)));
                vertices.add(new Vertex(new Vector3f((i + 1) * SPOT_WIDTH,SPOT_HEIGHT,(j + 1) * SPOT_LENGTH), new Vector2f(XHigher,YHigher)));
                vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH,SPOT_HEIGHT,(j + 1) * SPOT_LENGTH), new Vector2f(XLower,YHigher)));

                texX = ((level.getPixel(i,j) & 0xFF0000) >> 16) / NUM_TEXTURES; // Red component
                texY = texX % NUM_TEX_EXPONENT;
                texX /= NUM_TEX_EXPONENT;

                XHigher = 1f - (float)texX/(float)NUM_TEX_EXPONENT;
                XLower = XHigher - 1f/(float)NUM_TEX_EXPONENT;
                YLower= 1f - (float)texY/(float)NUM_TEX_EXPONENT;
                YHigher = YLower - 1f/(float)NUM_TEX_EXPONENT;

                // Generate Walls
                if((level.getPixel(i,j - 1) & 0xFFFFFF) == 0){
                    indices.add(vertices.size() + 0);
                    indices.add(vertices.size() + 1);
                    indices.add(vertices.size() + 2);
                    indices.add(vertices.size() + 0);
                    indices.add(vertices.size() + 2);
                    indices.add(vertices.size() + 3);

                    vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH,0,j * SPOT_LENGTH), new Vector2f(XLower,YLower)));
                    vertices.add(new Vertex(new Vector3f((i + 1) * SPOT_WIDTH,0,j * SPOT_LENGTH), new Vector2f(XHigher,YLower)));
                    vertices.add(new Vertex(new Vector3f((i + 1) * SPOT_WIDTH,SPOT_HEIGHT,j * SPOT_LENGTH), new Vector2f(XHigher,YHigher)));
                    vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH,SPOT_HEIGHT,j  * SPOT_LENGTH), new Vector2f(XLower,YHigher)));
                }
                if((level.getPixel(i,j + 1) & 0xFFFFFF) == 0){
                    indices.add(vertices.size() + 2);
                    indices.add(vertices.size() + 1);
                    indices.add(vertices.size() + 0);
                    indices.add(vertices.size() + 3);
                    indices.add(vertices.size() + 2);
                    indices.add(vertices.size() + 0);

                    vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH,0,(j+1) * SPOT_LENGTH), new Vector2f(XLower,YLower)));
                    vertices.add(new Vertex(new Vector3f((i + 1) * SPOT_WIDTH,0,(j+1) * SPOT_LENGTH), new Vector2f(XHigher,YLower)));
                    vertices.add(new Vertex(new Vector3f((i + 1) * SPOT_WIDTH,SPOT_HEIGHT,(j+1) * SPOT_LENGTH), new Vector2f(XHigher,YHigher)));
                    vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH,SPOT_HEIGHT,(j+1) * SPOT_LENGTH), new Vector2f(XLower,YHigher)));
                }
                if((level.getPixel(i - 1,j) & 0xFFFFFF) == 0){
                    indices.add(vertices.size() + 2);
                    indices.add(vertices.size() + 1);
                    indices.add(vertices.size() + 0);
                    indices.add(vertices.size() + 3);
                    indices.add(vertices.size() + 2);
                    indices.add(vertices.size() + 0);

                    vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH,0,j * SPOT_LENGTH), new Vector2f(XLower,YLower)));
                    vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH,0,(j+1) * SPOT_LENGTH), new Vector2f(XHigher,YLower)));
                    vertices.add(new Vertex(new Vector3f(i  * SPOT_WIDTH,SPOT_HEIGHT,(j+1) * SPOT_LENGTH), new Vector2f(XHigher,YHigher)));
                    vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH,SPOT_HEIGHT,j  * SPOT_LENGTH), new Vector2f(XLower,YHigher)));
                }

                if((level.getPixel(i + 1,j) & 0xFFFFFF) == 0){
                    indices.add(vertices.size() + 0);
                    indices.add(vertices.size() + 1);
                    indices.add(vertices.size() + 2);
                    indices.add(vertices.size() + 0);
                    indices.add(vertices.size() + 2);
                    indices.add(vertices.size() + 3);

                    vertices.add(new Vertex(new Vector3f((i+1) * SPOT_WIDTH,0,j * SPOT_LENGTH), new Vector2f(XLower,YLower)));
                    vertices.add(new Vertex(new Vector3f((i+1) * SPOT_WIDTH,0,(j+1) * SPOT_LENGTH), new Vector2f(XHigher,YLower)));
                    vertices.add(new Vertex(new Vector3f((i+1) * SPOT_WIDTH,SPOT_HEIGHT,(j+1) * SPOT_LENGTH), new Vector2f(XHigher,YHigher)));
                    vertices.add(new Vertex(new Vector3f((i+1) * SPOT_WIDTH,SPOT_HEIGHT,j  * SPOT_LENGTH), new Vector2f(XLower,YHigher)));
                }
            }
        }

        Vertex[] vertArray = new Vertex[vertices.size()];
        Integer[] intArray = new Integer[indices.size()];

        vertices.toArray(vertArray);
        indices.toArray(intArray);

        mesh = new Mesh(vertArray, Util.toIntArray(intArray));
        transform = new Transform();

    }

    public void input(){

    }

    public void update(){

    }

    public void render(){
        shader.bind();
        shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
        mesh.draw();
        shader.unbind();
    }
}
