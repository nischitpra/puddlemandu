package com.nhuchhe.box2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * Created by nischit on 3/7/17.
 */

public class Constants {
    /**
     * to convert box2d coordinate to camera coordinate divide my Width or height
     * then multiply by camera width or height
     */

    public static final float CAMERA_WIDTH=Gdx.graphics.getWidth() ;
    public static final float CAMERA_HEIGHT=Gdx.graphics.getHeight() ;

    //this scaling is for box2d
    public static final float WIDTH= (CAMERA_WIDTH/CAMERA_HEIGHT)*10.0f;
    public static final float HEIGHT=10.0f;
    public static final Vector2 SCALE=new Vector2(0.008f,0.008f);
    public static final int TEXT_SIZE_BIG=128;
    public static final int TEXT_SIZE_SMALL=64;
    public static final int TEXT_SIZE_VERY_SMALL=50;
    public static final int TEXT_SIZE_MEDIUM=96;
    public static final long NANOSECOND=1000000000;
    public static final float ANIMATION_SPEED=1.0f/30.0f; // lower is faster
    public static final float FACT_SPEED=12;

    // random area for creating new bolt position
    public static final Vector2 GRAPH_EDGE=new Vector2(WIDTH*0.28f,HEIGHT*0.14f);

    public static final int GUIDE=12;
    public static final float BUTTON_HEIGHT=Constants.CAMERA_HEIGHT/ GUIDE;


    public static final long TAP=50000000;
    public static final float GRAVITY=-78.0f;
    public static final Vector2 LIGNTNING_PLAYTIME=new Vector2(0.1f,1f);
    public static final Vector2 LIGNTNING_WAITTIME=new Vector2(0.1f,3f);

    public static final float ANIMATION_DURATION=0.125f;

    public static final String PLAYER="PLAYER";
    public static final String GROUND="GROUND";
    public static final String PUDDLE="PUDDLE";
    public static final String VEHICLE ="VEHICLE";
    public static final String COIN = "COIN";
    public static final String DEAD = "DEAD";
    public static final String BOLT = "BOLT";
    public static final String FACT_INDEX= "FACT_INDEX";



    public static final String VEHICLE_FRONT ="VEHICLE_FRONT";
    public static final String FOOT_DETECTOR="FOOT_DETECTOR";
    public static final String PLAYER_SPRITESHEET="PLAYER_SPRITESHEET";
    public static final String FLIP_SPRITESHEET="PLAYER_FRAME_ROWS";
    public static final String HIGHSCORE="HIGHSCORE";
    public static final String SCORE="SCORE";
    public static final String NEW_HIGHSCORE="NEW";
    public static final String PLACE="PLACE";
    public static final String MUSIC="MUSIC";
    public static final String JUMP="JUMP";
    public static final String PARTICLE_EFFECT="PARTICLE_EFFECT";


    public static final String TEXTURE_ATLAS_HUD="hud.pack";
    public static final String TEXTURE_ATLAS_GAMEWORLD="gameWorld.pack";
    public static final String TEXTURE_ATLAS_BACKGROUND="background.pack";
    public static final String TEXTURE_ATLAS_FOREGROUND="foreground.pack";


    public static HashMap<Integer,Array<String>> resourceToLoad;

    public static int MOVE_DIRECTION=1;
    public static boolean DAY=true;


    public static Array<Body> deadBodies=new Array();

    public static void reset(){
        MOVE_DIRECTION=1;
        deadBodies=new Array();
    }
}