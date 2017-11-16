package com.base.engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

public class Player {
    private static final float MOUSE_SENSITIVITY = 0.5f;
    private static final float MOVE_SPEED = 6f;
    private static final float LOOK_SPEED = 6f;
    private static final float PLAYER_SIZE = 0.25f;
    private static final Vector3f zeroVector = new Vector3f(0,0,0);

    private Camera camera;
    private Vector3f movementVector = zeroVector;

    public Player(Vector3f position){
        camera = new Camera(position, new Vector3f(0,0,1), new Vector3f(0,1,0));
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