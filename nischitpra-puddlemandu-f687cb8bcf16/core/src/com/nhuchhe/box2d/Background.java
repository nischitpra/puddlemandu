package com.nhuchhe.box2d;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by nischit on 6/7/17.
 *
 * HUD camera is used for rendering the background
 *
 * genereate building at the front row ,,, behind it will be miscellaneous stuffs like forests etc.
 */

public class Background {
    private MyGame game;
    private Array<String> string_skyList,string_mountainList,string_hillList,string_buildingList, string_poleList, string_landmarkList, string_miscellaneousList;

    private CircularQueue sky;
    private CircularQueue mountain;
    private CircularQueue hill;
    private CircularQueue miscellaneous;
    private CircularQueue building;
    private CircularQueue pole;
    private boolean once=true;

    private int counter;

    private final int LANDMARK_DENSITY;


    private float scale_sky=4.6f,scale_mountain=2f,scale_hill=1f,scale_miscellaneous=1.1f,scale_building=1f,scale_pole=3f;//1.8f;
    private float movespeed_sky=0.1f,movespeed_mountain=0.5f,movespeed_hill=1.3f,movespeed_miscellaneous=2.0f,movespeed_building=2.8f,movespeed_pole=4f;

    private Sprite skyFilterNight,skyFilter;
    private float alpha;
    private float alphaIncrementor=0.002f;
    private long dayNightLength;

    private String LANDMARK="landmark";
    private String MISC="misc";


    public Background(MyGame game){
        this.game=game;

        LANDMARK_DENSITY=MathUtils.random(2,3);
        int maxTextureQueue=10;

        // Ensure each layer has enough tiles to cover ultra-wide devices.
        int skySize = Math.max(3, tilesFor("sky", scale_sky));
        int mountainSize = Math.max(3, tilesFor("mountain", scale_mountain));
        int hillSize = Math.max(3, tilesFor("hill", scale_hill));
        int miscSize = Math.max(4, tilesFor("jungle", scale_miscellaneous));
        int buildingSize = Math.max(9, tilesFor("Buildings 0", scale_building));
        int poleSize = Math.max(3, tilesFor("pole", scale_pole));

        // Align sky to the top of the screen so it doesn't get clipped on ultra-wide devices.
        float skyY = Constants.CAMERA_HEIGHT - game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_BACKGROUND, "sky").getRegionHeight() * scale_sky;
        sky=new CircularQueue(game,Constants.TEXTURE_ATLAS_BACKGROUND,skySize,maxTextureQueue,scale_sky,skyY,movespeed_sky);
        mountain=new CircularQueue(game,Constants.TEXTURE_ATLAS_BACKGROUND,mountainSize,maxTextureQueue,scale_mountain,Constants.CAMERA_HEIGHT*0.4f,movespeed_mountain);
        hill=new CircularQueue(game,Constants.TEXTURE_ATLAS_BACKGROUND,hillSize,maxTextureQueue,scale_hill,Constants.CAMERA_HEIGHT*0.3f,movespeed_hill);
        miscellaneous=new CircularQueue(game,Constants.TEXTURE_ATLAS_BACKGROUND,miscSize,maxTextureQueue,scale_miscellaneous,Constants.CAMERA_HEIGHT*0.09f,movespeed_miscellaneous);
        building=new CircularQueue(game,Constants.TEXTURE_ATLAS_BACKGROUND,buildingSize,maxTextureQueue,scale_building,Constants.CAMERA_HEIGHT*0.1f,movespeed_building);
        pole=new CircularQueue(game,Constants.TEXTURE_ATLAS_BACKGROUND,poleSize,maxTextureQueue,scale_pole,Constants.CAMERA_HEIGHT*0.1f,movespeed_pole);

        counter=0;

        string_skyList=new Array<String>();
        string_mountainList=new Array<String>();
        string_hillList=new Array<String>();
        string_landmarkList =new Array<String>();
        string_buildingList=new Array<String>();
        string_poleList =new Array<String>();
        string_miscellaneousList=new Array<String>();

        string_skyList.add("sky");

        string_mountainList.add("mountain");

        string_hillList.add("hill");

        string_landmarkList.add("Patan Durbar");
        string_landmarkList.add("Patan Dhoka");
        string_landmarkList.add("Dharahara");
        string_landmarkList.add("Basantapur");
        string_landmarkList.add("Swayambhu");
        string_landmarkList.add("Pashupati");
        string_landmarkList.add("Bouddha");

        string_buildingList.add("Buildings 0");
        string_buildingList.add("Buildings 2");
        string_buildingList.add("Buildings 3");
        string_buildingList.add("Buildings 4");
        string_buildingList.add("tree0");
        string_buildingList.add("tree1");
        string_buildingList.add("tree2");
        string_buildingList.add("tree3");
        string_buildingList.add("tree4");
        string_buildingList.add("tree5");
        string_buildingList.add("tree6");

        string_poleList.add("pole");
        string_poleList.add("poleLight");


