package com.base.engine;

import java.util.ArrayList;

public class Game {

    private static Level level;
    //private static Player player;
    private static boolean isRunning;

    public Game(){
        Player player = new Player(new Vector3f(10,0.4f,8));
        level = new Level("TestLevel.png", "WolfCollection.png", player);

        Transform.setProjection(70f, Window.getWidth(), Window.getHeight(), 0.01f, 1000f);
        Transform.setCamera(player.getCamera());
        isRunning = true;
    }

    public void start(){

    }

    public void input(){
        level.input();
    }

    public void update(){
        if(isRunning)
            level.update();
    }

    public void render(){
        if(isRunning)
            level.render();

    }

    public static void setIsRunning(boolean setVal){
        isRunning = setVal;
    }

   // public static Player getPlayer(){
     //   return player;
 //   }

    public static Level getLevel(){
        return level;
    }
}
