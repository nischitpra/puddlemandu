package com.nhuchhe.box2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;


/**
 * Created by nischit on 4/7/17.
 */

public class Resource {
    private String PATH_TEXTURE="sprites/";
    private HashMap<String, BodyEditorLoader> bodyLoaderHashMap;
    private HashMap<String, Sound> soundHashMap;
    private HashMap<String,Music> musicHashMap;
    private HashMap<String, Animation<TextureRegion>> animationHashMap;
    private HashMap<String, TextureRegion> textureRegionHashMap;
    private HashMap<String, TextureAtlas> textureAtlasHashMap;


    public Resource(){
        bodyLoaderHashMap = new HashMap<String, BodyEditorLoader>();
        soundHashMap = new HashMap<String, Sound>();
        musicHashMap = new HashMap<String, Music>();
        animationHashMap = new HashMap<String,  Animation<TextureRegion>>();
        textureRegionHashMap = new HashMap<String, TextureRegion>();
        textureAtlasHashMap = new HashMap<String, TextureAtlas>();
    }

    public Sprite getSprite(String string){
        Sprite sprite=new Sprite(getTextureRegion(Constants.TEXTURE_ATLAS_GAMEWORLD,string));
        sprite.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);
        sprite.setSize(sprite.getWidth()*Constants.SCALE.x,sprite.getHeight()*Constants.SCALE.y);
        return sprite;
    }

    public BodyEditorLoader getBodyEditorLoader(String string){
        if(bodyLoaderHashMap.containsKey(string)){
            return bodyLoaderHashMap.get(string);
        }
        System.out.println("BodyEditorLoader- Loading from memory: "+string);
        bodyLoaderHashMap.put(string,new BodyEditorLoader(Gdx.files.internal(string)));
        return bodyLoaderHashMap.get(string);
    }
    public Sound getSound(String string){
        if(soundHashMap.containsKey(string)){
            return soundHashMap.get(string);
        }
        System.out.println("Sound- Loading from memory: "+string);
        soundHashMap.put(string,Gdx.audio.newSound(Gdx.files.internal(string)));
        return soundHashMap.get(string);
    }
    public Music getMusic(String string){
        if(musicHashMap.containsKey(string)){
            return musicHashMap.get(string);
        }
        System.out.println("Music- Loading from memory: "+string);
        musicHashMap.put(string,Gdx.audio.newMusic(Gdx.files.internal(string)));
        return musicHashMap.get(string);
    }
    public  Animation<TextureRegion> getAnimationSequence(String string,final int FRAME_COLS,final int FRAME_ROWS){
        if(animationHashMap.containsKey(string)){
            return animationHashMap.get(string);
        }
        System.out.println("Animation- Loading from memory: "+string);
        TextureRegion sheet=getTextureRegion(Constants.TEXTURE_ATLAS_GAMEWORLD,string);
        TextureRegion[][] tmp = sheet.split(sheet.getRegionWidth() / FRAME_COLS, sheet.getRegionHeight() / FRAME_ROWS);
        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        animationHashMap.put(string,new Animation<TextureRegion>(Constants.ANIMATION_SPEED,walkFrames));
        return animationHashMap.get(string);
    }
    public Animation<TextureRegion> getAnimationSequence(String string){
        if(animationHashMap.containsKey(string)){
            return animationHashMap.get(string);
        }
        return null;
    }
    public TextureAtlas getTextureAtlas(String string){
        if(textureAtlasHashMap.containsKey(string)){
            return textureAtlasHashMap.get(string);
        }
        System.out.println("Texture Atlas- Loading from memory: "+string);
        textureAtlasHashMap.put(string,new TextureAtlas(Gdx.files.internal(PATH_TEXTURE+string)));
        return textureAtlasHashMap.get(string);
    }
    public TextureRegion getTextureRegion(String atlas,String region){
        if(textureRegionHashMap.containsKey(region)){
            return textureRegionHashMap.get(region);
        }
        System.out.println("Texture Region- Loading from Texture Atlas: "+region);
        textureRegionHashMap.put(region,getTextureAtlas(atlas).findRegion(region));
        return textureRegionHashMap.get(region);
    }

    public boolean isBodyLoaderLoaded(String string){ return bodyLoaderHashMap.get(string)!=null; }
    public boolean isSoundLoaded(String string){ return soundHashMap.get(string)!=null; }
    public boolean isMusicLoaded(String string){ return musicHashMap.get(string)!=null; }
    public boolean isAnimationLoaded(String string){ return animationHashMap.get(string)!=null; }
    public boolean isTextureAtlasLoaded(String string){ return textureAtlasHashMap.get(string)!=null; }
    public boolean isTextureRegionLoaded(String string){ return textureRegionHashMap.get(string)!=null; }

    public void disposeSound(String string){ soundHashMap.get(string).dispose(); }
    public void disposeMusic(String string){ musicHashMap.get(string).dispose(); }
    public void disposeTextureAtlas(String string){ textureAtlasHashMap.get(string).dispose(); }
    public void disposeTextureRegion(String string){  }
    public void disposeBodyLoader(String string){  }
    public void disposeAnimation(String string){  }

    public void dispose(HashMap<Integer,Array<String>> resourceToDispose){
        for(int i=0;i<resourceToDispose.size();i++){
            if(resourceToDispose.get(i).get(0)=="sound"){
                if(isSoundLoaded(resourceToDispose.get(i).get(1))){
                    disposeSound(resourceToDispose.get(i).get(1));
                }
            }else if(resourceToDispose.get(i).get(0)=="music"){
                if(isMusicLoaded(resourceToDispose.get(i).get(1))){
                    disposeMusic(resourceToDispose.get(i).get(1));
                }
            }else if(resourceToDispose.get(i).get(0)=="bodyLoader"){
                if(isBodyLoaderLoaded(resourceToDispose.get(i).get(1))){
                    disposeBodyLoader(resourceToDispose.get(i).get(1));
                }
            }else if(resourceToDispose.get(i).get(0)=="animation"){
                if(isAnimationLoaded(resourceToDispose.get(i).get(1))){
                    disposeAnimation(resourceToDispose.get(i).get(1));
                }
            }else if(resourceToDispose.get(i).get(0)=="textureRegion"){
                if(isTextureRegionLoaded(resourceToDispose.get(i).get(1))){
                    disposeTextureRegion(resourceToDispose.get(i).get(1));
                }
            }else if(resourceToDispose.get(i).get(0)=="textureAtlas"){
                if(isTextureAtlasLoaded(resourceToDispose.get(i).get(1))){
                    disposeTextureAtlas(resourceToDispose.get(i).get(1));
                }
            }
        }
        
        bodyLoaderHashMap.clear();
        soundHashMap.clear();
        musicHashMap.clear();
        animationHashMap.clear();
        textureAtlasHashMap.clear();
        textureRegionHashMap.clear();
    }
}
