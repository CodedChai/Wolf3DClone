package com.base.engine;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;



public class Window {
    /**
     * Stores the window handle.
     */
    public static long windowid;
    public static int w;
    public static int h;
    /**
     * Key callback for the window.
     */
    public static GLFWKeyCallback keyCallback;

    /**
     * Shows if vsync is enabled.
     */
    public static boolean vsync = true;

    public static void createWindow(int width, int height, String title, boolean vsyncEnabled){
        vsync = vsyncEnabled;
        w = width;
        h = height;

        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        windowid = glfwCreateWindow(width, height, title, NULL, NULL);
        if ( windowid == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(Window.windowid, keyCallback = new Input());


        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(windowid, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    windowid,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowid);
        /* Enable v-sync */
        if (vsync) {
            glfwSwapInterval(1);
        } else {
            glfwSwapInterval(0);
        }

        // Make the window visible
        glfwShowWindow(windowid);

        // First time setup necessities
        GL.createCapabilities();
        RenderUtil.setClearColor(0.0f, 0.0f, 0.0f);
        RenderUtil.initGraphics();
    }

    public static void render(){
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();


        RenderUtil.initGraphics();

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        if ( !glfwWindowShouldClose(windowid) ) {
            RenderUtil.clearScreen();
        }
    }

    public static void lateRender(){
        if ( !glfwWindowShouldClose(windowid) ) {
            glfwSwapBuffers(windowid); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

    public static void setTitle(CharSequence title) {
        glfwSetWindowTitle(windowid, title);
    }

    public static boolean isCloseRequested(){
        return glfwWindowShouldClose(windowid);
    }

    public static boolean isVSyncEnabled() {
        return vsync;
    }

    public static void setVSync(boolean vsyncenable) {
        vsync = vsyncenable;
        if (vsync) {
            glfwSwapInterval(1);
        } else {
            glfwSwapInterval(0);
        }
    }

    public static void destroy() {
        glfwDestroyWindow(windowid);
        glfwTerminate();
    }

    public static int getHeight(){
        return h;
    }

    public static int getWidth(){
        return w;
    }

}
