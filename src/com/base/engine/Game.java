package com.base.engine;

import java.util.ArrayList;

public class Game {

    private static Level level;
    //private static Player player;
    private static boolean isRunning;
    private static int levelNum = 0;


    public Game(){
        //Player player = new Player(new Vector3f(10,0.4f,8));
        loadNextLevel();

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

    public static void loadNextLevel(){
        levelNum++;
        level = new Level("Level"+levelNum+".png", "WolfCollection.png");

        Transform.setProjection(70f, Window.getWidth(), Window.getHeight(), 0.01f, 1000f);
        Transform.setCamera(level.getPlayer().getCamera());
        isRunning = true;
    }
   // public static Player getPlayer(){
     //   return player;
 //   }

    public static Level getLevel(){
        return level;
    }
}
