package com.nhuchhe.box2d;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Created by nischit on 13/7/17.
 */

public class ContactChecker implements ContactListener {
    MyGame game;

    public ContactChecker(MyGame game){
        this.game=game;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if(fixtureA.getBody().getUserData()!=Constants.PLAYER && fixtureB.getBody().getUserData()!=Constants.PLAYER) return;

        Fixture nonPlayerFixture=fixtureA.getBody().getUserData()==Constants.PLAYER?fixtureB:fixtureA;
        Fixture playerFixture=fixtureA.getBody().getUserData()==Constants.PLAYER?fixtureA:fixtureB;

        if(fixtureA.getUserData()==Constants.FOOT_DETECTOR||fixtureB.getUserData()==Constants.FOOT_DETECTOR){
            ((GameScreen)game.getScreen()).character.isGrounded=true;
//            ((GameScreen)game.getScreen()).character.JUMP_COUNT=game.preferences.getInteger(Constants.JUMP);
            ((GameScreen)game.getScreen()).character.JUMP_COUNT=999999;
        }
        if(nonPlayerFixture.getUserData()==Constants.COIN){
            nonPlayerFixture.setUserData(Constants.DEAD);
            String a=contact.getFixtureA().getBody().getUserData()==Constants.PLAYER?
                    contact.getFixtureB().getBody().getUserData().toString():
                    contact.getFixtureA().getBody().getUserData().toString();

            ((GameScreen)game.getScreen()).coin.destoryCoin(Integer.parseInt(a));
            game.audioManager.play("coin",false);
            ((GameScreen)game.getScreen()).character.coinCount++;
        }

        if(fixtureA.getUserData()==Constants.FOOT_DETECTOR || fixtureB.getUserData()==Constants.FOOT_DETECTOR) return;

        if(nonPlayerFixture.getUserData()==Constants.PUDDLE){
            nonPlayerFixture.setUserData(Constants.DEAD);
            playerFixture.setUserData(Constants.DEAD);
            playerFixture.getBody().setUserData(Constants.DEAD);
            ((GameScreen)game.getScreen()).background.stopScroll();
            ((GameScreen)game.getScreen()).foreground.stopScroll();
            game.audioManager.play("splash",false);
            game.audioManager.play("scream",false);



            ((GameScreen)game.getScreen()).character.isDead=true;
            ((GameScreen)game.getScreen()).character.deathType=Constants.PUDDLE;
            ((GameScreen)game.getScreen()).character.deadTime= game.utils.randomTime(1.5f,1.5f);
            ((GameScreen)game.getScreen()).character.deadDirection= Constants.MOVE_DIRECTION;
            ((GameScreen)game.getScreen()).character.score= ((GameScreen)game.getScreen()).character.getPosition().x;
        }
        if(nonPlayerFixture.getUserData()==Constants.VEHICLE_FRONT ){
            nonPlayerFixture.setUserData(Constants.DEAD);
            playerFixture.setUserData(Constants.DEAD);
            playerFixture.getBody().setUserData(Constants.DEAD);
            ((GameScreen)game.getScreen()).background.stopScroll();
            ((GameScreen)game.getScreen()).foreground.stopScroll();

            game.audioManager.play("crash_"+ MathUtils.random(0,1),false);
            game.audioManager.play("scream",false);


            ((GameScreen)game.getScreen()).character.isDead=true;
            ((GameScreen)game.getScreen()).character.deathType=Constants.VEHICLE;
            ((GameScreen)game.getScreen()).character.deadTime= game.utils.randomTime(1.5f,1.5f);
            ((GameScreen)game.getScreen()).character.deadDirection= Constants.MOVE_DIRECTION;
            ((GameScreen)game.getScreen()).character.score= ((GameScreen)game.getScreen()).character.getPosition().x;

        }

        if(nonPlayerFixture.getUserData()==Constants.BOLT){
            nonPlayerFixture.setUserData(Constants.DEAD);

            playerFixture.setUserData(Constants.DEAD);
            playerFixture.getBody().setUserData(Constants.DEAD);
            ((GameScreen)game.getScreen()).background.stopScroll();
            ((GameScreen)game.getScreen()).foreground.stopScroll();


            game.audioManager.play("bang",false);
            game.audioManager.play("scream",false);
            game.audioManager.play("earRinging",false);


            ((GameScreen)game.getScreen()).character.isDead=true;
            ((GameScreen)game.getScreen()).character.deathType=Constants.BOLT;
            ((GameScreen)game.getScreen()).character.deadTime= game.utils.randomTime(1.5f,1.5f);
            ((GameScreen)game.getScreen()).character.deadDirection= Constants.MOVE_DIRECTION;
            ((GameScreen)game.getScreen()).character.score= ((GameScreen)game.getScreen()).character.getPosition().x;

        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
