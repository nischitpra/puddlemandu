package com.nhuchhe.box2d;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by nischit on 5/7/17.
 */

public class Controller extends Stage implements InputProcessor {
    private Character character;
    private boolean once=false;

    public Controller(Character character){
        this.character=character;
    }
    public boolean touchDown (int x, int y, int pointer, int button) {
        if(x<Constants.CAMERA_WIDTH/2){
            Constants.MOVE_DIRECTION=-1;
        }else {
            if(!once){
                character.jump();
                once=true;
            }
        }
        return true;
    }

    public boolean touchUp (int x, int y, int pointer, int button) {
        if(x<Constants.CAMERA_WIDTH/2){
            Constants.MOVE_DIRECTION=1;
        }else {
            once=false;
        }
        return true;
    }

    public void render(){

    }

    public void dispose(){
        this.clear();
    }
}