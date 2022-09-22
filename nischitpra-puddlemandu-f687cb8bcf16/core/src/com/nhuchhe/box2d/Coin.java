package com.nhuchhe.box2d;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

/**
 * Created by nischit on 13/7/17.
 */

public class Coin {
    MyGame game;
    int MAX_COIN=1;
    Array<GameObject> activeCoinList;
    String string_coin;
    private long lastGenerated;
    private Vector2 lastPosition;

    private Vector2 tempVector;
    private GameScreen screen;
    private Pool<GameObject> gameObjectPool=new Pool<GameObject>() {
        @Override
        protected GameObject newObject() {
            return new GameObject();
        }
    };

    public Coin(MyGame game,GameScreen screen){
        this.game=game;
        this.screen=screen;
        activeCoinList=new Array<GameObject>();
        string_coin="coin";
        lastPosition=new Vector2(MathUtils.random(20,40)+Constants.WIDTH,MathUtils.random(Constants.HEIGHT*0.35f,Constants.HEIGHT*0.60f));
        tempVector=new Vector2();
    }
    private Vector2 getPosition(float positionX){
        if(positionX<lastPosition.x){
            lastPosition.x+=MathUtils.random(20,40)+Constants.WIDTH;
        }else{
            lastPosition.x=positionX+MathUtils.random(20,40)+Constants.WIDTH;
        }
        tempVector.set(lastPosition.x,MathUtils.random(Constants.HEIGHT*0.35f,Constants.HEIGHT*0.60f));
        return tempVector;
    }

    public void generateCoin(float positionX){
        if(activeCoinList.size==MAX_COIN) return;
        GameObject object= gameObjectPool.obtain();
        object.init(game.world,
                game.resource.getSprite(string_coin),
                game.resource.getBodyEditorLoader("sprites.json"),
                string_coin,
                getPosition(positionX),
                BodyDef.BodyType.DynamicBody);

        object.getBody().getFixtureList().get(0).setUserData(Constants.COIN);
        object.getBody().getFixtureList().get(0).setSensor(true);
        object.getBody().setUserData(activeCoinList.size+"");
        object.getBody().setGravityScale(0);
        activeCoinList.add(object);

    }
    public void maintainCoin(float positionX){
        Iterator<GameObject> iterator =activeCoinList.iterator();
        if(TimeUtils.nanoTime()>lastGenerated){
            generateCoin(positionX);
            lastGenerated=game.utils.randomTime(3f,8f);
        }
        while(iterator.hasNext()){
            GameObject object=iterator.next();
            // used to destroy first Coin
            if(object.getPosition().x<positionX-Constants.WIDTH){
                destoryCoin(Integer.parseInt(object.getBody().getUserData().toString()));
            }
        }
    }

    public void update(float positionX){
        maintainCoin(positionX);
    }
    public void render(){
//        Iterator<GameObject> iterator =activeCoinList.iterator();
        for(int i=0;i<activeCoinList.size;i++){
            GameObject object=activeCoinList.get(i);

            if(game.utils.inRange(object.getPosition().x,screen.character.getPosition().x-Constants.WIDTH*0.5f-object.getWidth(),screen.character.getPosition().x+Constants.WIDTH)){
                game.batch.draw(object.getSprite(),
                        object.getPosition().x,object.getPosition().y,
                        object.getSprite().getWidth(),object.getSprite().getHeight());
            }
        }
    }



    public void destoryCoin(int index){
        gameObjectPool.free(activeCoinList.get(index));
//        Constants.deadBodies.add(activeCoinList.get(index).getBody());
        activeCoinList.removeIndex(index);
    }
    public void dispose(){
        Iterator<GameObject> iterator=activeCoinList.iterator();
        while (iterator.hasNext()){
            GameObject object=iterator.next();
            object.dispose();
            object.setBody(null);
            iterator.remove();
        }
    }
}
