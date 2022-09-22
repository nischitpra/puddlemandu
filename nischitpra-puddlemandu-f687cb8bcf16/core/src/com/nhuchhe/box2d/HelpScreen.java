package com.nhuchhe.box2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by nischit on 29/7/17.
 */

public class HelpScreen implements Screen {
    private MyGame game;
    private OrthographicCamera camera;
    private GlyphLayout layout;
    private Stage splashScreen;

    public HelpScreen(MyGame game){
        this.game=game;

        splashScreen=new Stage();
        Image image=new Image(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_GAMEWORLD,"groundExtender"));
        image.setSize(Constants.CAMERA_WIDTH,Constants.CAMERA_HEIGHT);
        splashScreen.addActor(image);
        image=new Image(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"logo"));
        image.setPosition(game.utils.getCenterPoint(0,Constants.CAMERA_WIDTH,image.getWidth()),
                game.utils.getCenterPoint(0,Constants.CAMERA_HEIGHT,image.getHeight()));
        splashScreen.addActor(image);

        camera=new OrthographicCamera();
        camera.setToOrtho(false,Constants.CAMERA_WIDTH,Constants.CAMERA_HEIGHT);
        System.out.println("inside help screen");
        layout=new GlyphLayout();

        splashScreen.addAction(Actions.sequence(Actions.alpha(1),Actions.fadeOut(Constants.ANIMATION_DURATION/2)));
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        Sprite s;
        s=new Sprite(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"help_backArrow"));
        s.setPosition(Constants.CAMERA_WIDTH*0.25f-s.getWidth(),game.utils.getCenterPoint(0,Constants.CAMERA_HEIGHT,s.getHeight()));
        s.draw(game.batch);
        s=new Sprite(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"help_character"));
        s.flip(true,false);
        s.setPosition(Constants.CAMERA_WIDTH*0.25f,game.utils.getCenterPoint(0,Constants.CAMERA_HEIGHT,s.getHeight()));
        s.draw(game.batch);
        layout.setText(game.font_small,"Run Back");
        game.font_small.draw(game.batch,"Run Back",
                game.utils.getCenterPoint(0,Constants.CAMERA_WIDTH*0.5f,layout.width),
                Constants.CAMERA_HEIGHT*0.35f);

        // screen divider
        s=new Sprite(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"help_line"));
        s.setSize(Constants.BUTTON_HEIGHT*0.1f,Constants.CAMERA_HEIGHT*0.5f);
        s.setPosition(game.utils.getCenterPoint(0,Constants.CAMERA_WIDTH,s.getWidth()),
                game.utils.getCenterPoint(0,Constants.CAMERA_HEIGHT,s.getHeight()));
        s.draw(game.batch);


        s=new Sprite(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"help_jump"));
        s.setPosition(game.utils.getCenterPoint(Constants.CAMERA_WIDTH*0.5f,Constants.CAMERA_WIDTH,s.getWidth()),game.utils.getCenterPoint(0,Constants.CAMERA_HEIGHT,s.getHeight()));
        s.draw(game.batch);
        s=new Sprite(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"help_character"));
        s.setPosition(Constants.CAMERA_WIDTH*0.65f,Constants.CAMERA_HEIGHT*0.2f);
        s.draw(game.batch);
        s=new Sprite(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"help_character"));
        s.setPosition(Constants.CAMERA_WIDTH*0.83f,Constants.CAMERA_HEIGHT*0.58f);
        s.draw(game.batch);
        layout.setText(game.font_small,"Jump");
        game.font_small.draw(game.batch,"Jump",
                Constants.CAMERA_WIDTH*0.75f,
                Constants.CAMERA_HEIGHT*0.35f);

        layout.setText(game.font_small,"Tap anywhere to begin!");
        game.font_small.draw(game.batch,"Tap anywhere to begin!",game.utils.getCenterPoint(0,Constants.CAMERA_WIDTH,layout.width),Constants.CAMERA_HEIGHT*0.1f);

        game.batch.end();

        if(Gdx.input.justTouched()){
            game.audioManager.play("button",false);
            game.setScreen(new GameScreen(game));
            //dispose the current HelpScreen after rendering the GameScreen
            dispose();
            System.gc();
            System.out.println("Garbage collector called");
        }
    }


    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

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
