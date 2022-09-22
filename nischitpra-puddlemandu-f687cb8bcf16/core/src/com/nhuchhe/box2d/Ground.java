package com.nhuchhe.box2d;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.Iterator;

/**
 * Created by nischit on 3/7/17.
 */

public class Ground{
    private MyGame game;
    private Array<GameObject> grounds;
    private final Pool<GameObject> gameObjectPool=new Pool<GameObject>(){
        @Override
        protected GameObject newObject() {
            return new GameObject();
        }
    };


    private String[] jsonName,spriteName;
    private Array<Integer> terrain=new Array<Integer>();
    private GameObject tempObject;

    private Array<Integer> tempArrayInteger;

    private Vector2 tempVector;
    private GameScreen screen;


    public Ground(MyGame game,GameScreen screen) {
        this.game=game;
        this.screen=screen;
        grounds=new Array<GameObject>();

        //this file contains the structure of all of the ground
        jsonName=new String[]{"ground.json"};
        spriteName =new String[]{
                "ground0",
                "ground1",
                "ground2",
                "ground3",
                "ground4",
                "ground5",
                "ground6",
                "ground7",
                "ground8",

                "puddle0",
                "puddle1",
                "puddle2",
                "puddle3",
                "puddle7",
                "puddle8",


                "ground9",
                "ground11",
                "ground12",
                "ground13",
                "ground14",

                "puddle9",

               };
        tempObject =gameObjectPool.obtain();
        tempObject.init(game.world,
                game.resource.getSprite(spriteName[0]),
                game.resource.getBodyEditorLoader(jsonName[0]),
                spriteName[0],
                new Vector2(0,0),
                BodyDef.BodyType.StaticBody);

        tempObject.setUserData(Constants.GROUND);
        grounds.add(tempObject);
        tempVector=new Vector2();

        terrain.add(0);
        generateGround(true);
        terrain.add(0);
        generateGround(true);
        terrain.add(0);
        generateGround(true);
        terrain.add(0);
        generateGround(true);
        terrain.add(0);
        generateGround(true);
        terrain.add(0);
        generateGround(true);
        terrain.add(0);
        generateGround(true);
        terrain.add(0);
        generateGround(true);


        tempArrayInteger=new Array<Integer>();
    }
    private void generateGround(boolean skip){
        if(!skip){
            terrainGenerator();
        }
        int i=terrain.get(terrain.size-1);
        tempObject =gameObjectPool.obtain();
        tempObject.init(
                game.world,
                game.resource.getSprite(spriteName[i]),
                game.resource.getBodyEditorLoader(jsonName[0]),
                spriteName[i],
                tempVector.set(grounds.get(grounds.size-1).getPosition().x+
                        grounds.get(grounds.size-1).getWidth(), 0),
                BodyDef.BodyType.StaticBody);

        if(spriteName[i].contains("ground")){
            tempObject.setUserData(Constants.GROUND);
        }else{
            tempObject.addFixture(game.resource.getBodyEditorLoader(jsonName[0]),spriteName[i]+"_water",Constants.PUDDLE);
        }
        grounds.add(tempObject);
    }


