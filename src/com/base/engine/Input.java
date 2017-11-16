package com.base.engine;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Input extends GLFWKeyCallback {

    private static float newX = Window.getWidth()/2;
    private static float newY = Window.getHeight()/2;

    private static float prevX = 0;
    private static float prevY = 0;

    private static boolean rotX = false;
    private static boolean rotY = false;

    public static float deltaX;
    public static float deltaY;

    public static int[] keys = new int[65535];
    public static float xpos, ypos;
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        keys[key] = action;
    }

    public static boolean getKey(int keyCode){
        if(glfwGetKey(Window.windowid, keyCode) == GLFW_PRESS || glfwGetKey(Window.windowid, keyCode) == GLFW_REPEAT){
            return true;
        }
        return false;
    }

    public static boolean getKeyPress(int keyCode){
        if(glfwGetKey(Window.windowid, keyCode) == GLFW_PRESS){
            return true;
        }
        return false;
    }

    public static void hideCursor(){
        glfwSetInputMode(Window.windowid, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    public static void showCursor(){
        glfwSetInputMode(Window.windowid, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    public static boolean getMouseButton(int buttonCode){
        int state = glfwGetMouseButton(Window.windowid, buttonCode);
        if(state == GLFW_PRESS){
            return true;
        }
        return false;
    }

    public static void cursorPosition(){
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

        glfwGetCursorPos(Window.windowid, x, y);
        x.rewind();
        y.rewind();

        newX = (float)x.get();
        newY = (float)y.get();

        deltaX = newX - 400;
        deltaY = newY - 300;

        rotX = newX != prevX;
        rotY = newY != prevY;

        if(rotY) {
            System.out.println("ROTATE Y AXIS: " + deltaY);

        }
        if(rotX) {
            System.out.println("ROTATE X AXIS: " + deltaX);
        }

        prevX = newX;
        prevY = newY;


        System.out.println("Delta X = " + deltaX + " Delta Y = " + deltaY);

        glfwSetCursorPos(Window.windowid, Window.getWidth()/2, Window.getHeight()/2);
    }


}
