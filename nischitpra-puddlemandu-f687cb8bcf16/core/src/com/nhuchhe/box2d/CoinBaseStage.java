package com.nhuchhe.box2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by nischit on 31/8/17.
 */

public class CoinBaseStage extends Stage {
    private MyGame game;
    private Label.LabelStyle labelStyle;
    private Skin skin;




    public CoinBaseStage(final MyGame game){
        this.game=game;
        this.labelStyle=new Label.LabelStyle(game.font_big, Color.WHITE);
        this.skin=new Skin(Gdx.files.internal("glassy/skin/glassy-ui.json"));

        Label title=new Label("Store",labelStyle);
        title.setPosition(game.utils.getCenterPoint(0,Constants.CAMERA_WIDTH,title.getWidth()),Constants.CAMERA_HEIGHT*0.8f);
        this.addActor(title);

        Table coinTable=new Table();
        coinTable.setTransform(true);
        Image image=new Image(new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"99")));
        image.setSize(Constants.BUTTON_HEIGHT*3,Constants.BUTTON_HEIGHT*2);
        coinTable.add(image).width(Constants.BUTTON_HEIGHT*3).height(Constants.BUTTON_HEIGHT*2).expand();
        image=new Image(new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"499")));
        image.setSize(Constants.BUTTON_HEIGHT*3,Constants.BUTTON_HEIGHT*2);
        coinTable.add(image).width(Constants.BUTTON_HEIGHT*3).height(Constants.BUTTON_HEIGHT*2).expand();
        image=new Image(new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"2499")));
        image.setSize(Constants.BUTTON_HEIGHT*3,Constants.BUTTON_HEIGHT*2);
        coinTable.add(image).width(Constants.BUTTON_HEIGHT*3).height(Constants.BUTTON_HEIGHT*2).expand();
        image=new Image(new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"4999")));
        image.setSize(Constants.BUTTON_HEIGHT*3,Constants.BUTTON_HEIGHT*2);
        coinTable.add(image).width(Constants.BUTTON_HEIGHT*3).height(Constants.BUTTON_HEIGHT*2).expand();

        coinTable.row();

        TextButton button=new TextButton("$0.99",skin);
        button.setColor(75/255.0f,155/255.0f,255.0f/255.0f,1f);//blue
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.audioManager.play("button",false);
                return true;
            }
        });
        coinTable.add(button).expand();

        button=new TextButton("$1.99",skin);
        button.setColor(75/255.0f,155/255.0f,255.0f/255.0f,1f);//blue
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.audioManager.play("button",false);
                return true;
            }
        });
        coinTable.add(button).expand();

        button=new TextButton("$4.99",skin);
        button.setColor(75/255.0f,155/255.0f,255.0f/255.0f,1f);//blue
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.audioManager.play("button",false);
                return true;
            }
        });
        coinTable.add(button).expand();

        button=new TextButton("$9.99",skin);
        button.setColor(75/255.0f,155/255.0f,255.0f/255.0f,1f);//blue
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.audioManager.play("button",false);
                return true;
            }
        });
        coinTable.add(button).expand();

        coinTable.pack();
        coinTable.setSize(Constants.CAMERA_WIDTH*0.85f,coinTable.getHeight());
        coinTable.setPosition(game.utils.getCenterPoint(0,Constants.CAMERA_WIDTH,coinTable.getWidth()), Constants.CAMERA_HEIGHT*0.45f);
        this.addActor(coinTable);

        coinTable=new Table();
        coinTable.setTransform(true);
        image=new Image(new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"jump")));
        image.setSize(Constants.BUTTON_HEIGHT*3,Constants.BUTTON_HEIGHT*2);
        coinTable.add(image).width(Constants.BUTTON_HEIGHT*3).height(Constants.BUTTON_HEIGHT*2).expand();
        image=new Image(new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"video")));
        image.setSize(Constants.BUTTON_HEIGHT*3,Constants.BUTTON_HEIGHT*2);
        coinTable.add(image).width(Constants.BUTTON_HEIGHT*3).height(Constants.BUTTON_HEIGHT*2).expand();
        coinTable.row();

        button=new TextButton("$1.99",skin);
        button.setColor(75/255.0f,155/255.0f,255.0f/255.0f,1f);//blue
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.audioManager.play("button",false);
                return true;
            }
        });
        coinTable.add(button).expand();

        button=new TextButton("Watch AD",skin);
        button.setColor(75/255.0f,155/255.0f,255.0f/255.0f,1f);//blue
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.audioManager.play("button",false);
                return true;
            }
        });
        coinTable.add(button).expand();

        coinTable.pack();
        coinTable.setSize(Constants.CAMERA_WIDTH*0.5f,coinTable.getHeight());
        coinTable.setPosition(game.utils.getCenterPoint(0,Constants.CAMERA_WIDTH,coinTable.getWidth()),
              Constants.CAMERA_HEIGHT*0.085f);

        this.addActor(coinTable);


        ImageButton backButton=new ImageButton(new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"backButton")));
        backButton.getStyle().imageDown=new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"backButton"));
        backButton.setTransform(true);
        backButton.setSize(Constants.BUTTON_HEIGHT*2,Constants.BUTTON_HEIGHT*2);
        backButton.setPosition(game.utils.getCenterPoint(Constants.CAMERA_WIDTH*0.05f,Constants.CAMERA_WIDTH*0.1f,Constants.BUTTON_HEIGHT*2),
                game.utils.getCenterPoint(Constants.CAMERA_HEIGHT*0.85f,Constants.CAMERA_HEIGHT*0.9f,Constants.BUTTON_HEIGHT*2));
        backButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.audioManager.play("button",false);
                ((MainMenuScreen)game.getScreen()).prevStage=((MainMenuScreen)game.getScreen()).stageID;
                ((MainMenuScreen)game.getScreen()).stageID=0;
                ((MainMenuScreen)game.getScreen()).changeInputProcessor(0);
                return true;
            }

        });
        this.addActor(backButton);
    }

    public void render(){
        this.act(Gdx.graphics.getDeltaTime());

        this.draw();
    }
}
