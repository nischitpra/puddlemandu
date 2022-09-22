package com.nhuchhe.box2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by nischit on 3/7/17.
 */

public class Character extends GameObject {
    private MyGame game;
    private final float MOVEMENT_SPEED=9.5f;
    private final Vector2 JUMP_FORCE=new Vector2(0,900f);

    private boolean deadOnce=false;
    public int JUMP_COUNT;
    public boolean isGrounded=false;
    public int coinCount=0;
    public String deathType;
    public boolean isDead=false;
    public long deadTime;
    public int deadDirection;
    public float score;


    //for animation
    private Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
    private float stateTime;
    private final boolean FLIP_SPRITESHEET;

    // temporary variables
    private Vector2 tempVector;



    public Character(MyGame game){
        super();
        JUMP_COUNT=game.preferences.getInteger(Constants.JUMP);

        walkAnimation=game.resource.getAnimationSequence(game.preferences.getString(Constants.PLAYER_SPRITESHEET));
        Sprite sprite=new Sprite(walkAnimation.getKeyFrame(stateTime,true));
        sprite.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);
        sprite.setSize(sprite.getWidth()*Constants.SCALE.x,sprite.getHeight()*Constants.SCALE.y);

        init(game.world, sprite,
                game.resource.getBodyEditorLoader("player.json"),
                game.preferences.getString(Constants.PLAYER_SPRITESHEET),
                new Vector2((Constants.CAMERA_WIDTH/4)/Constants.CAMERA_WIDTH*Constants.WIDTH,
                        (Constants.CAMERA_HEIGHT/2)/Constants.CAMERA_HEIGHT*Constants.HEIGHT));


        this.FLIP_SPRITESHEET=game.preferences.getBoolean(Constants.FLIP_SPRITESHEET);
        this.game=game;

        // for loading animation

        this.setFixedRotation(true);

        FixtureDef fixtureDef=new FixtureDef();
        fixtureDef.isSensor=true;
        PolygonShape p=new PolygonShape();
        p.setAsBox(10f*Constants.SCALE.x,20f*Constants.SCALE.y, new Vector2(getWidth()/2,getHeight()*0.1f),0.0f);
        fixtureDef.shape= p;
        Fixture fixture=this.getBody().createFixture(fixtureDef);
        fixture.setUserData(Constants.FOOT_DETECTOR);
        getBody().setUserData(Constants.PLAYER);

        // so that every character has the same mass
        MassData massData=new MassData();
        massData.mass=0.70f;
        this.getBody().setMassData(massData);

        tempVector=new Vector2();
    }
    public void jump(){
        if(JUMP_COUNT>0){
            Body b=this.getBody();
            b.setLinearVelocity(0, 0);
            b.applyForce(JUMP_FORCE,getBody().getWorldCenter(),true);
            JUMP_COUNT--;
            isGrounded=false;
            game.audioManager.play("jump",false);
        }
    }

    public void move(){
        if(!isDead){
            setLinearVelocity(tempVector.set(MOVEMENT_SPEED*Constants.MOVE_DIRECTION,getLinearVelocity().y));
        }else if(deathType==Constants.VEHICLE){
            if(!deadOnce){
                setLinearVelocity(tempVector.set(-MOVEMENT_SPEED/2, MathUtils.random(20, 40)));
                getBody().setAngularVelocity(10*deadDirection);
                deadOnce=true;
            }
        }else if(deathType==Constants.PUDDLE){
            if(getBody().getTransform().getRotation()*MathUtils.radiansToDegrees<3f){
                setLinearVelocity(tempVector.set(0,0));
                getBody().setAngularVelocity(3*deadDirection*-1);
            }
        }else if(deathType==Constants.BOLT){
            if(getBody().getTransform().getRotation()*MathUtils.radiansToDegrees<3f){
                setLinearVelocity(tempVector.set(5*deadDirection,0));
                getBody().setAngularVelocity(3*deadDirection*-1);
                deadDirection*=-1;
            }
        }
    }
    public void update(){
        if(getPosition().y<0){
            game.audioManager.play("scream",false);
            ((GameScreen)game.getScreen()).endGame();
            return;
        }
        move();
        if(isDead){
            if(TimeUtils.nanoTime()>deadTime) ((GameScreen)game.getScreen()).endGame();
        }

        animateCharacter();
    }

    public void render(){
        stateTime+= Gdx.graphics.getDeltaTime();
        getSprite().draw(game.batch);

        // for highsccore
        if(getPosition().x>game.preferences.getFloat(Constants.HIGHSCORE)){
            game.audioManager.play("highscore",false);
        }
    }

    private void animateCharacter(){
        Sprite s;
        if(!isDead){
            s=new Sprite(walkAnimation.getKeyFrame(stateTime, true));
        }else{
            s=new Sprite(walkAnimation.getKeyFrame(0, true));
        }
        if(FLIP_SPRITESHEET) s.flip(true,false);
        float scale=getHeight()/s.getHeight();
        s.setSize(s.getWidth()*scale,getHeight());

        s.setOrigin(getWidth()/2 *Constants.SCALE.x,getHeight()/2 *Constants.SCALE.y);
        s.setPosition(getPosition().x,getPosition().y);
        s.setRotation(game.utils.getRadianToDegree(getBody().getAngle()));
        if(Constants.MOVE_DIRECTION<0)
            s.flip(true,false);
        setSprite(s);

        if(getPosition().x>game.preferences.getFloat(Constants.HIGHSCORE)){
            game.audioManager.play("highscore",false);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        walkAnimation=null;
    }
}