    public void update(Vector2 playerPosition){
//        Iterator<GameObject> iterator=grounds.iterator();
        for (int i=0;i<grounds.size;i++){
            tempObject =grounds.get(i);

            // used to create ground if last block is close to player
            if(grounds.get(grounds.size-1).getPosition().x+ tempObject.getWidth()<playerPosition.x+Constants.WIDTH*1f){
                generateGround(false);
            }
            // used to remove grounds beyond certain blocks away from player
            if(tempObject.getPosition().x+ tempObject.getWidth()+Constants.CAMERA_WIDTH*0.07f<playerPosition.x){
                gameObjectPool.free(tempObject);
//                Constants.deadBodies.add(tempObject.getBody());
                grounds.removeIndex(i);
            }
        }
    }
    public void render(){
//        Iterator<GameObject> iterator=grounds.iterator();
        for (int i=0;i<grounds.size;i++){
            tempObject =grounds.get(i);
            if(game.utils.inRange(tempObject.getPosition().x,screen.character.getPosition().x-Constants.WIDTH*0.5f-tempObject.getWidth(),screen.character.getPosition().x+Constants.WIDTH)){
                game.batch.draw(tempObject.getSprite(),
                        tempObject.getPosition().x,
                        tempObject.getPosition().y,
                        tempObject.getWidth(),
                        tempObject.getHeight());
            }
        }
        //draw highscore thing,, should be ontop of everything that has previously been drawn
        if(game.utils.inRange(game.preferences.getFloat(Constants.HIGHSCORE),screen.character.getPosition().x-Constants.WIDTH*0.5f-tempObject.getWidth(),screen.character.getPosition().x+Constants.WIDTH)){
            game.batch.draw(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_GAMEWORLD, "highscore"),
                    game.preferences.getFloat(Constants.HIGHSCORE), 1
                    , game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_GAMEWORLD, "highscore").getRegionWidth() * Constants.SCALE.x,
                    game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_GAMEWORLD, "highscore").getRegionHeight() * Constants.SCALE.y);
        }
    }

    /**
     *
     * ground0   low plain              low     0     0,1,8,9,10,15,16,19
     * ground1   low to high slope      high    1     2,3,4,5,6,7,11,12,13,17,18,20
     * ground2   high to low slope      low     2     0,1,8,9,10,15,16,19
     * ground3   high plain             high    3     2,3,4,5,6,7,11,12,13,17,18,20
     * ground4   high plain             high    4     2,3,4,5,6,7,11,12,13,17,18,20
     * ground_5  high to low            low     5     0,1,8,9,10,15,16,19
     * ground_6  high to high           high    6     2,3,4,5,6,7,11,12,13,17,18,20
     * ground_7  high to low            low     7     0,1,8,9,10,15,16,19
     * ground_8  low to high            high    8     2,3,4,5,6,7,11,12,13,17,18,20

     * puddle0 low puddle               low     9     0,1,8,15,16,19
     * puddle1 low to high puddle       high    10    2,3,4,5,6,7,17,18
     * puddle2 high to low puddle       low     11    0,1,8,15,16,19
     * puddle3 high puddle              high    12    2,3,4,5,6,7,17,18
     * puddle7 high start open puddle   high    13    14
     * puddle8 high end open puddle     high    14    2,3,4,5,6,7,17,18
     *
     * ground_9    low to low           low     15    0,1,8,9,10,15,16,19
     * ground_11   low to high          high    16    2,3,4,5,6,7,11,12,13,17,18,20
     * ground_12   high to high         high    17    2,3,4,5,6,7,11,12,13,17,18,20
     * ground_13   high to high         high    18    2,3,4,5,6,7,11,12,13,17,18,20
     * ground_14   low to low            low    19    0,1,8,9,10,15,16,19
     *
     * puddle_9    high to high         high    20    2,3,4,5,6,7,17,18
     *
     */
    // terrain generator
    private void terrainGenerator(){
        switch(terrain.get(terrain.size-1)){
            case 0:
                tempArrayInteger.clear();
                tempArrayInteger.add(0);
                tempArrayInteger.add(1);
                tempArrayInteger.add(8);
                tempArrayInteger.add(9);
                tempArrayInteger.add(10);
                tempArrayInteger.add(15);
                tempArrayInteger.add(16);
                tempArrayInteger.add(19);
                addTerrain(tempArrayInteger);
                break;
            case 1:
                tempArrayInteger.clear();
                tempArrayInteger.add(2);
                tempArrayInteger.add(3);
                tempArrayInteger.add(4);
                tempArrayInteger.add(5);
                tempArrayInteger.add(6);
                tempArrayInteger.add(7);
                tempArrayInteger.add(11);
                tempArrayInteger.add(12);
                tempArrayInteger.add(13);
                tempArrayInteger.add(17);
                tempArrayInteger.add(18);
                tempArrayInteger.add(20);
                addTerrain(tempArrayInteger);
                break;
            case 2:
                tempArrayInteger.clear();
                tempArrayInteger.add(0);
                tempArrayInteger.add(1);
                tempArrayInteger.add(8);
                tempArrayInteger.add(9);
                tempArrayInteger.add(10);
                tempArrayInteger.add(15);
                tempArrayInteger.add(16);
                tempArrayInteger.add(19);
                addTerrain(tempArrayInteger);
                break;
            case 3:
                tempArrayInteger.clear();
                tempArrayInteger.add(2);
                tempArrayInteger.add(3);
                tempArrayInteger.add(4);
                tempArrayInteger.add(5);
                tempArrayInteger.add(6);
                tempArrayInteger.add(7);
                tempArrayInteger.add(11);
                tempArrayInteger.add(12);
                tempArrayInteger.add(13);
                tempArrayInteger.add(17);
                tempArrayInteger.add(18);
                tempArrayInteger.add(20);
                addTerrain(tempArrayInteger);
                break;
            case 4:
                tempArrayInteger.clear();
                tempArrayInteger.add(2);
                tempArrayInteger.add(3);
                tempArrayInteger.add(4);
                tempArrayInteger.add(5);
                tempArrayInteger.add(6);
                tempArrayInteger.add(7);
                tempArrayInteger.add(11);
                tempArrayInteger.add(12);
                tempArrayInteger.add(13);
                tempArrayInteger.add(17);
                tempArrayInteger.add(18);
                tempArrayInteger.add(20);
                addTerrain(tempArrayInteger);
                break;
            case 5:
                tempArrayInteger.clear();
                tempArrayInteger.add(0);
                tempArrayInteger.add(1);
                tempArrayInteger.add(8);
                tempArrayInteger.add(9);
                tempArrayInteger.add(10);
                tempArrayInteger.add(15);
                tempArrayInteger.add(16);
                tempArrayInteger.add(19);
                addTerrain(tempArrayInteger);
                break;
            case 6:
                tempArrayInteger.clear();
                tempArrayInteger.add(2);
                tempArrayInteger.add(3);
                tempArrayInteger.add(4);
                tempArrayInteger.add(5);
                tempArrayInteger.add(6);
                tempArrayInteger.add(7);
                tempArrayInteger.add(11);
                tempArrayInteger.add(12);
                tempArrayInteger.add(13);
                tempArrayInteger.add(17);
                tempArrayInteger.add(18);
                tempArrayInteger.add(20);
                addTerrain(tempArrayInteger);
                break;
            case 7:
                tempArrayInteger.clear();
                tempArrayInteger.add(0);
                tempArrayInteger.add(1);
                tempArrayInteger.add(8);
                tempArrayInteger.add(9);
                tempArrayInteger.add(10);
                tempArrayInteger.add(15);
                tempArrayInteger.add(16);
                tempArrayInteger.add(19);
                addTerrain(tempArrayInteger);
                break;
            case 8:
                tempArrayInteger.clear();
                tempArrayInteger.add(2);
                tempArrayInteger.add(3);
                tempArrayInteger.add(4);
                tempArrayInteger.add(5);
                tempArrayInteger.add(6);
                tempArrayInteger.add(7);
                tempArrayInteger.add(11);
                tempArrayInteger.add(12);
                tempArrayInteger.add(13);
                tempArrayInteger.add(17);
                tempArrayInteger.add(18);
                tempArrayInteger.add(20);
                addTerrain(tempArrayInteger);
                break;
            case 9:
                tempArrayInteger.clear();
                tempArrayInteger.add(0);
                tempArrayInteger.add(1);
                tempArrayInteger.add(8);
                tempArrayInteger.add(15);
                tempArrayInteger.add(16);
                tempArrayInteger.add(19);
                addTerrain(tempArrayInteger);
                break;
            case 10:
                tempArrayInteger.clear();
                tempArrayInteger.add(2);
                tempArrayInteger.add(3);
                tempArrayInteger.add(4);
                tempArrayInteger.add(5);
                tempArrayInteger.add(6);
                tempArrayInteger.add(7);
                tempArrayInteger.add(17);
                tempArrayInteger.add(18);
                addTerrain(tempArrayInteger);
                break;
            case 11:
                tempArrayInteger.clear();
                tempArrayInteger.add(0);
                tempArrayInteger.add(1);
                tempArrayInteger.add(8);
                tempArrayInteger.add(15);
                tempArrayInteger.add(16);
                tempArrayInteger.add(19);
                addTerrain(tempArrayInteger);
                break;
            case 12:
                tempArrayInteger.clear();
                tempArrayInteger.add(2);
                tempArrayInteger.add(3);
                tempArrayInteger.add(4);
                tempArrayInteger.add(5);
                tempArrayInteger.add(6);
                tempArrayInteger.add(7);
                tempArrayInteger.add(17);
                tempArrayInteger.add(18);
                addTerrain(tempArrayInteger);
                break;
            case 13:
                tempArrayInteger.clear();
                tempArrayInteger.add(14);
                addTerrain(tempArrayInteger);
                break;
            case 14:
                tempArrayInteger.clear();
                tempArrayInteger.add(2);
                tempArrayInteger.add(3);
                tempArrayInteger.add(4);
                tempArrayInteger.add(5);
                tempArrayInteger.add(6);
                tempArrayInteger.add(7);
                tempArrayInteger.add(17);
                tempArrayInteger.add(18);
                addTerrain(tempArrayInteger);
                break;
            case 15:
                tempArrayInteger.clear();
                tempArrayInteger.add(0);
                tempArrayInteger.add(1);
                tempArrayInteger.add(8);
                tempArrayInteger.add(9);
                tempArrayInteger.add(10);
                tempArrayInteger.add(15);
                tempArrayInteger.add(16);
                tempArrayInteger.add(19);
                addTerrain(tempArrayInteger);
                break;
            case 16:
                tempArrayInteger.clear();
                tempArrayInteger.add(2);
                tempArrayInteger.add(3);
                tempArrayInteger.add(4);
                tempArrayInteger.add(5);
                tempArrayInteger.add(6);
                tempArrayInteger.add(7);
                tempArrayInteger.add(11);
                tempArrayInteger.add(12);
                tempArrayInteger.add(13);
                tempArrayInteger.add(17);
                tempArrayInteger.add(18);
                tempArrayInteger.add(20);
                addTerrain(tempArrayInteger);
                break;
            case 17:
                tempArrayInteger.clear();
                tempArrayInteger.add(2);
                tempArrayInteger.add(3);
                tempArrayInteger.add(4);
                tempArrayInteger.add(5);
                tempArrayInteger.add(6);
                tempArrayInteger.add(7);
                tempArrayInteger.add(11);
                tempArrayInteger.add(12);
                tempArrayInteger.add(13);
                tempArrayInteger.add(17);
                tempArrayInteger.add(18);
                tempArrayInteger.add(20);
                addTerrain(tempArrayInteger);
                break;
            case 18:
                tempArrayInteger.clear();
                tempArrayInteger.add(2);
                tempArrayInteger.add(3);
                tempArrayInteger.add(4);
                tempArrayInteger.add(5);
                tempArrayInteger.add(6);
                tempArrayInteger.add(7);
                tempArrayInteger.add(11);
                tempArrayInteger.add(12);
                tempArrayInteger.add(13);
                tempArrayInteger.add(17);
                tempArrayInteger.add(18);
                tempArrayInteger.add(20);
                addTerrain(tempArrayInteger);
                break;
            case 19:
                tempArrayInteger.clear();
                tempArrayInteger.add(0);
                tempArrayInteger.add(1);
                tempArrayInteger.add(8);
                tempArrayInteger.add(9);
                tempArrayInteger.add(10);
                tempArrayInteger.add(15);
                tempArrayInteger.add(16);
                tempArrayInteger.add(19);
                addTerrain(tempArrayInteger);
                break;
            case 20:
                tempArrayInteger.clear();
                tempArrayInteger.add(2);
                tempArrayInteger.add(3);
                tempArrayInteger.add(4);
                tempArrayInteger.add(5);
                tempArrayInteger.add(6);
                tempArrayInteger.add(7);
                tempArrayInteger.add(17);
                tempArrayInteger.add(18);
                addTerrain(tempArrayInteger);
                break;
        }
    }
    private void addTerrain(Array<Integer> list){
        terrain.add(list.get(MathUtils.random(0,list.size-1)));
    }

    public void dispose(){
        Iterator<GameObject> iterator=grounds.iterator();
        while(iterator.hasNext()){
            tempObject =iterator.next();
            tempObject.dispose();
            tempObject.setBody(null);
            iterator.remove();
        }
    }
}
