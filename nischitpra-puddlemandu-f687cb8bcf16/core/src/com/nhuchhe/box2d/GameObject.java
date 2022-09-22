package com.nhuchhe.box2d;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;


/**
 * Created by nischit on 3/7/17.
 */

public class GameObject implements Pool.Poolable{
    private World world;
    private Body body;
    private Sprite sprite;

    public GameObject(){

    }

    public void init(World world, Sprite sprite){
        this.sprite=sprite;
        this.world=world;
    }
    public void init(World world, Sprite sprite, BodyEditorLoader loader, String fixtureName, Vector2 position){
        this.sprite=sprite;
        this.world=world;

        BodyDef bodyDef= new BodyDef();
        bodyDef.type= BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x,position.y);

        FixtureDef fixtureDef= new FixtureDef();
        fixtureDef.density=1f;

        body=world.createBody(bodyDef);
        loader.attachFixture(body,fixtureName,fixtureDef,sprite.getWidth());
    }
    public void init(World world, Sprite sprite, BodyEditorLoader loader, String fixtureName, Vector2 position, BodyDef.BodyType bodyType){
        this.sprite=sprite;
        this.world=world;

        BodyDef bodyDef= new BodyDef();
        bodyDef.type= bodyType;
        bodyDef.position.set(position.x,position.y);

        FixtureDef fixtureDef= new FixtureDef();
        fixtureDef.density=1f;

        body=world.createBody(bodyDef);
        loader.attachFixture(body,fixtureName,fixtureDef,sprite.getWidth());
    }

    public void addFixture(BodyEditorLoader loader,String fixtureName,String fixtureUserData){
        FixtureDef fixtureDef= new FixtureDef();
        fixtureDef.density=1f;
        loader.attachFixture(body,fixtureName,fixtureDef,sprite.getWidth());
        body.getFixtureList().get(body.getFixtureList().size-1).setUserData(fixtureUserData);
    }

    public void setLinearVelocity(Vector2 velocity){
        body.setLinearVelocity(velocity);
    }
    public void setUserData(String data){body.setUserData(data);}
    public void setWorld(World world){this.world=world;}
    public void setSprite(Sprite sprite){this.sprite=sprite;}

    public void setBody(Body body){
        this.body=body;
    }
    /**
     * function to add fixture to body. this will add another fixture to the existing body
     * @return
     */

    public World getWorld(){return world;}
    public Vector2 getPosition(){return body.getPosition();}
    public float getWidth(){return sprite.getWidth();}
    public float getHeight(){return sprite.getHeight();}
    public Vector2 getLinearVelocity(){return body.getLinearVelocity();}
    public Body getBody(){return body;}
    public Sprite getSprite(){return sprite;}

    public void setFixedRotation(boolean fixedRotation){body.setFixedRotation(fixedRotation);}


    public void dispose(){
        if(world!=null && body!=null)
        world.destroyBody(body);
    }


    @Override
    public void reset() {
        if(body!=null){
            Constants.deadBodies.add(body);
        }
        sprite=null;
        body=null;

    }
}