        string_miscellaneousList.add("jungle");

        fillRandom(sky,string_skyList);
        fillRandom(mountain,string_mountainList);
        fillRandom(hill,string_hillList);
        fillRandom(miscellaneous,string_miscellaneousList);
        fillBuilding();
        fillRandom(pole,string_poleList);

        sky.createActiveObject(0);
        mountain.createActiveObject(0);
        hill.createActiveObject(0);
        miscellaneous.createActiveObject(0);
        building.createActiveObject(0);
        pole.createActiveObject(0);

        fillRandom(sky,string_skyList);
        fillRandom(mountain,string_mountainList);
        fillRandom(hill,string_hillList);
        fillRandom(miscellaneous,string_miscellaneousList);
        fillBuilding();
        fillRandom(pole,string_poleList);


        skyFilterNight =new Sprite(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_BACKGROUND,"night"));
        skyFilterNight.setSize(Constants.CAMERA_WIDTH*1f,Constants.CAMERA_HEIGHT*0.9f);
        skyFilterNight.setPosition(0,Constants.CAMERA_HEIGHT*0.1f);

        alpha=0;
        skyFilterNight.setAlpha(alphaIncrementor>0?alpha>1?1:alpha:alpha<0?0:alpha);
        dayNightLength=game.utils.randomTime(8,12);

        skyFilter=new Sprite(game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_BACKGROUND,"skyFilter2"));
        skyFilter.setPosition(0,Constants.CAMERA_HEIGHT*0.1f);
        skyFilter.setSize(Constants.CAMERA_WIDTH,Constants.CAMERA_HEIGHT);

    }

    private int tilesFor(String textureName, float scale) {
        float tileW = game.resource.getTextureRegion(Constants.TEXTURE_ATLAS_BACKGROUND, textureName).getRegionWidth() * scale;
        if (tileW <= 0) return 3;
        // +2 for safety (offscreen + rounding)
        return (int)Math.ceil(Constants.CAMERA_WIDTH / tileW) + 2;
    }
    private void fillRandom(CircularQueue queue,Array<String> array){
        for(int i=0;i<queue.MAX_TEXTURE_SIZE;i++){
            queue.enqueueTextureQueue(array.get(MathUtils.random(0,array.size-1)),MISC);
        }
    }

    private void fillBuilding(){
        for(int i=0;i<building.MAX_TEXTURE_SIZE;i++){
            if(i%LANDMARK_DENSITY==0){
                building.enqueueTextureQueue(string_landmarkList.get(counter),LANDMARK);
                if(counter==string_landmarkList.size-1){
                    counter=0;
                }else{
                    counter++;
                }
            }else{
                building.enqueueTextureQueue(string_buildingList.get(MathUtils.random(0,string_buildingList.size-1)),MISC);
            }
        }
    }
    public String getLocation(){
        return building.getActiveTextureName();
    }

    public float getAlphaIncrementor(){return alphaIncrementor;}
    public float getAlpha(){return alpha;}
    private void dayNightCycle(){
        if(alpha<0 && once){
            dayNightLength=game.utils.randomTime(20,20);
            once=false;
            alphaIncrementor=Math.abs(alphaIncrementor);
            alpha=0;
        }
        else if(alpha>1 && once){
            dayNightLength=game.utils.randomTime(20,20);
            once=false;
            alphaIncrementor=-Math.abs(alphaIncrementor);
            alpha=1;
        }
        if(TimeUtils.nanoTime()>dayNightLength && !once){
            once=true;
        }
        if(alpha>=0 && alpha<=1){
            skyFilterNight.setAlpha(alpha);
            alpha+=alphaIncrementor;
        }
    }

    public void update(){
        sky.update();
        mountain.update();
        hill.update();
        miscellaneous.update();
        building.update();
        pole.update();

        if(sky.getTextureQueueSize()<=1){
            fillRandom(sky,string_skyList);
        }
        if(mountain.getTextureQueueSize()<=1){
            fillRandom(mountain,string_mountainList);
        }
        if(hill.getTextureQueueSize()<=1){
            fillRandom(hill,string_hillList);
        }
        if(miscellaneous.getTextureQueueSize()<=1){
            fillRandom(miscellaneous,string_miscellaneousList);
        }
        if(building.getTextureQueueSize()<=1){
            fillBuilding();
        }
        if(pole.getTextureQueueSize()<=1){
            fillRandom(pole,string_poleList);
        }
        dayNightCycle();
    }
    public void stopScroll(){
        sky.stopScroll();
        mountain.stopScroll();
        hill.stopScroll();
        miscellaneous.stopScroll();
        building.stopScroll();
        pole.stopScroll();
    }
    public void render() {
        sky.render();
        mountain.render();
        hill.render();
        miscellaneous.render();
        building.render();
        pole.render();
        skyFilter.draw(game.batch);
        skyFilterNight.draw(game.batch);


    }
    public void dispose(){

    }
}
