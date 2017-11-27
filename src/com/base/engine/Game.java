package com.base.engine;

import java.util.ArrayList;

public class Game {

    private static Level level;
    //private static Player player;

    public Game(){
        Player player = new Player(new Vector3f(10,0.56f,8));
        level = new Level("TestLevel.png", "WolfCollection.png", player);

        Transform.setProjection(70f, Window.getWidth(), Window.getHeight(), 0.01f, 1000f);
        Transform.setCamera(player.getCamera());

    }

    public void start(){

    }

    public void input(){
        level.input();
    }

    public void update(){
        level.update();
    }

    public void render(){
        level.render();

    }

   // public static Player getPlayer(){
     //   return player;
 //   }

    public static Level getLevel(){
        return level;
    }
}
