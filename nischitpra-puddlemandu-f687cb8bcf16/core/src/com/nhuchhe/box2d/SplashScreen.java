package com.nhuchhe.box2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * Created by nischit on 6/7/17.
 */

public class SplashScreen implements Screen {
    private MyGame game;
    private OrthographicCamera camera;
    private int i;
    private final String lastResource;
    int count=0;
    private TextureRegion logo;

    public SplashScreen(MyGame game){
        this.game=game;

        camera=new OrthographicCamera();
        camera.setToOrtho(false,Constants.CAMERA_WIDTH,Constants.CAMERA_HEIGHT);
        System.out.println("inside splash screen");

        logo=game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"logo");

        Constants.resourceToLoad=new HashMap<Integer, Array<String>>();
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"textureAtlas","background.pack"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"textureAtlas","foreground.pack"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"textureAtlas","gameWorld.pack"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"textureAtlas","hud.pack"}));


        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"bodyLoader","player.json"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"bodyLoader","ground.json"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"bodyLoader","sprites.json"}));

        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","bang.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","bus.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","car.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","chicken_0.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","coin.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","cow.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","crash_0.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","crash_1.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","dog_0.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","dog_1.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","earRinging.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","highscore.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","jump.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","lightning_0.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","lightning_1.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","lightning_2.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","lightning_3.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","lightning_4.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","motorcycle_0.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","motorcycle_1.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","scream_0.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","scream_1.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","scream_2.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","scream_3.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","scream_girl.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","splash.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","zombie_0.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","zombie_1.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","button.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","select.mp3"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"sound","selectWrong.mp3"}));

        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"animation","motorcycle0","1","1"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"animation","motorcycle1","1","1"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"animation","sajhaBus","1","1"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"animation","beetleCar","1","1"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"animation","microbus","1","1"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"animation","tractor","1","1"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"animation","tampo","1","1"}));
//        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"animation","cow","19","1"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"animation","dog0","13","1"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"animation","dog1","11","1"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"animation","Buddy","16","1"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"animation","Alibubu","16","1"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"animation","Alibuba","16","1"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"animation","Stickman","16","1"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"animation","Lakhe","22","1"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"animation","Lakhe Full","25","1"}));
//        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"animation","Ghost","35","1"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"animation","Maichyang","20","1"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"animation","zombie","20","1"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"animation","Billy","15","1"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"animation","Pinokia","15","1"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"animation","chicken","13","1"}));
        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"animation","ostrich","15","1"}));

        Constants.resourceToLoad.put(count++,new Array<String>(new String[]{"music","background.mp3"}));



        this.lastResource= Constants.resourceToLoad.get(count-1).get(1);

        i=0;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        game.batch.draw(logo,
                game.utils.getCenterPoint(0,Constants.CAMERA_WIDTH,logo.getRegionWidth()),
                game.utils.getCenterPoint(0,Constants.CAMERA_HEIGHT,logo.getRegionHeight()));
        game.batch.end();

        if(game.resource.isMusicLoaded(lastResource)){
            game.audioManager.configureAudioSystem();
            game.setScreen(new MainMenuScreen(game));
            //dispose the current splashscreen after rendering the Mainmenu Screen
            dispose();
        }
        if (game.resource.isTextureRegionLoaded("logo")){
            if(Constants.resourceToLoad.get(i).get(0)=="bodyLoader"){
                if(game.resource.isBodyLoaderLoaded(Constants.resourceToLoad.get(i).get(1))){
                    i++;
                }else{
                    game.resource.getBodyEditorLoader(Constants.resourceToLoad.get(i).get(1));
                }
            }else if(Constants.resourceToLoad.get(i).get(0)=="sound"){
                if(game.resource.isSoundLoaded(Constants.resourceToLoad.get(i).get(1))){
                    i++;
                }else{
                    game.resource.getSound(Constants.resourceToLoad.get(i).get(1));
                }
            }else if(Constants.resourceToLoad.get(i).get(0)=="animation"){
                if(game.resource.isAnimationLoaded(Constants.resourceToLoad.get(i).get(1))){
                    i++;
                }else{
                    game.resource.getAnimationSequence(Constants.resourceToLoad.get(i).get(1),
                            Integer.parseInt(Constants.resourceToLoad.get(i).get(2)),
                            Integer.parseInt(Constants.resourceToLoad.get(i).get(3)));
                }
            }else if(Constants.resourceToLoad.get(i).get(0)=="music"){
                if(game.resource.isMusicLoaded(Constants.resourceToLoad.get(i).get(1))){
                    i++;
                }else{
                    game.resource.getMusic(Constants.resourceToLoad.get(i).get(1));
                }
            }else if(Constants.resourceToLoad.get(i).get(0)=="textureAtlas"){
                if(game.resource.isTextureAtlasLoaded(Constants.resourceToLoad.get(i).get(1))){
                    i++;
                }else{
                    game.resource.getTextureAtlas(Constants.resourceToLoad.get(i).get(1));
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {


    }
}
