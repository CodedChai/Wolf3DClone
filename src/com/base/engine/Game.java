package com.base.engine;
public class Game {

    Bitmap level;

    public Game(){
        level = new Bitmap("TestLevel.png");
        for(int i = 0; i < level.getWidth(); i++){
            for(int j = 0; j < level.getHeight(); j++){
                System.out.print(level.getPixel(i, j));
            }
            System.out.println();
        }
    }

    public void start(){

    }

    public void input(){

    }

    public void update(){

    }

    public void render(){

    }
}
