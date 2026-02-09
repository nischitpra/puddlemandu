package com.nhuchhe.box2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

/**
 * Created by nischit on 25/8/17.
 */

public class CharacterPickerStage extends Stage {
    private MyGame game;


    private Skin skin;

    private Array<Array<String>> textureList;
    private Array<Animation<TextureRegion>> walkAnimationList;

    private final float VELOCITY = Constants.CAMERA_WIDTH*1.2f;
    private float velocity =-VELOCITY;
    private float acceleration;

    private long stateTime=0;
    private TextButton selectButton;

    public int selectedIndex =0;
    final Slider slider;
    public Table mainTable;


    public CharacterPickerStage(final MyGame game){
        this.game=game;
        this.skin=new Skin(Gdx.files.internal("glassy/skin/glassy-ui.json"));


        textureList=new Array<Array<String>>();
        textureList.add(new Array<String>(new String[]{"Pinokia","false","40"}));
        textureList.add(new Array<String>(new String[]{"Alibuba","false","0"}));
        textureList.add(new Array<String>(new String[]{"Buddy","false","2"}));
        textureList.add(new Array<String>(new String[]{"Maichyang","false","2"}));
        textureList.add(new Array<String>(new String[]{"Stickman","false","5"}));
        textureList.add(new Array<String>(new String[]{"Alibubu","false","5"}));
        textureList.add(new Array<String>(new String[]{"Lakhe","false","10"}));
        textureList.add(new Array<String>(new String[]{"Lakhe Full","false","10"}));
        textureList.add(new Array<String>(new String[]{"Billy","false","10"}));
//        textureList.add(new Array<String>(new String[]{"Ghost","false","1000"}));
        textureList.add(new Array<String>(new String[]{"zombie","true","20"}));
        textureList.add(new Array<String>(new String[]{"dog0","true","20"}));
        textureList.add(new Array<String>(new String[]{"dog1","true","20"}));
//        textureList.add(new Array<String>(new String[]{"cow","true","2000"}));
        textureList.add(new Array<String>(new String[]{"ostrich","true","30"}));
        textureList.add(new Array<String>(new String[]{"chicken","true","30"}));

        walkAnimationList=new Array<Animation<TextureRegion>>();
        for(int i=0;i<textureList.size;i++){
            walkAnimationList.add(game.resource.getAnimationSequence(textureList.get(i).get(0)));
        }

        slider=new Slider(0,textureList.size-1,1,false,skin);
        slider.setSize(Constants.CAMERA_WIDTH*0.5f,Constants.CAMERA_HEIGHT*0.1f);
        slider.getStyle().knob.setMinHeight(Constants.CAMERA_HEIGHT*0.05f);
        slider.getStyle().knob.setMinWidth(Constants.CAMERA_HEIGHT*0.05f);
        slider.setColor(251/255.0f,207/255.0f,0,1f);//yellow
        slider.setColor(75/255.0f,155/255.0f,255.0f/255.0f,1f);//blue
        slider.setPosition(game.utils.getCenterPoint(0,Constants.CAMERA_WIDTH,slider.getWidth()),
                Constants.CAMERA_HEIGHT*0.3f);
        slider.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float prevIndex=selectedIndex;
                selectedIndex= (int) slider.getValue();

                Cell cell=mainTable.getCells().get(selectedIndex);
                Table t= (Table) cell.getActor();
                Vector2 position = new Vector2(mainTable.getX()+t.getX(),mainTable.getY()+t.getY());

                if(position.x<Constants.CAMERA_WIDTH/2){
                    velocity =-VELOCITY;
                }else{
                    velocity =VELOCITY;
                }
                if(Math.abs(prevIndex-selectedIndex)>4){
                    velocity*=3;
                }
                selectButton.setText(textureList.get(selectedIndex).get(2)+"\n"+textureList.get(selectedIndex).get(0));
            }
        });
        this.addActor(slider);


        mainTable=new Table();
        Sprite s;
        for(int i=0;i<textureList.size;i++){
            s=new Sprite(walkAnimationList.get(i).getKeyFrame(stateTime,true));

            if(Boolean.parseBoolean(textureList.get(i).get(1))){
                s.flip(true,false);
            }
            final Image image=new Image(new TextureRegionDrawable(new TextureRegion(s)));
            image.setSize(image.getWidth()*0.125f,image.getHeight()*0.125f);
            final int finalI = i;

            Table container=new Table();
            container.setTouchable(Touchable.enabled);
            container.addListener(new InputListener(){

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    selectedIndex = finalI;
                    slider.setValue(selectedIndex);
                    return true;
                }
            });
            container.add(image).align(Align.bottomLeft).expand();
            mainTable.add(container).width(image.getWidth()*20f).height(image.getHeight()*20f).align(Align.bottom);
        }
        mainTable.pack();
        mainTable.setPosition(Constants.CAMERA_WIDTH * 0.125f,Constants.CAMERA_HEIGHT * 0.4f);
        this.addActor(mainTable);

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
        addActor(backButton);

        ImageButton leftButton=new ImageButton(new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"backUp")));
        leftButton.getStyle().imageDown=new TextureRegionDrawable(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"backDown"));
        leftButton.setTransform(true);
        leftButton.setSize(Constants.BUTTON_HEIGHT*2,Constants.BUTTON_HEIGHT*2);
        leftButton.setPosition(game.utils.getCenterPoint(Constants.CAMERA_WIDTH*0.05f,Constants.CAMERA_WIDTH*0.1f,Constants.BUTTON_HEIGHT*2),
                game.utils.getCenterPoint(Constants.CAMERA_HEIGHT*0.4f,Constants.CAMERA_HEIGHT*0.6f,Constants.BUTTON_HEIGHT*2));
        leftButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.audioManager.play("button",false);
                slider.setValue(slider.getValue()-1);
                return true;
            }

        });
        addActor(leftButton);
        TextureRegion tr= new TextureRegion(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"backUp"));
        tr.flip(true,false);
        ImageButton rightButton=new ImageButton(new TextureRegionDrawable(tr));
        tr=new TextureRegion(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_HUD,"backDown"));
        tr.flip(true,false);
        rightButton.getStyle().imageDown=new TextureRegionDrawable(tr);
        rightButton.setTransform(true);
        rightButton.setSize(Constants.BUTTON_HEIGHT*2,Constants.BUTTON_HEIGHT*2);
        rightButton.setPosition(game.utils.getCenterPoint(Constants.CAMERA_WIDTH*0.9f,Constants.CAMERA_WIDTH*0.95f,Constants.BUTTON_HEIGHT*2),
                game.utils.getCenterPoint(Constants.CAMERA_HEIGHT*0.4f,Constants.CAMERA_HEIGHT*0.6f,Constants.BUTTON_HEIGHT*2));
        rightButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.audioManager.play("button",false);
                slider.setValue(slider.getValue()+1);
                return true;
            }

        });
        addActor(rightButton);

        selectButton=new TextButton(textureList.get(selectedIndex).get(2)+"\n"+textureList.get(selectedIndex).get(0),skin);
        selectButton.setColor(92/255.0f,220/255.0f,29/255.0f,1f);//green
        selectButton.setSize(selectButton.getWidth()*1.4f,selectButton.getHeight()*1.4f);
        selectButton.setPosition(game.utils.getCenterPoint(0,Constants.CAMERA_WIDTH,selectButton.getWidth()),
                Constants.CAMERA_HEIGHT*0.12f);
        selectButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(game.preferences.getInteger(Constants.COIN)>=Integer.parseInt(textureList.get(selectedIndex).get(2))||game.preferences.getBoolean(textureList.get(selectedIndex).get(0))){
                    game.audioManager.play("select",false);
                    if(!game.preferences.getBoolean(textureList.get(selectedIndex).get(0))){
                        game.preferences.putInteger(Constants.COIN,game.preferences.getInteger(Constants.COIN)-Integer.parseInt(textureList.get(selectedIndex).get(2).toString()));
                    }
                    game.preferences.putBoolean(textureList.get(selectedIndex).get(0),true);
                    game.preferences.putString(Constants.PLAYER_SPRITESHEET,textureList.get(selectedIndex).get(0));
                    game.preferences.putBoolean(Constants.FLIP_SPRITESHEET,Boolean.parseBoolean(textureList.get(selectedIndex).get(1)));
                    game.preferences.flush();

                    selectButton.setText(textureList.get(selectedIndex).get(2)+"\n"+textureList.get(selectedIndex).get(0));

                }else{

                    game.audioManager.play("selectWrong",false);
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        this.addActor(selectButton);

        updatePosition();
    }

    public void render(){
        this.act(Gdx.graphics.getDeltaTime());
        stateTime+=Gdx.graphics.getDeltaTime();

        updatePosition();
        checkPrice();
        this.draw();
    }
    public void updatePosition(){
        Cell cell=mainTable.getCells().get(selectedIndex);
        Table t= (Table) cell.getActor();
        Vector2 position = new Vector2(mainTable.getX()+t.getX(),mainTable.getY()+t.getY());
        if((int)(position.x+t.getWidth()/2.0f)==(int)Constants.CAMERA_WIDTH/2){
            acceleration=0;
            velocity=0;
            return;
        }
        acceleration = velocity * velocity /(2*(Constants.CAMERA_WIDTH/2-position.x-t.getWidth()/2.0f));
        velocity += acceleration * Gdx.graphics.getDeltaTime();
        mainTable.setX(mainTable.getX()- velocity * Gdx.graphics.getDeltaTime());
        //for scaling
        for(int i=0;i<walkAnimationList.size;i++){
            cell=mainTable.getCells().get(i);
            t= (Table) cell.getActor();
            position = new Vector2(mainTable.getX()+t.getX(),mainTable.getY()+t.getY());

            float x= Constants.CAMERA_WIDTH/2-position.x-t.getWidth()/2.0f;
            x=x/(Constants.CAMERA_WIDTH/2-Math.abs(x));
            x=MathUtils.clamp(Math.abs(x),-1,1);
            float scale=MathUtils.clamp(-2.5f*x+2.5f,0.75f,2.5f);
            t.getCells().get(0).getActor().setScale(scale);
        }
    }
    private void checkPrice(){
        if(!game.preferences.getBoolean(textureList.get(selectedIndex).get(0))){
            if(!selectButton.getText().toString().contains("Buy")){
                selectButton.setText("Buy\n"+selectButton.getText());
            }
            selectButton.setColor(251/255.0f,207/255.0f,0,1f);//yellow
        }else{
            selectButton.setColor(92/255.0f,220/255.0f,29/255.0f,1f);//green
        }
    }

}
