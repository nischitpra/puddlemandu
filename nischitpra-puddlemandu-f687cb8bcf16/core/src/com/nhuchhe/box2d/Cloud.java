package com.nhuchhe.box2d;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by nischit on 1/8/17.
 */

public class Cloud {
    MyGame game;
    private Array<Sprite> activeCloud;
    private Array<Float> vel;
    private Array<String> string_cloud;
    private int MAX_CLOUD =1;
    private Array<Lightning> lightning;
    private Array<Vector2> scheduler; //x= wait time, y= play time
    private long lastGenerated;
    private boolean once;
    private GameScreen screen;
    private Vector2 tempVector;


    public Cloud(MyGame game,GameScreen screen){
        this.game=game;
        this.screen=screen;
        activeCloud=new Array<Sprite>();
        string_cloud =new Array<String>();
        lightning=new Array<Lightning>();
        vel =new Array<Float>();
        scheduler=new Array<Vector2>();

        string_cloud.add("cloud0");
        string_cloud.add("cloud1");
        string_cloud.add("cloud2");

        once=false;

        tempVector=new Vector2();
        lastGenerated=game.utils.randomTime(30,35);

    }

    private void generateCloud(float playerPositionX){
        if(activeCloud.size== MAX_CLOUD) return;
        activeCloud.add(getRandomCloud(playerPositionX));
        lightning.add(new Lightning(game));
        scheduler.add(new Vector2(0,game.utils.randomTime(Constants.LIGNTNING_PLAYTIME.x,Constants.LIGNTNING_PLAYTIME.y)));
    }
    private Sprite getRandomCloud(float playerPositionX){
        Sprite s=game.resource.getSprite(string_cloud.get(MathUtils.random(0,string_cloud.size-1)));
        s.setPosition(Constants.WIDTH+playerPositionX,Constants.HEIGHT*0.97f);
        vel.add(-MathUtils.random(0.01f,0.1f));
        return s;
    }

    public void update(float playerPositionX){
        if(TimeUtils.nanoTime()>lastGenerated){
            generateCloud(playerPositionX);
            lastGenerated=game.utils.randomTime(5.0f,8.0f);
        }
        for(int i=0;i<activeCloud.size;i++){

            // remove cloud out of screen
            if(activeCloud.get(i).getX()+activeCloud.get(i).getWidth()+Constants.WIDTH/2<playerPositionX){
                activeCloud.removeIndex(i);
                vel.removeIndex(i);
                lightning.get(i).destroy(lightning.get(i).getHead());
                lightning.removeIndex(i);
                scheduler.removeIndex(i);
                i--;
                continue;
            }

            activeCloud.get(i).setPosition(activeCloud.get(i).getX()+vel.get(i),Constants.HEIGHT*0.97f);
            // play if has play time
            if(scheduler.get(i).y-TimeUtils.nanoTime()>0){
                // this is halved because the lightning body contains the whole body and has its center at the center
                tempVector.set(activeCloud.get(i).getX()+activeCloud.get(i).getWidth()/2,activeCloud.get(i).getY()+activeCloud.get(i).getHeight()*0.3f);
                lightning.get(i).generateTree(tempVector,vel.get(i));
                // this condition is added because lightning creating has to wait and sometimes generate may not have created it
                if(lightning.get(i).getHead()!=null){
                    tempVector.set(lightning.get(i).getHead().getParentPosition().x+vel.get(i),lightning.get(i).getHead().getParentPosition().y);
                    lightning.get(i).getHead().setParentPosition(tempVector);
                }
                // set random time for wait time
                if(!once){
                    scheduler.get(i).x=game.utils.randomTime(Constants.LIGNTNING_WAITTIME.x,Constants.LIGNTNING_WAITTIME.y);
                    once=true;
                }
            }else{
                // wait for wait time to reinitialize play time
                if(TimeUtils.nanoTime()-scheduler.get(i).x>0){
                    scheduler.get(i).y=game.utils.randomTime(Constants.LIGNTNING_PLAYTIME.x,Constants.LIGNTNING_PLAYTIME.y);
                    once=false;
                }
                // destroy lightning after playtime is over
                lightning.get(i).destroy(lightning.get(i).getHead());
            }
        }
    }

    public void render(){
        for(int i=0;i<activeCloud.size;i++){
            if(game.utils.inRange(activeCloud.get(i).getX(),screen.character.getPosition().x-Constants.WIDTH*0.5f-activeCloud.get(i).getX(),screen.character.getPosition().x+Constants.WIDTH)){
                lightning.get(i).render();
                activeCloud.get(i).draw(game.batch);
            }
        }
    }

    public void dispose() {
        for(int i=0;i<lightning.size;i++){
            lightning.get(i).dispose(lightning.get(i).getHead());

        }
    }
}
