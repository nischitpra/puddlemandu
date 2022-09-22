package com.nhuchhe.box2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;


/**
 * Created by nischit on 10/7/17.
 */

public class Vehicle  {
    private MyGame game;
    private Array<GameObject> activeVehicle;
    private Array<String> string_vehicle;
    private Array<String> sound_string;
    private GameObject temp;
    private GameScreen screen;
    private int MAX_VEHICLE=1;

    private long lastGenerated;

    private Vector2 tempVector;

    Vector2 VELOCITY=new Vector2(-MathUtils.random(7,10),-5);
    //for animation
    private Animation<TextureRegion> walkAnimation;
    private float stateTime;
    private Pool<GameObject> gameObjectPool=new Pool<GameObject>() {
        @Override
        protected GameObject newObject() {
            return new GameObject();
        }
    };

    public Vehicle(MyGame game,GameScreen screen){
        this.game=game;
        this.screen=screen;
        activeVehicle=new Array<GameObject>();
        string_vehicle=new Array<String>();
        sound_string=new Array<String>();

        string_vehicle.add("sajhaBus");
        string_vehicle.add("motorcycle0");
        string_vehicle.add("motorcycle1");
        string_vehicle.add("tampo");
        string_vehicle.add("microbus");
        string_vehicle.add("beetleCar");
//        string_vehicle.add("cow");
        string_vehicle.add("zombie");
        string_vehicle.add("dog0");
        string_vehicle.add("dog1");
        string_vehicle.add("ostrich");
        string_vehicle.add("chicken");
        string_vehicle.add("tractor");

        tempVector=new Vector2();
        lastGenerated=game.utils.randomTime(10,12);
    }

    private void generateVehicle(float playerPositionX){
        if(activeVehicle.size==MAX_VEHICLE) return;
        int index=getVehicleType();
        String type=string_vehicle.get(index);
        walkAnimation=game.resource.getAnimationSequence(type);
        temp=getFromPool(type,tempVector.set(Constants.WIDTH*1.2f+playerPositionX, Constants.HEIGHT*0.4f));
        activeVehicle.add(temp);

        playSound(type);

    }

    private int getVehicleType(){
        return MathUtils.random(0,string_vehicle.size-1);
    }

    private GameObject getFromPool(String fixtureName, Vector2 position){
        temp=gameObjectPool.obtain();
        Sprite sprite=new Sprite(walkAnimation.getKeyFrame(stateTime,true));
        sprite.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);
        sprite.setSize(sprite.getWidth()*Constants.SCALE.x,sprite.getHeight()*Constants.SCALE.y);

        temp.init(game.world,
                sprite,
                game.resource.getBodyEditorLoader("sprites.json"),
                fixtureName,
                position,
                BodyDef.BodyType.DynamicBody);
        temp.addFixture(game.resource.getBodyEditorLoader("sprites.json"), fixtureName+"_front",Constants.VEHICLE_FRONT);
        return temp;
    }


    public void update(float playerPositionX){
        stateTime+= Gdx.graphics.getDeltaTime();

        if(TimeUtils.nanoTime()>lastGenerated){
            generateVehicle(playerPositionX);
            lastGenerated=game.utils.randomTime(1.0f,3f);
        }
        for(int i=0;i<activeVehicle.size;i++){
            temp=activeVehicle.get(i);

            temp.setLinearVelocity(VELOCITY);
            Sprite s=new Sprite(walkAnimation.getKeyFrame(stateTime, true));
            s.setSize(s.getWidth()*Constants.SCALE.x,s.getHeight()*Constants.SCALE.y);
            s.setOrigin(s.getWidth()/2 *Constants.SCALE.x,s.getHeight()/2 *Constants.SCALE.y);
            s.setPosition(temp.getPosition().x,temp.getPosition().y);
            s.setRotation(game.utils.getRadianToDegree(temp.getBody().getAngle()));
            temp.setSprite(s);
            // remove first active bus
            if(temp.getPosition().x+temp.getWidth()+Constants.WIDTH/2<playerPositionX||temp.getPosition().y<0){
                gameObjectPool.free(temp);
                activeVehicle.removeIndex(i);
                stopSound();
            }
        }
    }

    public void render(){
        Iterator<GameObject> iterator=activeVehicle.iterator();
        while(iterator.hasNext()){
            temp=iterator.next();
            if(game.utils.inRange(temp.getPosition().x,screen.character.getPosition().x-Constants.WIDTH*0.5f-temp.getWidth(),screen.character.getPosition().x+Constants.WIDTH)){
                temp.getSprite().draw(game.batch);
            }
        }
    }

    public void dispose() {
        Iterator<GameObject> iterator=activeVehicle.iterator();
        while(iterator.hasNext()){
            temp=iterator.next();
            temp.dispose();
            temp.setBody(null);
            iterator.remove();
        }
    }
    private void playSound(String type){
        if(type.contains("bus")||type.contains("Bus")){
            game.audioManager.play("bus",true);
            sound_string.add("bus");
        }
        else if(type.equals("motorcycle_0")){
            game.audioManager.play("motorcycle_0",true);
            sound_string.add("motorcycle_0");
        }
        else if(type.equals("motorcycle_1")){
            game.audioManager.play("motorcycle_1",true);
            sound_string.add("motorcycle_1");
        }
        else if(type.contains("tampo")){
            game.audioManager.play("motorcycle_0",true);
            sound_string.add("tampo");
        }
        else if(type.contains("car")||type.contains("Car")){
            game.audioManager.play("car",true);
            sound_string.add("car");
        }
        else if(type.contains("cow")){
            game.audioManager.play("cow",true);
            sound_string.add("cow");
        }
        else if(type.contains("dog")){
            game.audioManager.play("dog_"+MathUtils.random(0,1),true);
            sound_string.add("dog");
        }
        else if(type.contains("zombie")){
            game.audioManager.play("zombie_"+MathUtils.random(0,1),true);
            sound_string.add("zombie");
        }
        else if(type.contains("chicken")){
            game.audioManager.play("chicken_0",true);
            sound_string.add("chicken");
        }
        else if(type.contains("ostrich")){
            game.audioManager.play("chicken_0",true);
            sound_string.add("ostrich");
        }
        else if(type.contains("tractor")){
            game.audioManager.play("car",true);
            sound_string.add("tractor");
        }
    }
    private void stopSound(){
        if(sound_string.size==0) return;

        String s=sound_string.removeIndex(0);
        if(s=="bus"){
            game.audioManager.stopSound_Bus();
        } else if(s=="motorcycle_0"){
            game.audioManager.stopSound_Motorcycle_0();
        } else if(s=="motorcycle_1"){
            game.audioManager.stopSound_Motorcycle_1();
        } else if(s=="tampo"){
            game.audioManager.stopSound_Motorcycle_0();
        } else if(s=="car"){
            game.audioManager.stopSound_Car();
        } else if(s=="dog"){
            game.audioManager.stopSound_Dog_0();
            game.audioManager.stopSound_Dog_1();
        } else if(s=="cow"){
            game.audioManager.stopSound_Cow();
        } else if(s=="zombie"){
            game.audioManager.stopSound_Zombie_0();
            game.audioManager.stopSound_Zombie_1();
        } else if(s=="chicken"){
            game.audioManager.stopSound_Chicken_0();
        } else if(s=="ostrich"){
            game.audioManager.stopSound_Chicken_0();
        } else if(s=="tractor"){
            game.audioManager.stopSound_Car();
        }

    }
}
