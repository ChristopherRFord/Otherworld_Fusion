package com.fusion;

import box2dLight.RayHandler;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.fusion.gfx.DebugRenderer;
import com.fusion.gfx.VirtualViewportCamera;
import com.fusion.util.*;

/**
 * FusionScreen
 * @author Christopher Ford
 * 
 * Template for a game screen. This employs the State
 * design pattern, which splits the game into different
 * states so it is easier to organize.
 */
public abstract class FusionScreen implements Screen
{
	// Managers
	protected AssetGroupManager 	assetManager;
	protected EngineManager 		entityManager;
	protected GameScreenManager 	screenManager;
	protected PhysicsWorldManager 	physicsManager;
	
	// Viewport & rendering
	protected VirtualViewportCamera 		gameCamera;
	protected VirtualViewportCamera 		physicsCamera;
	protected Batch 						gameBatch;
	protected OrthogonalTiledMapRenderer 	tiledMapRenderer;
	protected RayHandler 					lightWorld;
	
	// User interface
	protected Stage stage;
	
	// Debugging
	protected DebugRenderer debugRenderer;
	
	// Reference to the game
	protected final FusionGame game;
	
	// ID of the screen
	protected final int ID;
	
	// Different ways a screen can be entered and left
	public enum ScreenSwitchState
	{
		SET_SCREEN,
		
		PUSH_SCREEN,
		POP_SCREEN;
	}
	
	public FusionScreen(int ID, FusionGame game)
	{
		this.ID = ID;
		this.game = game;
		
		assetManager = game.assetManager;
		entityManager = game.entityManager;
		screenManager = game.screenManager;
		physicsManager = game.physicsManager;
		
		gameCamera = game.gameCamera;
		physicsCamera = game.physicsCamera;
		gameBatch = game.gameBatch;
		
		stage = game.stage;
		
		tiledMapRenderer = game.tiledMapRenderer;
		lightWorld = game.lightWorld;
		
		debugRenderer = game.debugRenderer;
	}
	
	public int getID(){	return ID;	}

	protected 	abstract void Update(float delta);
	protected 	abstract void Render(float delta);
	public 		abstract void Enter(ScreenSwitchState state);
	public 		abstract void Leave(ScreenSwitchState state);
	protected 	abstract void InitGUI(int width, int height);
	
	@Override
	public void render(float delta)
	{
		// Screen's update method
		Update(delta);
		
		// UI's update method
		stage.act();
		
		// Physic's update method
		physicsManager.step(1/45f, 6, 2);
		
		// Screen's rendering method
		Render(delta);
		
		// Engine's update & rendering method
		entityManager.getEngine().update(delta);
		
		lightWorld.setCombinedMatrix(gameCamera.combined);
		lightWorld.updateAndRender();
		debugRenderer.debugRender(game.getDebug());
		
		// UI's render method
		stage.draw();
	}

	@Override
	public void resize(int width, int height)
	{
		InitGUI(width, height);
	}
	
	@Override
	public void hide(){}
	
	protected void renderMap()
	{
		tiledMapRenderer.setView(gameCamera);
		
		if (tiledMapRenderer.getMap() != null)
			tiledMapRenderer.render();
	}

	@Override
	public void show()
	{
		gameCamera.position.set(gameCamera.viewportWidth/2, gameCamera.viewportHeight/2, 0);
	}
	@Override
	public void pause(){}
	@Override
	public void resume(){}
	@Override
	public void dispose(){}
}
