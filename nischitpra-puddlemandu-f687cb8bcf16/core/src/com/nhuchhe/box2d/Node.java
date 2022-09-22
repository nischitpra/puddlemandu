package com.nhuchhe.box2d;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by nischit on 31/7/17.
 */

public class Node implements Pool.Poolable{
    private MyGame game;
    private Body body;
    private Vector2 position,parentPosition;
    private float length,width=0.05f;
    private Node left,right;
    private float tangent;
    private Sprite s;

    private Vector2[] vector;

    public Node(){
        vector =new Vector2[4];
        for(int i=0;i<vector.length;i++){
            vector[i]=new Vector2();
        }
        position=new Vector2();
    }


    public void init(MyGame game, Body body){
        this.game=game;
        this.body=body;
        left=null;
        right=null;

    }
    public void setLeft(Node left){ this.left=left; }
    public void setRight(Node right){ this.right=right; }
    public Node getLeft(){return left;}
    public Node getRight(){return right;}
    public Vector2 getChildPosition(){ return position; }

    public void createFixture(){
        FixtureDef fixtureDef=new FixtureDef();
        fixtureDef.isSensor=true;
        PolygonShape p=new PolygonShape();

        vector[0].set(parentPosition.x,parentPosition.y);
        vector[1].set((float)(parentPosition.x+width*Math.cos(tangent)),(float)(parentPosition.y+width*Math.sin(tangent)));
        vector[2].set(position.x,position.y);
        vector[3].set((float)(position.x+width*Math.cos(tangent)),(float)(position.y+width*Math.sin(tangent)));


        p.set(vector);
        fixtureDef.shape= p;
        Fixture fixture=body.createFixture(fixtureDef);
        fixture.setUserData(Constants.BOLT);

    }

    public void createPosition(Vector2 parentPosition){
        position.set(MathUtils.random(parentPosition.x-Constants.GRAPH_EDGE.x*0.5f,parentPosition.x+Constants.GRAPH_EDGE.x*0.5f),
                MathUtils.random(parentPosition.y-Constants.GRAPH_EDGE.y*0.2f,parentPosition.y-Constants.GRAPH_EDGE.y*1.0f));
        tangent=MathUtils.radiansToDegrees*(MathUtils.atan2(parentPosition.y-position.y,parentPosition.x-position.x));
        length= (float) Math.sqrt(Math.pow(parentPosition.x-position.x,2)+Math.pow(parentPosition.y-position.y,2));
        this.parentPosition=parentPosition;

        createFixture();
    }
    public Vector2 getParentPosition(){
        return parentPosition;
    }
    public void setParentPosition(Vector2 position){
        parentPosition=position;
    }
    public void render(float displacement){
        s=game.resource.getSprite("bolt");
        s.setPosition(parentPosition.x,parentPosition.y);
        s.setScale(length/s.getWidth(),width/s.getHeight());
        s.setOrigin(s.getWidth(),0);
        s.setRotation(tangent);
        s.draw(game.batch);
        position.x+=displacement;
    }
    public void renderGlow(){
        s=game.resource.getSprite("lightning");
        s.setOrigin(s.getWidth()/2,s.getHeight()/2);
        s.setPosition(parentPosition.x-s.getWidth()/2,parentPosition.y-s.getHeight()/2);
        s.setAlpha(0.13f);
        s.setScale(2);
        s.draw(game.batch);
    }

    @Override
    public void reset() {
        body=null;
        left=null;
        right=null;
    }
}

