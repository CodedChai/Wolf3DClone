package com.base.engine;

import org.lwjglx.Sys;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class Player {
    public static final float GUN_OFFSET = -0.0875f;
    public static final float SCALE = 0.0625f;

    public static final float SIZEY = SCALE;
    public static final float SIZEX = (float)((double)SIZEY / (1.0379746835443037974683544303797 * 2.0));
    public static final float START = 0;

    public static final float TEX_MAX_X = 1;
    public static final float TEX_MAX_Y = 1;
    public static final float TEX_MIN_X = 0;
    public static final float TEX_MIN_Y = 0;

    private static final float MOUSE_SENSITIVITY = 0.5f;
    private static final float MOVE_SPEED = 6f;
    private static final float LOOK_SPEED = 6f;
    public static final float PLAYER_SIZE = 0.25f;
    public static final float SHOOT_DISTANCE = 1000.0f;
    public static final int DAMAGE_MIN = 20;
    public static final int DAMAGE_MAX = 60;
    public static final int MAX_HEALTH = 100000;
    public static final double ATTACK_DELAY = .4;

    private static final Vector3f zeroVector = new Vector3f(0,0,0);
    private Random rand;
    private int health;
    private Camera camera;
    private Vector3f movementVector;
    private double lastAttackTime;
    private boolean canAttack;
    private static Mesh mesh;
    private static Material gunMaterial;

    private Transform gunTransform;

    public Player(Vector3f position){
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

        if(gunMaterial == null){
            gunMaterial = new Material(new Texture("PISGB0.png"));
        }
        gunTransform = new Transform();
        gunTransform.setTranslation(new Vector3f(10,0,7));
        movementVector = zeroVector;
        rand = new Random();
        health = MAX_HEALTH;
        camera = new Camera(position, new Vector3f(0,0,1), new Vector3f(0,1,0));
        canAttack = true;
    }

    public void damage(int amount){
        health -= amount;

        if(health > MAX_HEALTH)
            health = MAX_HEALTH;

        System.out.println(health);

        if(health <= 0){
            Game.setIsRunning(false);
            System.out.println("YOU DIED!");
        }
    }

    public int getDamage(){
        return rand.nextInt(DAMAGE_MAX - DAMAGE_MIN) + DAMAGE_MIN;
    }

    public void input(){
        float rotAmount = (float)(LOOK_SPEED * Time.getDelta());
        movementVector = zeroVector;

        if(Input.getKey(GLFW_KEY_W)){
            movementVector = movementVector.add(camera.getForward());
            //camera.move(camera.getForward(), movAmount);
        }
        if(Input.getKey(GLFW_KEY_S)){
            movementVector = movementVector.sub(camera.getForward());
            //camera.move(camera.getForward(), -movAmount);
        }
        if(Input.getKey(GLFW_KEY_A)){
            movementVector = movementVector.add(camera.getLeft());
            //camera.move(camera.getLeft(), movAmount);
        }
        if(Input.getKey(GLFW_KEY_D)){
            movementVector = movementVector.add(camera.getRight());
            //camera.move(camera.getRight(), movAmount);
        }
        //Input.cursorPosition();

        if(Input.getKey(GLFW_KEY_UP)){
            camera.rotateX(-rotAmount);
        }
        if(Input.getKey(GLFW_KEY_DOWN)){
            camera.rotateX(rotAmount);
        }
        if(Input.getKey(GLFW_KEY_LEFT)){
            camera.rotateY(-rotAmount);
        }
        if(Input.getKey(GLFW_KEY_RIGHT)){
            camera.rotateY(rotAmount);
        }

        if(Input.getKey(GLFW_KEY_E)){
            Game.getLevel().openDoors(camera.getPos(), true);
        }
        if(!canAttack)
            handleCanAttack();

        if(Input.getKeyPress(GLFW_KEY_SPACE) && canAttack){
            canAttack = false;
            lastAttackTime = Time.getTime()/(double)Time.SECOND;

            gunMaterial = new Material(new Texture("PISFA0.png"));

            Vector2f lineStart = new Vector2f(camera.getPos().getX(), camera.getPos().getZ());
            Vector2f castDirection = new Vector2f(camera.getForward().getX(), camera.getForward().getZ()).normalize();
            Vector2f lineEnd = lineStart.add(castDirection.mul(SHOOT_DISTANCE));

            Game.getLevel().checkIntersections(lineStart, lineEnd, true);
        }
    }

    private void handleCanAttack(){
        double time = Time.getTime()/(double)Time.SECOND;

        if((time - lastAttackTime) > ATTACK_DELAY){
            gunMaterial = new Material(new Texture("PISGB0.png"));
            canAttack = true;
        }
    }

    public void update(){
        // Player movement
        float movAmount = (float)(MOVE_SPEED * Time.getDelta());
        movementVector.setY(0);
        if(movementVector.length() > 0){
            movementVector = movementVector.normalize();
        }
        Vector3f oldPos = camera.getPos();
        Vector3f newPos = oldPos.add(movementVector.mul(movAmount));

        Vector3f collisionVector = Game.getLevel().checkCollision(oldPos, newPos, PLAYER_SIZE, PLAYER_SIZE);
        movementVector = movementVector.mul(collisionVector);

        if(movementVector.length() > 0)
            camera.move(movementVector, movAmount);

        // Gun movement
        gunTransform.setTranslation(camera.getPos().add(camera.getForward().normalize().mul(0.105f)));
        gunTransform.getTranslation().setY(gunTransform.getTranslation().getY() + GUN_OFFSET);
        Vector3f directionToCam = Transform.getCamera().getPos().sub(gunTransform.getTranslation());

        float angleToFaceCamera = (float)Math.toDegrees(Math.atan(directionToCam.getZ()/directionToCam.getX()));

        if(directionToCam.getX() < 0)
            angleToFaceCamera += 180.0f;

        gunTransform.getRotation().setY(angleToFaceCamera + 90.0f);
    }

    public void render(){
        Shader shader = Game.getLevel().getShader();
        shader.bind();
        shader.updateUniforms(gunTransform.getTransformation(), gunTransform.getProjectedTransformation(), gunMaterial);
        mesh.draw();
        shader.unbind();
    }

    public int getMaxHealth(){
        return  MAX_HEALTH;
    }

    public int getHealth(){
        return health;
    }

    public Camera getCamera() {
        return camera;
    }
}
