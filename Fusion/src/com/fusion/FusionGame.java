package com.fusion;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
	protected AssetGroupManager AssetManager;
	protected EngineManager EntityManager;
	protected GameScreenManager ScreenManager;
	protected PhysicsWorldManager PhysicsManager;
	
	// Viewport & rendering
	protected VirtualViewportFactory VirtualViewportFactory;
	protected VirtualViewportCamera GameCamera;
	protected VirtualViewportCamera PhysicsCamera;
	protected Batch GameBatch;
	protected Batch UIBatch;
	protected OrthogonalTiledMapRenderer TiledMapRenderer;
	
	// User interface
	protected Stage Stage;
	protected Console Console;
	
	// Debugging
	protected DebugRenderer DebugRenderer;
	protected boolean Debug;
	
	// Is the game started
	private boolean Start;

	@Override
	public void create()
	{
		AssetManager = AssetGroupManager.GetAssetGroupManager();
		EntityManager = EngineManager.GetEngineManager();
		ScreenManager = GameScreenManager.GetScreenManager();
		PhysicsManager = PhysicsWorldManager.GetPhysicsWorldManager();
		
		VirtualViewportFactory = new VirtualViewportFactory(800, 480, 854, 600);
		GameCamera = new VirtualViewportCamera();
		PhysicsCamera = new VirtualViewportCamera();
		GameBatch = new SpriteBatch();
		UIBatch = new SpriteBatch();
		TiledMapRenderer = new OrthogonalTiledMapRenderer(null, GameBatch);
		
		EntityManager.GetEngine().addSystem(new RenderSystem(GameBatch));
		EntityManager.GetEngine().addSystem(new PhysicsSystem());
		EntityManager.GetEngine().addSystem(new DesktopInputSystem(this));
		
		DebugRenderer = new DebugRenderer(GameCamera, PhysicsCamera);
		Debug = false;
		
		Start = false;
	}
	
	@Override
	public void resize(int width, int height)
	{
		
		// Setting up the GameCamera
		VirtualViewport VirtualViewport = VirtualViewportFactory.getVirtualViewport(width, height);
		GameCamera.setVirtualViewport(VirtualViewport);
		GameCamera.updateViewport();
		GameCamera.position.set(GameCamera.viewportWidth/2, GameCamera.viewportHeight/2, 0);
		
		// Setting up the PhysicsCamera
		PhysicsCamera.setVirtualViewport(new VirtualViewport(GameCamera.viewportWidth/SCALING_FACTOR, GameCamera.viewportHeight/SCALING_FACTOR));
		PhysicsCamera.updateViewport();
		PhysicsCamera.position.set(GameCamera.position.x/SCALING_FACTOR, GameCamera.position.y/SCALING_FACTOR, 0);
		
		// Remake the stage
		if (Stage != null) Stage.dispose();
		Stage = new Stage(new StretchViewport(VirtualViewport.getWidth(), VirtualViewport.getHeight()), UIBatch);
		Gdx.input.setInputProcessor(Stage);
		
		super.resize(width, height);
		
		// If it's the initial resize init
		// and start the game
		if (!Start)
		{
			Start = true;
			Init();
			Start();
		}
	}
	
	@Override
	public void render()
	{			
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		GameCamera.update();
		GameBatch.setProjectionMatrix(GameCamera.combined);
		
		PhysicsCamera.update();
		
		super.render();
	}
	
	@Override
	public void dispose()
	{
		AssetManager.dispose();
		
		Close();
	}
	
	public abstract void Init();
	public abstract void Start();
	public abstract void Close();
	
	public VirtualViewportCamera GetGameCamera()			{	return GameCamera;			}
	public VirtualViewportCamera GetPhysicsCamera()			{	return PhysicsCamera;		}
	public Batch GetGameBatch()								{	return GameBatch;			}
	public Batch GetUIBatch()								{	return UIBatch;				}
	public OrthogonalTiledMapRenderer GetTiledMapRenderer()	{	return TiledMapRenderer;	}
	
	public Stage GetStage()									{	return Stage;				}
	public Console GetConsole()								{	return Console;				}
	
	public boolean GetDebug()								{	return Debug;				}
	public void ToggleDebug()								{	Debug = !Debug;				}
}
