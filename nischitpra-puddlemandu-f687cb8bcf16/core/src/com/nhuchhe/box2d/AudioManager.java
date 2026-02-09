package com.nhuchhe.box2d;

import com.badlogic.gdx.math.MathUtils;

/**
 * Created by nischit on 11/7/17.
 */

public class AudioManager {
    private MyGame game;
    private long vehicle_id;
    private boolean once_highscore;

    public AudioManager(MyGame game){
        this.game=game;
    }


    public void stopSound_Bus(){
        game.resource.getSound("bus.mp3").stop();
    }
    public void stopSound_Motorcycle_0(){
        game.resource.getSound("motorcycle_0.mp3").stop();
    }
    public void stopSound_Motorcycle_1(){
        game.resource.getSound("motorcycle_1.mp3").stop();
    }
    public void stopSound_Car(){game.resource.getSound("car.mp3").stop();}
    public void stopSound_Cow(){game.resource.getSound("cow.mp3").stop();}
    public void stopSound_Zombie_0(){game.resource.getSound("zombie_0.mp3").stop();}
    public void stopSound_Zombie_1(){game.resource.getSound("zombie_1.mp3").stop();}
    public void stopSound_Dog_0(){game.resource.getSound("dog_0.mp3").stop();}
    public void stopSound_Dog_1(){game.resource.getSound("dog_1.mp3").stop();}
    public void stopSound_Chicken_0(){game.resource.getSound("chicken_0.mp3").stop();}

    private void playMusic_Background(){
        game.resource.getMusic("background.mp3").play();
    }
    private void playVehicleSound(String s){
        vehicle_id = game.resource.getSound(s+".mp3").play();
        game.resource.getSound(s+".mp3").setLooping(vehicle_id,true);
    }
    private void playSound(String s){
        game.resource.getSound(s+".mp3").play();
    }

    public void play(String s,boolean isVehicle){
        if(!game.preferences.getBoolean(Constants.MUSIC)){
            return;
        }
        if(s.equals("background")){
            playMusic_Background();
        }else if(s.equals("highscore")){
            playHighscore();
        }else if(s.equals("scream")){
            playScream();
        }
        else{
            if(isVehicle){
                playVehicleSound(s);
            }else{
                playSound(s);
            }
        }
    }

    public void updateSettings(){
        if(!game.preferences.getBoolean(Constants.MUSIC)){
            if(game.resource.getMusic("background.mp3").isPlaying()){
                game.resource.getMusic("background.mp3").stop();
            }
        }else if(game.preferences.getBoolean(Constants.MUSIC)){
            if(!game.resource.getMusic("background.mp3").isPlaying()){
                game.resource.getMusic("background.mp3").play();
            }
        }
    }
    private void playScream(){
        if(game.preferences.getString(Constants.PLAYER_SPRITESHEET).contains("Maichyang")){
            game.audioManager.play("scream_girl",false);
        }else{
            game.audioManager.play("scream_"+ MathUtils.random(0,3),false);
        }
    }
    private void playHighscore(){
        if(!once_highscore){
            game.resource.getSound("highscore.mp3").play();
            once_highscore=true;
        }
    }

    public void dispose(){


    }

    public void reset(){
        once_highscore=false;
    }

    public void configureAudioSystem(){
        game.resource.getMusic("background.mp3").setVolume(0.6f);
        game.resource.getMusic("background.mp3").setLooping(true);
        reset();
    }
}
