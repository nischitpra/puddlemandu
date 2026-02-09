package com.nhuchhe.box2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

/**
 * Created by nischit on 6/7/17.
 */

public class MainMenuScreen implements Screen {
    private MyGame game;
    private OrthographicCamera camera;
    private CharacterPickerStage characterPickerStage;
    private MainMenuStage mainMenuStage;
    // Shop removed (coin click no longer opens CoinBaseStage).
    // private CoinBaseStage coinBaseStage;
    private SettingsStage settingsStage;
    private Stage commonStage;

    private ParticleSystem particleSystem,atmosphere;
    private float scale,width;
    private TextureRegion t;

    private Array<String> tempArrayString;
    private InputMultiplexer inputMultiplexer;

    private GlyphLayout layout;
    private ImageButton coin;


    public int stageID =0;
    public int prevStage=-1;

    private Stage splashScreen;
    private Table coinTable;
    private Label coinCount;
    private Label.LabelStyle labelStyle;

    public MainMenuScreen(final MyGame game){
        this.game=game;
        this.characterPickerStage =new CharacterPickerStage(game);
        this.mainMenuStage=new MainMenuStage(game);
        this.settingsStage=new SettingsStage(game);
        this.commonStage=new Stage();
        this.inputMultiplexer=new InputMultiplexer();

        splashScreen=new Stage();
        Image image=new Image(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_GAMEWORLD,"groundExtender"));
        image.setSize(Constants.CAMERA_WIDTH,Constants.CAMERA_HEIGHT);
        splashScreen.addActor(image);
        image=new Image(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"logo"));
        image.setPosition(game.utils.getCenterPoint(0,Constants.CAMERA_WIDTH,image.getWidth()),
                game.utils.getCenterPoint(0,Constants.CAMERA_HEIGHT,image.getHeight()));
        image.setSize(image.getWidth(),image.getHeight());
        splashScreen.addActor(image);


        changeInputProcessor(0);

        Constants.reset();
        layout=new GlyphLayout();

        camera=new OrthographicCamera();
        camera.setToOrtho(false,Constants.CAMERA_WIDTH,Constants.CAMERA_HEIGHT);
        System.out.println("inside mainmenu screen");


        game.audioManager.play("background",false);

        tempArrayString=new Array<String>();
        tempArrayString.add("particle0");
        tempArrayString.add("particle1");
        tempArrayString.add("particle2");
        tempArrayString.add("particle3");
        tempArrayString.add("particle4");
        tempArrayString.add("particle5");
        tempArrayString.add("particle6");
        tempArrayString.add("particle7");
        particleSystem=new ParticleSystem(game,new Vector2(0.3f,1),5,tempArrayString, MathUtils.random(7,11),1);

        tempArrayString.clear();
        tempArrayString.add("dust1");
        tempArrayString.add("dust2");
        tempArrayString.add("dust4");
        tempArrayString.add("dust5");
        atmosphere=new ParticleSystem(game,new Vector2(0.8f,1f),4,tempArrayString, MathUtils.random(0.5f,1),2);


        coinTable=new Table();
        labelStyle=new Label.LabelStyle(game.font_small, Color.WHITE);
        coinCount=new Label(game.preferences.getInteger(Constants.COIN)+"",labelStyle);
        coinTable.add(coinCount).pad(0,8,0,8);

        coin=new ImageButton(new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"coin")));
        coin.setSize(Constants.BUTTON_HEIGHT*1,Constants.BUTTON_HEIGHT*1);

        coinTable.add(coin).pad(0,8,0,8);
        coinTable.pack();
        coinTable.setSize(coinTable.getWidth()*1.3f,coinTable.getHeight()*1.5f);
        coinTable.setPosition(Constants.CAMERA_WIDTH*0.975f-coinTable.getWidth(),Constants.CAMERA_HEIGHT*0.95f-coinTable.getHeight());
        coinTable.setBackground(new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"popUpBg")));
        // No click handler: coin display is informational only.
        commonStage.addActor(coinTable);
        characterPickerStage.addAction(Actions.alpha(0));
        settingsStage.addAction(Actions.alpha(0));

        splashScreen.addAction(Actions.sequence(Actions.alpha(1),Actions.fadeOut(Constants.ANIMATION_DURATION*2)));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        particleSystem.update();
        atmosphere.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        //background
        scale=4.6f;
        t=game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_BACKGROUND,"sky");
        // Tile + align to top so ultra-wide devices are covered without cropping.
        drawTiledX(t, Constants.CAMERA_HEIGHT - t.getRegionHeight() * scale, scale);
        scale=2f;
        t=game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_BACKGROUND,"mountain");
        drawTiledX(t, Constants.CAMERA_HEIGHT * 0.4f, scale);
        scale=1f;
        t=game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_BACKGROUND,"hill");
        drawTiledX(t, Constants.CAMERA_HEIGHT * 0.3f, scale);
        scale=1.1f;
        t=game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_BACKGROUND,"jungle");
        drawTiledX(t, Constants.CAMERA_HEIGHT * 0.09f, scale);


        //highscore landmark
        if(game.preferences.getString(Constants.PLACE).equals("")){
            t=game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_BACKGROUND,"Patan Durbar");
        }else{
            t=game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_BACKGROUND,game.preferences.getString(Constants.PLACE)+"");
        }
        scale=1.0f;
        if(t.getRegionHeight()+Constants.CAMERA_HEIGHT*0.16f>Constants.CAMERA_HEIGHT){
            scale=game.utils.getScale(Constants.CAMERA_HEIGHT*0.82f,t.getRegionHeight());
        }
        game.batch.draw(t,game.utils.getCenterPoint(0,Constants.CAMERA_WIDTH*0.5f,t.getRegionWidth()*scale),
                Constants.CAMERA_HEIGHT*0.16f,
                t.getRegionWidth()*scale,t.getRegionHeight()*scale);

        width=0;
        t=game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_GAMEWORLD,"ground0");
        game.batch.draw(t,0,Constants.CAMERA_HEIGHT*0.05f);
        while(width<=Constants.CAMERA_WIDTH){
            game.batch.draw(t,width,Constants.CAMERA_HEIGHT*0.05f);
            width+=t.getRegionWidth();
        }
        //to cover the ground gap
        game.batch.draw(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_GAMEWORLD,"groundExtender"),0,0,Constants.CAMERA_WIDTH,Constants.CAMERA_HEIGHT*0.05f);

        game.batch.end();
        game.batch.begin();

        particleSystem.render();
        atmosphere.render();

        //foreground
        game.batch.draw(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_FOREGROUND,"foregroundTree1"),Constants.CAMERA_WIDTH*0.4f,0);
        t=game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_FOREGROUND,"foregroundGrass0");
        game.batch.draw(t,0,0);
        while(width<=Constants.CAMERA_WIDTH){
            game.batch.draw(t,width,0);
            width+=t.getRegionWidth();
        }
        coinCount.setText(game.preferences.getInteger(Constants.COIN)+"");

        game.batch.end();
        game.batch.begin();

        renderStage();

        game.batch.end();

        if(mainMenuStage.startGame){
            game.setScreen(new HelpScreen(game));
            mainMenuStage.dispose();
            dispose();
        }
    }
    public void changeInputProcessor(int opt){
        inputMultiplexer.clear();
        inputMultiplexer.addProcessor(commonStage);

        switch (opt){
            case 0:
                inputMultiplexer.addProcessor(mainMenuStage);
                break;
            case 1:
                inputMultiplexer.addProcessor(characterPickerStage);
                break;
            case 3:
                inputMultiplexer.addProcessor(settingsStage);
                break;
        }
        Gdx.input.setInputProcessor(inputMultiplexer);
    }
    public void clearInputProcessor(){
        inputMultiplexer.clear();
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void renderStage(){
        if(prevStage!=stageID){
            switch (prevStage){
                case 0:
                    mainMenuStage.addAction(Actions.sequence(Actions.alpha(1),Actions.fadeOut(1)));
                    break;
                case 1:
                    characterPickerStage.addAction(Actions.sequence(Actions.alpha(1),Actions.fadeOut(1)));
                    break;
                case 3:
                    settingsStage.addAction(Actions.sequence(Actions.alpha(1),Actions.fadeOut(1)));
                    break;
                default:
                    break;
            }
            switch (stageID){
                case 0:
                    mainMenuStage.addAction(Actions.sequence(Actions.alpha(0),Actions.fadeIn(Constants.ANIMATION_DURATION)));
                    break;
                case 1:
                    characterPickerStage.addAction(Actions.sequence(Actions.alpha(0),Actions.fadeIn(Constants.ANIMATION_DURATION)));
                    break;
                case 3:
                    settingsStage.addAction(Actions.sequence(Actions.alpha(0),Actions.fadeIn(Constants.ANIMATION_DURATION)));
                    break;

            }
        }
        splashScreen.act();
        splashScreen.draw();

        mainMenuStage.render();
        characterPickerStage.render();
        settingsStage.render();

        commonStage.act();
        commonStage.draw();
        prevStage=stageID;
    }

    @Override
    public void resize(int width, int height) {
    }

    private void drawTiledX(TextureRegion region, float y, float scale) {
        float w = region.getRegionWidth() * scale;
        float h = region.getRegionHeight() * scale;
        // draw 1 extra tile to avoid seams on the far right due to float rounding.
        for (float x = 0; x <= Constants.CAMERA_WIDTH + w; x += w) {
            game.batch.draw(region, x, y, w, h);
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void pause() {
        game.preferences.putInteger(Constants.FACT_INDEX,mainMenuStage.factIndex);
        game.preferences.flush();
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
