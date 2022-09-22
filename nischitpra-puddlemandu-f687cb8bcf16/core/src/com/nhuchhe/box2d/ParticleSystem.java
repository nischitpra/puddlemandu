package com.nhuchhe.box2d;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;


/**
 * Created by nischit on 28/7/17.
 */

public class ParticleSystem {
    private MyGame game;
    private Array<String> particleNameList;
    private Array<Sprite> particleList;
    private Array<Vector2> velocityList;
    private Array<Long> lastDirectionTime;
    private Array<Integer> spriteToDestroy;

    private final int MAX_COUNT;
    private final float WIND_SPEED=5f;
    private final float ROTATION_SPEED;
    private Vector2 spawnDelay;
    private long lastGenerated;
    private float scale;

    public ParticleSystem (MyGame game, Vector2 spawnDelay,int MAX_COUNT,Array<String> particleNameList,float ROTATION_SPEED,float scale){
        this.game=game;
        this.spawnDelay=spawnDelay;
        this.MAX_COUNT=MAX_COUNT;
        this.particleNameList=new Array<String>(particleNameList);
        this.ROTATION_SPEED=ROTATION_SPEED;
        this.scale=scale;

        particleList=new Array<Sprite>();
        velocityList=new Array<Vector2>();
        lastDirectionTime=new Array<Long>();
        spriteToDestroy=new Array<Integer>();

        generateParticle();
    }
    public void setParticleNameList(Array<String> particleNameList){
        this.particleNameList=new Array<String>(particleNameList);
    }

    private void generateParticle(){
        if(particleList.size==MAX_COUNT) return;

        Sprite sprite=new Sprite(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_FOREGROUND,particleNameList.get(MathUtils.random(0,particleNameList.size-1))));
        sprite.setPosition(Constants.CAMERA_WIDTH,MathUtils.random(Constants.HEIGHT*.6f-sprite.getHeight()/2,Constants.CAMERA_HEIGHT*0.9f-sprite.getHeight()/2));
        sprite.setScale(scale);
        particleList.add(sprite);
        velocityList.add(new Vector2(MathUtils.random(4f,10f),MathUtils.random(-1f,5f)));
        lastDirectionTime.add(game.utils.randomTime(0.8f,2f));

    }
    private boolean validateParticle(int index){
        // generate particle at some random rate
        if(TimeUtils.nanoTime()>lastGenerated){
            generateParticle();
            lastGenerated= game.utils.randomTime(spawnDelay.x,spawnDelay.y);
        }
        // remove particle if is out of the screen
        if(particleList.get(index).getX()+particleList.get(index).getWidth()<0||particleList.get(index).getY()+particleList.get(index).getHeight()<0){
            particleList.removeIndex(index);
            velocityList.removeIndex(index);
            lastDirectionTime.removeIndex(index);

            spriteToDestroy.add(index);
            return true;
        }
        return true;
    }
    private void setDirection(int index){
        if(TimeUtils.nanoTime()>lastDirectionTime.get(index)){
            velocityList.set(index,new Vector2(MathUtils.random(4,10f),MathUtils.random(-1f,5f)));
            lastDirectionTime.set(index,game.utils.randomTime(0.8f,2f));
        }
    }
    private void drawParticle(int index){
        Sprite s=particleList.get(index);
        s.setPosition(s.getX()-(WIND_SPEED+velocityList.get(index).x)-(WIND_SPEED+velocityList.get(index).x)*0.8f*Constants.MOVE_DIRECTION,s.getY()-velocityList.get(index).y);
        s.setRotation(s.getRotation()+MathUtils.random(0f,ROTATION_SPEED));
        s.draw(game.batch);
    }

    public void render(){
        for(int i=0;i<particleList.size;i++){
            drawParticle(i);
        }
    }

    public void update(){
        if(particleList.size==0) generateParticle();
        for(int i=0;i<particleList.size;i++){
            setDirection(i);
            // validate contains destroy calls so its at the end
            validateParticle(i);
        }
    }

}
