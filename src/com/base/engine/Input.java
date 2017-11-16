package com.base.engine;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

public class Input extends GLFWKeyCallback {

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

    }


}
