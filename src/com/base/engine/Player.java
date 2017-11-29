package com.base.engine;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

public class Player {
    private static final float MOUSE_SENSITIVITY = 0.5f;
    private static final float MOVE_SPEED = 6f;
    private static final float LOOK_SPEED = 6f;
    public static final float PLAYER_SIZE = 0.25f;
    public static final float SHOOT_DISTANCE = 1000.0f;
    public static final int DAMAGE_MIN = 20;
    public static final int DAMAGE_MAX = 60;
    public static final int MAX_HEALTH = 100;

    private static final Vector3f zeroVector = new Vector3f(0,0,0);
    private Random rand;
    private int health;
    private Camera camera;
    private Vector3f movementVector = zeroVector;

    public Player(Vector3f position){
        rand = new Random();
        health = MAX_HEALTH;
        camera = new Camera(position, new Vector3f(0,0,1), new Vector3f(0,1,0));
    }

    public void damage(int amount){
        health -= amount;
        System.out.println(health);

        if(health > MAX_HEALTH)
            health = MAX_HEALTH;

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
            Game.getLevel().openDoors(camera.getPos());
        }

        if(Input.getKeyPress(GLFW_KEY_SPACE)){
            Vector2f lineStart = camera.getPos().getXZ();
            Vector2f castDirection = camera.getForward().getXZ().normalize();
            Vector2f lineEnd = lineStart.add(castDirection.mul(SHOOT_DISTANCE));

            Game.getLevel().checkIntersections(lineStart, lineEnd, true);
        }
    }

    public void update(){
        float movAmount = (float)(MOVE_SPEED * Time.getDelta());
        movementVector.setY(0);
        if(movementVector.length() > 0){
            movementVector = movementVector.normalize();
        }
        Vector3f oldPos = camera.getPos();
        Vector3f newPos = oldPos.add(movementVector.mul(movAmount));

        Vector3f collisionVector = Game.getLevel().checkCollision(oldPos, newPos, PLAYER_SIZE, PLAYER_SIZE);
        movementVector = movementVector.mul(collisionVector);

        camera.move(movementVector, movAmount);
    }

    public void render(){

    }

    public Camera getCamera() {
        return camera;
    }
}
