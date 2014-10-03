package com.fusion;

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
	protected AssetGroupManager AssetManager;
	protected EngineManager EntityManager;
	protected GameScreenManager ScreenManager;
	protected PhysicsWorldManager PhysicsManager;
	
	// Viewport & rendering
	protected VirtualViewportCamera GameCamera;
	protected VirtualViewportCamera PhysicsCamera;
	protected Batch GameBatch;
	protected OrthogonalTiledMapRenderer TiledMapRenderer;
	
	// User interface
	protected Stage Stage;
	
	// Debugging
	protected DebugRenderer DebugRenderer;
	
	// Reference to the game
	protected final FusionGame Game;
	
	// ID of the screen
	protected final int ID;
	
	// Different ways a screen can be entered and left
	public enum ScreenSwitchState
	{
		SET_SCREEN,
		
		PUSH_SCREEN,
		POP_SCREEN;
	}
	
	
	public FusionScreen(int ID, FusionGame Game)
	{
		this.ID = ID;
		this.Game = Game;
		
		AssetManager = Game.AssetManager;
		EntityManager = Game.EntityManager;
		ScreenManager = Game.ScreenManager;
		PhysicsManager = Game.PhysicsManager;
		
		GameCamera = Game.GameCamera;
		PhysicsCamera = Game.PhysicsCamera;
		GameBatch = Game.GameBatch;
		
		Stage = Game.Stage;
		
		DebugRenderer = Game.DebugRenderer;
		
		TiledMapRenderer = Game.TiledMapRenderer;
	}
	
	public int getID(){	return ID;	}

	protected 	abstract void Update(float Delta);
	protected 	abstract void Render(float Delta);
	public 		abstract void Enter(ScreenSwitchState State);
	public 		abstract void Leave(ScreenSwitchState State);
	protected 	abstract void InitGUI(int Width, int Height);
	
	@Override
	public void render(float Delta)
	{
		// Screen's update method
		Update(Delta);
		
		// UI's update method
		Stage.act();
		
		// Physic's update method
		PhysicsManager.Step(1/45f, 6, 2);
		
		// Screen's rendering method
		Render(Delta);
		
		// Engine's update & rendering method
		EntityManager.GetEngine().update(Delta);
		DebugRenderer.DebugRender(Game.GetDebug());
		
		// UI's render method
		Stage.draw();
	}

	@Override
	public void resize(int Width, int Height)
	{
		InitGUI(Width, Height);
	}
	
	@Override
	public void hide()
	{
	}
	
	protected void RenderMap()
	{
		TiledMapRenderer.setView(GameCamera);
		
		if (TiledMapRenderer.getMap() != null)
			TiledMapRenderer.render();
	}

	@Override
	public void show(){}
	@Override
	public void pause(){}
	@Override
	public void resume(){}
	@Override
	public void dispose(){}
	
	
}
