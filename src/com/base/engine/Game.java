package com.base.engine;

import java.util.ArrayList;

public class Game {

    private Level level;

    public Game(){
        Transform.setCamera(new Camera());
        Transform.setProjection(70f, Window.getWidth(), Window.getHeight(), 0.01f, 1000f);
        level = new Level("TestLevel.png", "WolfCollection.png");
    }

    public void start(){

    }

    public void input(){
        Transform.getCamera().input();
    }

    public void update(){

    }

    public void render(){
        level.render();
    }
}
