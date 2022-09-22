package com.nhuchhe.box2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by nischit on 13/8/17.
 */

public class CheckBox extends Actor {
    private MyGame game;
    private Label.LabelStyle labelStyle;
    private Skin skin;
    private Label label;
    private Image image;
    private float padding;
    public boolean isChecked;

    public CheckBox (final MyGame game, Skin skin, Label.LabelStyle labelStyle, String label,String constant, float padding){
        this.game=game;
        this.labelStyle=labelStyle;
        this.skin=skin;
        this.padding=padding;

        this.label=new Label(label,labelStyle);
        this.label.setSize(this.label.getWidth(),this.label.getHeight());
        isChecked=game.preferences.getBoolean(constant);

        image =new Image(new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,isChecked?"checked":"unchecked")));
        image.setSize(Constants.BUTTON_HEIGHT*2,Constants.BUTTON_HEIGHT*2);

        this.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isChecked=!isChecked;

                if(isChecked){
                    image.setDrawable(new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"checked")));
                }else{
                    image.setDrawable(new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"unchecked")));
                }

                return true;
            }
        });
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        image.setBounds(getParent().getX()+getX()+label.getWidth()+padding,game.utils.getCenterPoint(getParent().getY()+ getY(),getParent().getY()+ getY()+padding+image.getHeight(),image.getHeight()), image.getPrefWidth(), image.getPrefHeight());
        label.setBounds(getParent().getX()+getX(),game.utils.getCenterPoint(getParent().getY()+ getY(),getParent().getY()+ getY()+padding+image.getHeight(),label.getHeight()), label.getPrefWidth(), label.getPrefHeight());
    }

    @Override
    public float getWidth() {
        return label.getPrefWidth() + image.getPrefWidth()+padding;
    }

    @Override
    public float getHeight() {
        return image.getPrefHeight()+padding;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        label.draw(batch, parentAlpha);
        image.draw(batch,parentAlpha);
    }

}
