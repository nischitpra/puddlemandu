package com.nhuchhe.box2d;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;



/**
 * always change the screen at the end of the render looop and never in the middle.
 * do not dispose texture if you wish to restart a game.. ortherwise will have to reload it all manually
 */

public class MyGame extends Game {
	public World world;
	public SpriteBatch batch;
	public BitmapFont font_verySmall,font_small,font_medium,font_big;
	public MyGame game;
	public Preferences preferences;
	public AudioManager audioManager;
	public Box2DDebugRenderer debugRenderer;
	public Resource resource;
	public Utils utils;

	public enum Status{
		RESUME,PAUSE;
	};

	Status status;

	@Override
	public void create() {
		Constants.init();
		System.out.println("Gdx.graphics: w=" + Gdx.graphics.getWidth()
				+ " h=" + Gdx.graphics.getHeight()
				+ " bbW=" + Gdx.graphics.getBackBufferWidth()
				+ " bbH=" + Gdx.graphics.getBackBufferHeight()
		);
		game=this;
		world=new World(new Vector2(0,0f),true);
		world.setGravity(new Vector2(0,Constants.GRAVITY));
		batch=new SpriteBatch();
		debugRenderer=new Box2DDebugRenderer();
		this.utils=new Utils();

		System.out.println("initializing resource to load hashmap");

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/MostlyMono.ttf"));
		FreeTypeFontGenerator.setMaxTextureSize(FreeTypeFontGenerator.NO_MAXIMUM);
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = Constants.TEXT_SIZE_SMALL;
		font_small = generator.generateFont(parameter);
		parameter.size = Constants.TEXT_SIZE_MEDIUM;
		font_medium = generator.generateFont(parameter);
		parameter.size = Constants.TEXT_SIZE_BIG;
		font_big=generator.generateFont(parameter);
		parameter.size = Constants.TEXT_SIZE_VERY_SMALL;
		font_verySmall=generator.generateFont(parameter);

		generator.dispose();

		preferences= Gdx.app.getPreferences("GamePreference");

		//initialization for first time
		if(!game.preferences.contains(Constants.MUSIC)){
			game.preferences.putBoolean(Constants.MUSIC,true);
			game.preferences.flush();
		}
		if(!game.preferences.contains(Constants.JUMP)){
			game.preferences.putInteger(Constants.JUMP,2);
			game.preferences.flush();
			System.out.println("jump");
		}
		if(!game.preferences.contains(Constants.PLAYER_SPRITESHEET)){
			game.preferences.putString(Constants.PLAYER_SPRITESHEET,"Alibuba");
			game.preferences.putBoolean("Alibuba",true);
			game.preferences.putBoolean(Constants.FLIP_SPRITESHEET,false);
			game.preferences.putString(Constants.PLACE,"Patan Durbar");
		}

		resource=new Resource();
		audioManager=new AudioManager(game);
		this.setScreen(new SplashScreen(game));
		System.out.println("inside create of MyGame");

	}

	@Override
	public void resize(int width, int height) {
		Constants.init();
		System.out.println("Gdx.graphics(resize): w=" + Gdx.graphics.getWidth()
				+ " h=" + Gdx.graphics.getHeight()
				+ " bbW=" + Gdx.graphics.getBackBufferWidth()
				+ " bbH=" + Gdx.graphics.getBackBufferHeight()
		);
		super.resize(width, height);
	}

	@Override
	public void render() {super.render();}

	@Override
	public void dispose() {
		super.dispose();
		System.out.println("dispose");
		world.dispose();
		batch.dispose();
		font_small.dispose();
		font_big.dispose();
		resource.dispose(Constants.resourceToLoad);
	}

	@Override
	public void resume() {
		super.resume();
		System.out.println("resume");

		/**
		 * this might cause texture errors.
		 */
		if(status!=Status.PAUSE){
			create();
		}
		Constants.reset();
		status=Status.RESUME;
	}

	@Override
	public void pause() {
		super.pause();
		System.out.println("pause");
		// dispose is called by default when calling the application
		preferences.putFloat(Constants.SCORE,0);
		preferences.flush();

		// i dont think dispose is called by default

		status=Status.PAUSE;
	}

}