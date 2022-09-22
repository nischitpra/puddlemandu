package com.nhuchhe.box2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by nischit on 12/9/17.
 */

public class SettingsStage extends Stage {
    private MyGame game;
    private Label.LabelStyle labelStyle;
    private Skin skin;

    public SettingsStage(final MyGame game){
        this.game=game;
        labelStyle=new Label.LabelStyle(game.font_small, Color.WHITE);
        this.skin=new Skin(Gdx.files.internal("glassy/skin/glassy-ui.json"));

        final Table t=new Table();
        Label title=new Label("Settings",labelStyle);
        t.add(title).pad(8,0,8,0);
        t.row();

        final CheckBox musicCB=new CheckBox(game,skin,labelStyle,"Music",Constants.MUSIC,20);
        t.add(musicCB).pad(8,0,8,0);
        t.row();

        TextButton b=new TextButton("Done",skin);
        b.setSize(Constants.BUTTON_HEIGHT*10,Constants.BUTTON_HEIGHT*10);
        b.setColor(75/255.0f,155/255.0f,255.0f/255.0f,1f);//blue
        b.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.audioManager.play("button",false);
                game.preferences.putBoolean(Constants.MUSIC,musicCB.isChecked);
                game.preferences.flush();

                game.audioManager.updateSettings();

                ((MainMenuScreen)game.getScreen()).prevStage=((MainMenuScreen)game.getScreen()).stageID;
                ((MainMenuScreen)game.getScreen()).stageID=0;
                ((MainMenuScreen)game.getScreen()).changeInputProcessor(0);
                for(Actor actor:getActors()){
                    actor.setTouchable(Touchable.enabled);
                }
                return true;
            }
        });
        t.add(b).pad(8,0,8,0);
        t.row();

        addActor(t);

        t.setSize(t.getPrefWidth()+Constants.CAMERA_WIDTH*0.1f,t.getPrefHeight()+Constants.CAMERA_HEIGHT*0.1f);
//        t.background(new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"popUpBg")));
        t.setPosition(game.utils.getCenterPoint(0,Constants.CAMERA_WIDTH,t.getWidth()),
                game.utils.getCenterPoint(0,Constants.CAMERA_HEIGHT,t.getHeight()));


    }
    public void render(){
        this.act(Gdx.graphics.getDeltaTime());
        this.draw();
    }
}
