package com.nhuchhe.box2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;


/**
 * Created by nischit on 4/7/17.
 */

public class HUD extends Stage {
    private MyGame game;
    private Label.LabelStyle labelStyle;
    private GameScreen screen;
    private GlyphLayout layout=new GlyphLayout();
    public  ImageButton  pause;
    private String tempString;
    private String prevLocation;
    private long locationShowTime;
//    private float locationWidth;
    private Label location;

    /**
     * todo
     * create an actor label called location
     * add action to it which allows it to fade in and fade out by changing the alpha value with AlphaAction action =new AlphaAction()
     * text can be changed dynamically in render method .setText("location text here");
     */

    HUD(final MyGame game, final GameScreen screen){
        this.game=game;
        this.screen=screen;
        this.labelStyle=new Label.LabelStyle(game.font_big, Color.WHITE);

        pause=new ImageButton(new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"pause")),
                new TextureRegionDrawable(new TextureRegion(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"resume"))),
                new TextureRegionDrawable(new TextureRegion(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"resume"))));
        pause.setSize(Constants.BUTTON_HEIGHT,Constants.BUTTON_HEIGHT);
        pause.setPosition(Constants.CAMERA_WIDTH*0.93f, Constants.CAMERA_HEIGHT*0.86f);
        pause.setChecked(false);
        pause.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.audioManager.play("button",false);
                if(screen.status!= GameScreen.Status.Pause){
                    screen.status= GameScreen.Status.Pause;
                    pause.setSize(Constants.BUTTON_HEIGHT*2,Constants.BUTTON_HEIGHT*2);
                    pause.setPosition(game.utils.getCenterPoint(0,Constants.CAMERA_WIDTH,pause.getWidth()),
                            game.utils.getCenterPoint(0,Constants.CAMERA_HEIGHT,pause.getHeight()));
                    pause.setChecked(true);
                }else{
                    screen.status= GameScreen.Status.Resume;
                    pause.setSize(Constants.BUTTON_HEIGHT,Constants.BUTTON_HEIGHT);
                    pause.setPosition(Constants.CAMERA_WIDTH*0.93f, Constants.CAMERA_HEIGHT*0.86f);
                    pause.setChecked(false);
                }
                return true;
            }
        });
        this.addActor(pause);

        location=new Label("",labelStyle);
        this.addActor(location);
    }

    public void render(Character character) {
        this.act(Gdx.graphics.getDeltaTime());

        game.font_medium.draw(game.batch, (int) character.getPosition().x + "", Constants.CAMERA_WIDTH * 0.025f, Constants.CAMERA_HEIGHT * 0.95f);

        layout.setText(game.font_small, "" + character.coinCount);
        game.font_small.draw(game.batch, "" + character.coinCount, Constants.CAMERA_WIDTH * 0.025f, Constants.CAMERA_HEIGHT * 0.875f);
        game.batch.draw(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD, "coin"), Constants.CAMERA_WIDTH * 0.025f + 16 + layout.width, Constants.CAMERA_HEIGHT * 0.825f, Constants.BUTTON_HEIGHT * 0.6f, Constants.BUTTON_HEIGHT * 0.6f);

        tempString = screen.background.getLocation();
        tempString = tempString == "" ? "Patan Durbar" : tempString;
        // to show location when new location is reached
        if (prevLocation != tempString) {
            prevLocation = tempString;
            location.setText(tempString);
            location.pack();
            locationShowTime = TimeUtils.nanoTime();
            location.setPosition(game.utils.getCenterPoint(0, Constants.CAMERA_WIDTH, location.getWidth()), Constants.CAMERA_HEIGHT * 0.85f);
        }
        if (TimeUtils.nanoTime() - locationShowTime < Constants.NANOSECOND * 3) {
            game.utils.fadeInAnimation(location,0.01f);
        } else {
            game.utils.fadeOutAnimation(location,0.01f);
        }
        game.batch.end();
        game.batch.begin();
        //remove this
        game.font_small.draw(game.batch, Gdx.graphics.getFramesPerSecond() + "", Constants.CAMERA_WIDTH * 0.75f, Constants.CAMERA_HEIGHT * 0.95f);
        game.batch.end();
        game.batch.begin();

        this.draw();
    }

}
