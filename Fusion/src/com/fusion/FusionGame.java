package com.fusion;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import com.fusion.ecs.systems.RenderSystem;
import com.fusion.gfx.VirtualViewportFactory;
import com.fusion.gfx.VirtualViewport;
import com.fusion.gfx.VirtualViewportCamera;
import com.fusion.util.*;

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
	
	// Viewport & rendering
	protected VirtualViewportFactory VirtualViewportFactory;
	protected VirtualViewportCamera GameCamera;
	protected Batch GameBatch;
	protected OrthogonalTiledMapRenderer TiledMapRenderer;
	
	// User interface
	protected Stage Stage;
	protected Console Console;
	
	// Is the game started
	private boolean Start;

	@Override
	public void create()
	{
		AssetManager = AssetGroupManager.GetAssetGroupManager();
		EntityManager = EngineManager.GetEngineManager();
		ScreenManager = GameScreenManager.GetScreenManager();
		
		VirtualViewportFactory = new VirtualViewportFactory(800, 480, 854, 600);
		GameCamera = new VirtualViewportCamera();
		GameBatch = new SpriteBatch();
		TiledMapRenderer = new OrthogonalTiledMapRenderer(null, GameBatch);
		
		EntityManager.addSystem(new RenderSystem(GameBatch));
		
		Start = false;
	}
	
	@Override
	public void resize(int width, int height)
	{
		// Setting up the camera
		VirtualViewport VirtualViewport = VirtualViewportFactory.getVirtualViewport(width, height);
		GameCamera.setVirtualViewport(VirtualViewport);
		GameCamera.position.set(VirtualViewport.getWidth()/2, VirtualViewport.getHeight()/2, 0);
		GameCamera.updateViewport();
		
		// Remake the stage
		if (Stage != null) Stage.dispose();
		Stage = new Stage(new StretchViewport(VirtualViewport.getWidth(), VirtualViewport.getHeight()), GameBatch);
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
	public Batch GetGameBatch()								{	return GameBatch;			}
	public OrthogonalTiledMapRenderer GetTiledMapRenderer()	{	return TiledMapRenderer;	}
	
	public Stage GetStage()									{	return Stage;				}
	public Console GetConsole()								{	return Console;				}
}
