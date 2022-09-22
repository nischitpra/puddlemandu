package com.nhuchhe.box2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;


/**
 * Created by nischit on 8/8/17.
 */

public class MainMenuStage extends Stage {
    private MyGame game;
    private ImageButton start, settings, rate, characterSelect;
//    private Skin skin;
    private Label.LabelStyle labelStyle;
    private Label facts;
    public boolean startGame=false;

    private Table table;
    private GlyphLayout layout;

    private float padding =32;
    private Array<String> factList;
    private long lastFact;
    public int factIndex;


    public MainMenuStage(final MyGame game){
        this.game=game;
//        this.skin=new Skin(Gdx.files.internal("glassy/skin/glassy-ui.json"));
        this.labelStyle=new Label.LabelStyle(game.font_big,Color.WHITE);

        this.layout=new GlyphLayout();

        factList=new Array<String>();
        factList.add("Mount Everest is called Sagarmatha in Nepali and Chomolungma locally");
        factList.add("8 out of 10 highest mountains are found in Nepal");
        factList.add("Nepal is the land to the living goddess Kumari");
        factList.add("Pashupatinath alone contains 492 temples");
        factList.add("Nepal was the birth place of Lord Gautam Buddha");
        factList.add("Nepal is the only country with a triangular flag");
        factList.add("Swayambhunath is nicknamed The Monkey Temple");
        factList.add("Kathmandu Valley used to be a huge lake surrounded with the Himalayas");
        factList.add("Kathmandu got its name from the temple Kaasthamandap built entirely with one tree");
        factList.add("Namaste is the way to greet in Nepal");
        factList.add("Freak Street was where the hipsters rolled");
        factList.add("Kathmandu Valley is surrounded four major mountains - Shivapuri, Phulchoki, Nagarjun and Chandagiri");
        factList.add("Dharahara was the tallest building in Nepal");

        factIndex=game.preferences.getInteger(Constants.FACT_INDEX);

        table =new Table();


        Label title=new Label("Puddle-Mandu",labelStyle);
        title.setSize(title.getWidth(),title.getHeight());
        title.setColor(Color.WHITE);
        title.setPosition(game.utils.getCenterPoint(0,Constants.CAMERA_WIDTH,title.getWidth()), Constants.CAMERA_HEIGHT*0.65f);
        this.addActor(title);

        labelStyle.font=game.font_small;
        title=new Label((int)game.preferences.getFloat(Constants.HIGHSCORE)+" "+game.preferences.getString(Constants.PLACE),labelStyle);
        title.setSize(title.getWidth(),title.getHeight());
        title.setColor(Color.WHITE);
        title.setPosition(game.utils.getCenterPoint(Constants.CAMERA_WIDTH*0.0f,Constants.CAMERA_WIDTH*1f,title.getWidth()),
                Constants.CAMERA_HEIGHT*0.575f);
        this.addActor(title);

        int score=(int)game.preferences.getFloat(Constants.SCORE);
        title=new Label(score==0?"":score+"m",labelStyle);
        title.setSize(title.getWidth(),title.getHeight());
        title.setColor(Color.WHITE);
        title.setPosition(game.utils.getCenterPoint(Constants.CAMERA_WIDTH*0.0f,Constants.CAMERA_WIDTH*1f,layout.width),
                Constants.CAMERA_HEIGHT*0.5f);
        this.addActor(title);


        labelStyle=new Label.LabelStyle(game.font_small,Color.WHITE);

        ScrollPane scrollPane=new ScrollPane(table);
        scrollPane.setFlickScroll(true);
        scrollPane.setSize(Constants.CAMERA_WIDTH*0.95f,Constants.CAMERA_HEIGHT*0.4f);
        scrollPane.setPosition(game.utils.getCenterPoint(Constants.CAMERA_WIDTH*0.125f,Constants.CAMERA_WIDTH*(1-0.125f),scrollPane.getWidth()),
                game.utils.getCenterPoint(Constants.CAMERA_HEIGHT*0.40f,Constants.CAMERA_HEIGHT*0.60f,scrollPane.getHeight()));
        this.addActor(scrollPane);

        Table menuTable=new Table();

        rate =new ImageButton(new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"rateUp")));
        rate.getStyle().imageDown=new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"rateDown"));
        rate.setTransform(true);
        rate.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("button press");

                game.audioManager.play("button",false);
                return true;
            }

        });
        menuTable.add(rate).width(Constants.BUTTON_HEIGHT*2).height(Constants.BUTTON_HEIGHT*2).pad(0,padding,0,padding);


        characterSelect=new ImageButton(new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"characterUp")));
        characterSelect.getStyle().imageDown=new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"characterDown"));
        characterSelect.setTransform(true);
        characterSelect.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.audioManager.play("button",false);
                ((MainMenuScreen)game.getScreen()).prevStage=((MainMenuScreen)game.getScreen()).stageID;
                ((MainMenuScreen)game.getScreen()).stageID=1;
                ((MainMenuScreen)game.getScreen()).changeInputProcessor(1);
                return true;
            }

        });
        menuTable.add(characterSelect).width(Constants.BUTTON_HEIGHT*2).height(Constants.BUTTON_HEIGHT*2).pad(0,padding,0,padding);


        start =new ImageButton(new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"playUp")));
        start.getStyle().imageDown=new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"playDown"));
        start.setTransform(true);
        start.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                startGame=true;
                game.audioManager.play("button",false);
                ((MainMenuScreen)game.getScreen()).clearInputProcessor();
                return true;
            }
        });
        menuTable.add(start).width(Constants.BUTTON_HEIGHT*2).height(Constants.BUTTON_HEIGHT*2).pad(0,padding,0,padding);

        settings =new ImageButton(new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"settingsUp")));
        settings.getStyle().imageDown=new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"settingsDown"));
        settings.setTransform(true);

        settings.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ((MainMenuScreen)game.getScreen()).prevStage=((MainMenuScreen)game.getScreen()).stageID;
                ((MainMenuScreen)game.getScreen()).stageID=3;
                ((MainMenuScreen)game.getScreen()).changeInputProcessor(3);
                return true;
            }
        });
        menuTable.add(settings).width(Constants.BUTTON_HEIGHT*2).height(Constants.BUTTON_HEIGHT*2).pad(0,padding,0,padding);
        menuTable.setPosition(game.utils.getCenterPoint(0,Constants.CAMERA_WIDTH,menuTable.getWidth()),
                game.utils.getCenterPoint(0,Constants.CAMERA_HEIGHT*0.60f,menuTable.getHeight()));
        this.addActor(menuTable);

        labelStyle.font=game.font_verySmall;
        facts=new Label("",labelStyle);
        facts.setWrap(true);
        facts.setSize(Constants.CAMERA_WIDTH*0.95f,Constants.CAMERA_HEIGHT*0.195f);
        facts.setPosition(game.utils.getCenterPoint(0,Constants.CAMERA_WIDTH,facts.getWidth()),0);
        facts.setAlignment(Align.center);
        this.addActor(facts);
    }


    private void renderFact(){
        if(TimeUtils.nanoTime()-lastFact>Constants.NANOSECOND*Constants.FACT_SPEED){
            lastFact=TimeUtils.nanoTime();
            facts.addAction(Actions.sequence(Actions.alpha(1),Actions.fadeOut(Constants.ANIMATION_DURATION*6),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            facts.setText(factList.get(factIndex>=factList.size?0:factIndex++));
                        }
                    }),Actions.fadeIn(Constants.ANIMATION_DURATION*6)));
        }
    }

    public void render(){
        this.act(Gdx.graphics.getDeltaTime());
        renderFact();
        this.draw();
    }





    public void resize (int width, int height) {
        this.getViewport().update(width, height, true);
    }

    public void dispose () {

    }
}
