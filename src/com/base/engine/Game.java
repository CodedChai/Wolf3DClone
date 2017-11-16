package com.base.engine;

import org.lwjglx.Sys;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Guard on 11/10/2017.
 */
public class Game {

    private Mesh mesh;
    private Shader shader;
    private Transform transform;
    private Material material;
    private Camera camera;

    PointLight pLight1 = new PointLight(new BaseLight(new Vector3f(1, 0.5f, 0), 0.8f),
            new Attenuation(0,0,1), new Vector3f(-2f, 0f, 5f),10f);
    PointLight pLight2 = new PointLight(new BaseLight(new Vector3f(0, 0, 1), 0.8f),
            new Attenuation(0,0,1), new Vector3f(2f, 0f, 5f), 11f);

    SpotLight sLight1 = new SpotLight(new PointLight(new BaseLight(new Vector3f(0f, 1f, 1f), 0.8f),
            new Attenuation(0,0,0.1f), new Vector3f(-2f, 0f, 5f),30f),
            new Vector3f(1,1,1), 0.7f);

    public Game(){
        //mesh = ResourceLoader.loadMesh("cube.obj");
        material = new Material(new Texture("Marble.jpg"), new Vector3f(1.0f, 1.0f, 1.0f), 1, 16);
        shader = PhongShader.getInstance();
        camera = new Camera(new Vector3f(0,0,0), new Vector3f(0,0,1), new Vector3f(0,1,0));
        transform = new Transform();

         /*//created pyramid
        Vertex[] vertices = new Vertex[]{
                new Vertex(new Vector3f(-1.0f, -1.0f, 0.5773f), new Vector2f(0.0f,0.0f)),
                new Vertex(new Vector3f(0.0f, -1.0f, -1.15475f), new Vector2f(0.5f,0.0f)),
                new Vertex(new Vector3f(1.0f, -1.0f, 0.5773f), new Vector2f(1.0f,0.0f)),
                new Vertex(new Vector3f(0.0f, 1.0f, 0.0f), new Vector2f(0.5f,1.0f))
        };

        int[] indices = new int[] {0, 3, 1,
                                    1,3,2,
                                    2,3,0,
                                    1,2,0};*/
        float fieldDepth = 10.0f;
        float fieldWidth = 10.0f;
        Vertex[] vertices = new Vertex[] {  new Vertex(new Vector3f(-fieldWidth, 0.0f, -fieldDepth), new Vector2f(0.0f, 0.0f)),
                                            new Vertex(new Vector3f(-fieldWidth, 0.0f, fieldDepth * 3), new Vector2f(0.0f, 1.0f)),
                                            new Vertex(new Vector3f(fieldWidth * 3, 0.0f, -fieldDepth), new Vector2f(1.0f, 0.0f)),
                                            new Vertex(new Vector3f(fieldWidth * 3, 0.0f, fieldDepth * 3), new Vector2f(1.0f, 1.0f))
        };

        int indices[] = {   0, 1, 2,
                            2, 1, 3};
        mesh = new Mesh(vertices, indices, true);

        Transform.setProjection(70f, Window.getWidth(), Window.getHeight(), 0.1f, 1000);
        Transform.setCamera(camera);

        //PhongShader.setAmbientLight(new Vector3f(0.2f, 0.2f, 0.2f));
        PhongShader.setDirectionalLight(new DirectionalLight(new BaseLight(new Vector3f(1,1,1), 0.5f), new Vector3f(0.5f,-1,1)));

        PhongShader.setPointLight(new PointLight[]{pLight1, pLight2});
        PhongShader.setSpotLight(new SpotLight[]{sLight1});
    }

    public void start(){

    }

    public void input(){
        camera.input();

        if(Input.getKey(GLFW_KEY_E)){
            System.out.println("Pressed E");
        }
    }

    float temp = 0.0f;
    public void update(){
        temp += Time.getDelta();
        transform.setTranslation(0, -1, 5);

        pLight1.setAtten(new Attenuation(0, 0, (float)(Math.sin(temp))));
        pLight2.setAtten(new Attenuation(0, 0, (float)(Math.cos(temp))));
        pLight1.setPosition(new Vector3f(3, -.8f, 8.0f * (float)(Math.sin(temp) + 1.0/2.0) + 10));
        pLight2.setPosition(new Vector3f(7, -.8f, 8.0f * (float)(Math.cos(temp) + 1.0/2.0) + 10));
        sLight1.getPointLight().setPosition(camera.getPos());
        sLight1.setDirection(camera.getForward());
        //System.out.println(camera.getForward().toString());
        //transform.setTranslation((float)Math.sin(temp),(float)Math.abs(Math.cos(temp)),5);
        //transform.setRotation(0, (float)Math.sin(temp) * 180, (float)Math.sin(temp) * 180);
        //transform.setScale((float)Math.abs(Math.sin(temp)), (float)Math.abs(Math.sin(temp)), (float)Math.abs(Math.sin(temp)));
    }

    public void render(){
        RenderUtil.setClearColor(Transform.getCamera().getPos().div(2048f).Abs());
        shader.bind();
        shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
        mesh.draw();
        shader.unbind();
    }
}
