package com.nhuchhe.box2d;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

/**
 * Created by nischit on 5/8/17.
 */

public class Rain {
    private MyGame game;
    private final int MAX_COUNT;
    private float length,width=0.5f;
    private Array<Sprite> rainSprite;
    private Array<Vector3> direction; // x= angle, y= xvelocity, z = y velocity
    private final Vector2 WIDTH;
    private long lastUpdate;
    private final float RAIN_DELAY=0.2f;
    private int cursor=0;
    public Rain(MyGame game, int MAX_COUNT, Vector2 WIDTH){
        this.game=game;
        this.MAX_COUNT=MAX_COUNT;
        this.WIDTH=WIDTH;

        rainSprite=new Array<Sprite>();
        direction=new Array<Vector3>();
        lastUpdate=0;
    }
    private Sprite getSprite(int index){
        if(rainSprite.size<MAX_COUNT){
            return new Sprite(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_FOREGROUND,"rain"));
        }
        return rainSprite.get(index);
    }
    private void generateRain(int index){
        length=MathUtils.random(Constants.CAMERA_HEIGHT*0.5f,Constants.CAMERA_HEIGHT*0.9f);
        width=MathUtils.random(WIDTH.x,WIDTH.y);
        Sprite s=getSprite(index);
        s.setPosition(MathUtils.random(0,Constants.CAMERA_WIDTH),MathUtils.random(Constants.CAMERA_HEIGHT*0.8f,Constants.CAMERA_HEIGHT*1.2f));
        s.setScale(length/s.getWidth(),width/s.getHeight());
        s.setOrigin(s.getWidth(),0);
        float angle=Constants.MOVE_DIRECTION>0?75:105;
        s.setRotation(angle);
        s.setAlpha(0.5f);

        if(rainSprite.size<MAX_COUNT){
            rainSprite.add(s);
            direction.add(new Vector3(angle,MathUtils.random(17,23),MathUtils.random(85,95)));
        }
        direction.get(index).x=angle;
    }

    public void update(){
        if(TimeUtils.nanoTime()>lastUpdate){
            generateRain(cursor++);
            cursor=cursor>=MAX_COUNT?cursor%MAX_COUNT:cursor;
            lastUpdate= (long)(TimeUtils.nanoTime()+RAIN_DELAY*Constants.NANOSECOND);
        }
        move();
    }
    public void move(){
        for(int i=0;i<rainSprite.size;i++){
            Vector2 currentPos=new Vector2(rainSprite.get(i).getX(),rainSprite.get(i).getY());
            rainSprite.get(i).setPosition(
                    currentPos.x-(float)Math.cos(direction.get(i).x)*direction.get(i).y+(Constants.MOVE_DIRECTION<0?direction.get(i).y*0.5f:0.0f),
                    currentPos.y+(float)Math.sin(direction.get(i).x)*direction.get(i).z);
        }
    }
    public void render(){
        Iterator<Sprite> iterator=rainSprite.iterator();
        while(iterator.hasNext()){
            Sprite s=iterator.next();
            s.draw(game.batch);
        }
    }
}
