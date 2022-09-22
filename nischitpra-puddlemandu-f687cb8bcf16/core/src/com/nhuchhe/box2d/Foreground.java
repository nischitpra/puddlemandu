package com.nhuchhe.box2d;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * Created by nischit on 25/8/17.
 */

public class Foreground {
    private MyGame game;
    private float movespeed;
    private  CircularQueue forest;
    private int MAX_SIZE;
    private Array<String> string_forestList;
    private String MISC="misc";

    public Foreground(MyGame game){
        this.game=game;
        movespeed=36f;
        MAX_SIZE= 8;
        int MAX_TEXTURE_QUEUE=12;
        forest = new CircularQueue(game,Constants.TEXTURE_ATLAS_FOREGROUND,MAX_SIZE,MAX_TEXTURE_QUEUE,1,0,movespeed);

        string_forestList=new Array<String>();
        string_forestList.add("foregroundStone0");
        string_forestList.add("foregroundGrass0");
        string_forestList.add("foregroundGrass1");
        string_forestList.add("foregroundTree0");
        string_forestList.add("foregroundTree1");
        string_forestList.add("foregroundTree2");

        fillRandom(forest,string_forestList);
        forest.createActiveObject(0);
        fillRandom(forest,string_forestList);

    }
    private void fillRandom(CircularQueue queue,Array<String> array){
        for(int i=0;i<queue.MAX_TEXTURE_SIZE;i++){
            queue.enqueueTextureQueue(array.get(MathUtils.random(0,array.size-1)),MISC);
        }
    }

    public void update(){
        forest.update();
        fillRandom(forest,string_forestList);
    }
    public void render(){
        forest.render();
    }
    public void stopScroll(){
        forest.stopScroll();
    }
}
