package com.nhuchhe.box2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;


/**
 * Created by nischit on 6/7/17.
 */

public class GameScreen implements Screen {
    private MyGame game;
    public OrthographicCamera playerCamera,camera;

    /**
     * camera is used to render HUD and also background
     * playerCamera is used to render character and the ground
     *
     */

    Character character;
    Background background;
    Foreground foreground;
    Coin coin;
    private HUD hud;
    private Ground ground;
    private Controller controller;
    private Vehicle vehicle;
    private ContactChecker contactChecker;
    private ParticleSystem particleSystem,atmosphere;
    private Cloud cloud;
    private Rain rain_far,rain_near;

    private boolean updatelock =false;

    private boolean once;
    private boolean disposed=false;


    private Array<String> tempArrayString;

    private InputMultiplexer inputMultiplexer;
    private  String tempString;



    public enum Status{
        Resume,Pause
    }
    public Status status;

    public GameScreen (MyGame game){
        this.game=game;
        once=false;
        status=Status.Resume;

        playerCamera=new OrthographicCamera();
        playerCamera.setToOrtho(false, Constants.CAMERA_WIDTH,Constants.CAMERA_HEIGHT);

        camera=new OrthographicCamera();
        camera.setToOrtho(false,Constants.CAMERA_WIDTH,Constants.CAMERA_HEIGHT);
        // Center the camera so (0,0) is bottom-left in world coordinates.
        camera.position.set(Constants.CAMERA_WIDTH / 2f, Constants.CAMERA_HEIGHT / 2f, 0);

//        camera.zoom=2f;
        playerCamera.zoom=0.01f;
//        playerCamera.zoom=0.02f;
//        playerCamera.zoom=1f;

        background=new Background(game);
        hud=new HUD(game,this);
        ground=new Ground(game,this);
        character=new Character(game);
        controller=new Controller(character);
        vehicle=new Vehicle(game,this);
        coin=new Coin(game,this);
        contactChecker=new ContactChecker(game);
        rain_far=new Rain(game,6,new Vector2(3f,5f));
        rain_near=new Rain(game,3,new Vector2(5f,9f));
        foreground=new Foreground(game);


        tempArrayString=new Array<String>();
        tempArrayString.add("particle0");
        tempArrayString.add("particle1");
        tempArrayString.add("particle2");
        tempArrayString.add("particle3");
        tempArrayString.add("particle4");
        tempArrayString.add("particle5");
        tempArrayString.add("particle6");
        tempArrayString.add("particle7");
        particleSystem=new ParticleSystem(game,new Vector2(0.3f,1),5,tempArrayString,MathUtils.random(7,11),1);

        tempArrayString.clear();
        tempArrayString.add("dust1");
        tempArrayString.add("dust2");
        tempArrayString.add("dust4");
        tempArrayString.add("dust5");
        atmosphere=new ParticleSystem(game,new Vector2(0.8f,1f),8,tempArrayString, MathUtils.random(0.5f,1),2);
        cloud=new Cloud(game,this);

        game.world.setContactListener(contactChecker);

        inputMultiplexer=new InputMultiplexer();
        inputMultiplexer.addProcessor(hud);
        inputMultiplexer.addProcessor(controller);
        Gdx.input.setInputProcessor(inputMultiplexer);


        //handle updates in new thread and keep render for only rendering
        update.start();


        TimeLogger.log = true;
        TimeLogger.gatherFor = 240;

    }

    Thread update=new Thread(new Runnable() {
        @Override
        public void run() {
            while(!disposed){
                if(!updatelock) {
                    // non game world object updates here
                    if(status!=Status.Pause){
                        foreground.update();
                        background.update();
                    }
                    particleSystem.update();
                    atmosphere.update();
                    rain_near.update();
                    rain_far.update();

                    // does not allow re-update untill a render has been called
                    updatelock=true;
                }
            }
        }
    });

