package com.nhuchhe.box2d;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by nischit on 4/7/17.
 */

public class Utils {
    public  float getRadianToDegree(float radian){
        return radian/MathUtils.PI *180;
    }
    public  float getScale(float requiredHeight, float textureHeight){
        return requiredHeight/textureHeight;
    }
    public  float getCenterPoint(float start,float end,float textureWidth){
        return (start+end-textureWidth)/2.0f;
    }

    /**
     *     takes input in seconds and return in nanoseconds with respect to the current time
     *     used to create a delay of random time from x and y
     */

    public  long randomTime(float x, float y){
        return (long)(MathUtils.random(x,y)*Constants.NANOSECOND+ TimeUtils.nanoTime());

    }
    public  float decrement(float number,float decrementor){
        return number>0?number-decrementor:number+decrementor;
    }
    public  boolean inRange(float x, float min, float max){
        return (x>=min && x<=max);
    }
    public  void fadeInAnimation(Actor actor, float fadeInRate){
        Color color=actor.getColor();
        if(color.a<1){
            color.a+=fadeInRate;
            actor.setColor(color);
        }
    }
    public  void fadeOutAnimation(Actor actor,float fadeOutRate){
        Color color=actor.getColor();
        if(color.a>0){
            color.a-=fadeOutRate;
            actor.setColor(color);
        }
    }

}
