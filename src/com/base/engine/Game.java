package com.base.engine;

import java.util.ArrayList;

public class Game {

    private static Level level;
    private static Player player;

    public Game(){
        level = new Level("TestLevel.png", "WolfCollection.png");
        player = new Player(new Vector3f(10,0.56f,8));

        Transform.setProjection(70f, Window.getWidth(), Window.getHeight(), 0.01f, 1000f);
        Transform.setCamera(player.getCamera());

    }

    public void start(){

    }

    public void input(){
        level.input();
        player.input();
    }

    public void update(){
        level.update();
        player.update();
    }

    public void render(){
        level.render();
        player.render();
    }

    public static Player getPlayer(){
        return player;
    }

    public static Level getLevel(){
        return level;
    }
}
