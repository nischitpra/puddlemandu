package com.nhuchhe.box2d;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;


/**
 * Created by nischit on 13/7/17.
 */

class QueueObject{
    private Sprite sprite;
    private String textureName;
    private String type; // type is for building type like monument or random building

    public void setSprite(Sprite sprite){
        this.sprite=sprite;
    }
    public void setTextureName(String textureName){
        this.textureName=textureName;
    }
    public void setType(String type){
        this.type=type;
    }
    public Sprite getSprite(){
        return sprite;
    }
    public String getTextureName(){
        return textureName;
    }
    public String getType(){
        return type;
    }
}

public class CircularQueue {
    private MyGame game;
    private final int MAX_SIZE;
    private Array<QueueObject> activeObjectList;
    private float posY,scale,movespeed;
    private Sprite tempSprite;

    // 0 texture name 1 type
    private Array<Array<String>> textureQueue;// texture waiting to be shifted to active queue

    private String activeTextureName="";
    private final String TEXTURE_ATLAS_NAME;
    public int MAX_TEXTURE_SIZE;

    public CircularQueue(MyGame game,String TEXTURE_ATLAS_NAME,int MAX_SIZE,int MAX_TEXTURE_SIZE,float scale,float posY,float movespeed){
        this.game=game;
        this.TEXTURE_ATLAS_NAME=TEXTURE_ATLAS_NAME;
        this.MAX_SIZE=MAX_SIZE;
        this.MAX_TEXTURE_SIZE=MAX_TEXTURE_SIZE;
        this.posY=posY;
        this.scale=scale;
        this.movespeed=movespeed;

        activeObjectList =new Array<QueueObject>();
        textureQueue=new Array<Array<String>>();
        tempSprite=new Sprite();
    }

//  should call enqueue before updating

    public void enqueueTextureQueue(String textureName,String type){
        textureQueue.add(new Array<String>(new String[]{textureName,type}));
    }
    public void dequeueTextureQueue(){ textureQueue.removeIndex(0); }
    public int getTextureQueueSize(){
        return textureQueue.size;
    }

    public void createActiveObject(int size){
        QueueObject object=new QueueObject();
        object.setTextureName(textureQueue.get(0).get(0));
        object.setType(textureQueue.get(0).get(1));
        dequeueTextureQueue();

        Sprite prev= activeObjectList.size<=0?null:activeObjectList.get(size-1).getSprite();
        tempSprite=new Sprite(game.resource.getTextureRegion(TEXTURE_ATLAS_NAME,object.getTextureName()));
        tempSprite.setPosition(prev==null?0:prev.getX()+prev.getWidth(),posY);
        tempSprite.setSize(tempSprite.getWidth()*scale, tempSprite.getHeight()*scale);

        object.setSprite(tempSprite);
        activeObjectList.add(object);
    }
    public String getActiveTextureName(){
        return activeTextureName;
    }
    public void update(){
        //create new object at the end
        while(activeObjectList.size<MAX_SIZE){
            createActiveObject(activeObjectList.size);
        }
        // update position
        for(int i=0;i<activeObjectList.size;i++){
            tempSprite = activeObjectList.get(i).getSprite();
            tempSprite.setPosition(tempSprite.getX()-movespeed*Constants.MOVE_DIRECTION, tempSprite.getY());
            if(tempSprite.getX()<Constants.CAMERA_WIDTH*0.4f && tempSprite.getX()>0 && activeObjectList.get(i).getType().equals("landmark")){
                activeTextureName=activeObjectList.get(i).getTextureName();
            }
        }
        //destroy object outside the screen limit
        if(activeObjectList.get(0).getSprite().getX()+activeObjectList.get(0).getSprite().getWidth()<-Constants.CAMERA_WIDTH){
            activeObjectList.removeIndex(0);
        }
    }
    public void render(){
        for(int i=0;i<activeObjectList.size;i++){
            tempSprite =activeObjectList.get(i).getSprite();
            if(game.utils.inRange(tempSprite.getX(),0-tempSprite.getWidth()*1.5f,Constants.CAMERA_WIDTH*1.5f)){
                game.batch.draw(tempSprite, tempSprite.getX(), tempSprite.getY(), tempSprite.getWidth(), tempSprite.getHeight());
            }
        }
    }
    public void stopScroll(){
        movespeed=0;
    }

    public void dispose(){

    }
}