    @Override
    public void render(float delta) {
        TimeLogger.timeStart();
        if(status!=Status.Pause){
            game.world.step(Gdx.graphics.getDeltaTime(),6,2);
        }else{
            game.world.step(0,6,2);
        }
        Gdx.gl.glClearColor(0f,0f,0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(status!=Status.Pause) updateFunction();
        TimeLogger.timeLog("update function");
        // used to lock update call when rendering is going on
        updatelock=true;
        renderFunction();
        TimeLogger.timeLog("render function");
//        game.debugRenderer.render(game.world,playerCamera.combined);

        // destroying stuffs always come at the last
        if(!game.world.isLocked()){
            removeDeadBodies();
            if(disposed){
                disposeGame();
            }
        }
        TimeLogger.timeLog("delete bodies");
        updatelock=false;
    }

    public void updateFunction(){

        playerCamera.update();
        camera.update();

        if(background.getAlphaIncrementor()>0 && background.getAlpha()>0.9 && !once){
            // for night
            once=true;
            tempArrayString.clear();
            tempArrayString.add("dust0");
            tempArrayString.add("dust3");
            atmosphere.setParticleNameList(tempArrayString);
            Constants.DAY=false;
        }else if(background.getAlphaIncrementor()<0 && background.getAlpha()<0.1 && once ){
            //for day
            once=false;
            tempArrayString.clear();
            tempArrayString.add("dust1");
            tempArrayString.add("dust2");
            tempArrayString.add("dust4");
            tempArrayString.add("dust5");
            atmosphere.setParticleNameList(tempArrayString);
            Constants.DAY=true;
        }


        if(!character.isDead) {
            playerCamera.position.set(character.getPosition().x+Constants.WIDTH/4,Constants.HEIGHT/2.0f,0);
        }else if(character.deathType==Constants.PUDDLE){
            playerCamera.position.set(character.score+Constants.WIDTH/4,Constants.HEIGHT/2.0f,0);

        }

        // all game world objects are updated here
        coin.update(character.getPosition().x);
        cloud.update(character.getPosition().x);
        character.update();
        ground.update(character.getPosition());
        vehicle.update(character.getPosition().x);

    }
    public void renderFunction(){
        game.batch.begin();
        game.batch.setProjectionMatrix(camera.combined);
        background.render();
        rain_far.render();

        game.batch.setProjectionMatrix(playerCamera.combined);
        character.render();
        vehicle.render();
        ground.render();
        coin.render();
        cloud.render();

        game.batch.setProjectionMatrix(camera.combined);
        //to cover the ground gap
        game.batch.draw(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_GAMEWORLD,"groundExtender"),0,0,Constants.CAMERA_WIDTH,Constants.CAMERA_HEIGHT*0.05f);
        particleSystem.render();
        atmosphere.render();
        rain_near.render();
        foreground.render();

        hud.render(character);

        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {
        // Keep cameras in the same coordinate space as Constants (legacy code assumes this).
        // On iOS, width/height can differ from the expected space (HDPI); use Constants instead.
        camera.setToOrtho(false, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
        camera.position.set(Constants.CAMERA_WIDTH / 2f, Constants.CAMERA_HEIGHT / 2f, 0);

        playerCamera.setToOrtho(false, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
        // playerCamera position is driven by character in updateFunction()

        // Ensure Stage viewports track screen changes too.
        if (hud != null) hud.getViewport().update(width, height, true);
        if (controller != null) controller.getViewport().update(width, height, true);

    }

    @Override
    public void show() {

    }

    @Override
    public void pause() {
        status= GameScreen.Status.Pause;
        hud.pause.setSize(Constants.BUTTON_HEIGHT*2,Constants.BUTTON_HEIGHT*2);
        hud.pause.setPosition(game.utils.getCenterPoint(0,Constants.CAMERA_WIDTH,hud.pause.getWidth()),
                game.utils.getCenterPoint(0,Constants.CAMERA_HEIGHT,hud.pause.getHeight()));

        hud.pause.setChecked(true);

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        System.out.println("inside GameScreen dispose");
        hud.dispose();
        ground.dispose();
        character.dispose();
        background.dispose();
        controller.dispose();
        vehicle.dispose();
        coin.dispose();
        cloud.dispose();

        game.audioManager.reset();

        hud=null;
        ground=null;
        character=null;
        background=null;
        controller=null;
        vehicle=null;
        coin=null;

        disposed=true;
    }

    public void disposeGame(){
        dispose();
        game.setScreen(new MainMenuScreen(game));
    }
    public void endGame(){
        disposed=true;
        float highscore=game.preferences.getFloat(Constants.HIGHSCORE);
        float score=character.getPosition().x;
        if(highscore<score){
            game.preferences.putFloat(Constants.HIGHSCORE,score);
            game.preferences.putString(Constants.NEW_HIGHSCORE,Constants.NEW_HIGHSCORE);
            tempString=background.getLocation();
            tempString=tempString==""?"Patan Durbar":tempString;
            game.preferences.putString(Constants.PLACE,tempString);
        }
        game.preferences.putFloat(Constants.SCORE,score);
        game.preferences.putInteger(Constants.COIN,game.preferences.getInteger(Constants.COIN)+character.coinCount);
        game.preferences.flush();

    }

    private void removeDeadBodies(){
        Iterator<Body> iterator=Constants.deadBodies.iterator();
        while(iterator.hasNext()){
            Body b= iterator.next();
            game.world.destroyBody(b);
            iterator.remove();
        }
    }
}
