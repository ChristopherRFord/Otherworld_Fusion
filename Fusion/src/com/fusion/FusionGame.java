package com.fusion;

import box2dLight.RayHandler;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import com.fusion.ecs.systems.DesktopInputSystem;
import com.fusion.ecs.systems.PhysicsSystem;
import com.fusion.ecs.systems.RenderSystem;
import com.fusion.gfx.DebugRenderer;
import com.fusion.gfx.VirtualViewportFactory;
import com.fusion.gfx.VirtualViewport;
import com.fusion.gfx.VirtualViewportCamera;
import com.fusion.phx.SimpleContactListener;
import com.fusion.util.*;

import static com.fusion.util.PhysicsWorldManager.SCALING_FACTOR;

/**
 * FusionGame
 * @author Christopher Ford
 *
 * The first entry point for the game. This class is used to
 * load common assets, create screens and just start the game
 * in general.
 */
public abstract class FusionGame extends Game
{
	// Managers
	protected AssetGroupManager 	assetManager;
	protected EngineManager 		entityManager;
	protected GameScreenManager 	screenManager;
	protected PhysicsWorldManager 	physicsManager;
	
	// Viewport & rendering
	protected VirtualViewportFactory 		virtualViewportFactory;
	protected VirtualViewportCamera 		gameCamera;
	protected VirtualViewportCamera 		physicsCamera;
	protected Batch 						gameBatch;
	protected Batch 						uiBatch;
	protected OrthogonalTiledMapRenderer 	tiledMapRenderer;
	protected RayHandler 					lightWorld;
	
	// User interface
	protected Stage 	stage;
	protected Console	console;
	
	// Debugging
	protected DebugRenderer 	debugRenderer;
	protected boolean 			debug;
	
	// Is the game started
	private boolean start;

	@Override
	public void create()
	{
		assetManager = AssetGroupManager.getAssetGroupManager();
		entityManager = EngineManager.getEngineManager();
		screenManager = GameScreenManager.getScreenManager();
		physicsManager = PhysicsWorldManager.getPhysicsWorldManager();
		
		virtualViewportFactory = new VirtualViewportFactory(800, 480, 854, 600);
		gameCamera = new VirtualViewportCamera();
		physicsCamera = new VirtualViewportCamera();
		gameBatch = new SpriteBatch();
		uiBatch = new SpriteBatch();
		
		tiledMapRenderer = new OrthogonalTiledMapRenderer(null, gameBatch);
		lightWorld = physicsManager.getLightWorld();
		lightWorld.setAmbientLight(Color.BLACK);
		lightWorld.setShadows(true);
		
		entityManager.getEngine().addSystem(new RenderSystem(gameBatch));
		entityManager.getEngine().addSystem(new PhysicsSystem());
		entityManager.getEngine().addSystem(new DesktopInputSystem(this));
		
		physicsManager.getWorld().setContactListener(new SimpleContactListener());
		
		debugRenderer = new DebugRenderer(gameCamera, physicsCamera);
		debug = false;
		
		start = false;
	}
	
	@Override
	public void resize(int width, int height)
	{
		// Setting up the GameCamera
		VirtualViewport virtualViewport = virtualViewportFactory.getVirtualViewport(width, height);
		gameCamera.setVirtualViewport(virtualViewport);
		gameCamera.updateViewport();
		gameCamera.position.set(gameCamera.viewportWidth/2, gameCamera.viewportHeight/2, 0);
		
		// Setting up the PhysicsCamera
		physicsCamera.setVirtualViewport(new VirtualViewport(gameCamera.viewportWidth/SCALING_FACTOR, gameCamera.viewportHeight/SCALING_FACTOR));
		physicsCamera.updateViewport();
		physicsCamera.position.set(gameCamera.position.x/SCALING_FACTOR, gameCamera.position.y/SCALING_FACTOR, 0);
		
		if (stage == null)
			stage = new Stage(new StretchViewport(virtualViewport.getWidth(), virtualViewport.getHeight()), uiBatch);
		else
		{
			stage.clear();
			stage.setViewport(new StretchViewport(virtualViewport.getWidth(), virtualViewport.getHeight()));
		}
		Gdx.input.setInputProcessor(stage);
		
		super.resize(width, height);
		
		// If it's the initial resize init
		// and start the game
		if (!start)
		{
			start = true;
			init();
			start();
		}
	}
	
	@Override
	public void render()
	{			
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		gameCamera.update();
		gameBatch.setProjectionMatrix(gameCamera.combined);
		
		physicsCamera.update();
		
		super.render();
	}
	
	@Override
	public void dispose()
	{
		assetManager.dispose();
		
		close();
	}
	
	public abstract void init();
	public abstract void start();
	public abstract void close();
	
	public VirtualViewportCamera getGameCamera()			{	return gameCamera;			}
	public VirtualViewportCamera getPhysicsCamera()			{	return physicsCamera;		}
	public Batch getGameBatch()								{	return gameBatch;			}
	public Batch getUIBatch()								{	return uiBatch;				}
	public OrthogonalTiledMapRenderer getTiledMapRenderer()	{	return tiledMapRenderer;	}
	public RayHandler getLightWorld()						{	return lightWorld;			}
	
	public Stage getStage()									{	return stage;				}
	public Console getConsole()								{	return console;				}
	
	public boolean getDebug()								{	return debug;				}
	public void toggleDebug()								{	debug = !debug;				}
}
