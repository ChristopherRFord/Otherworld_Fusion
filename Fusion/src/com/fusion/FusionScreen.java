package com.fusion;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

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
	
	// Viewport & rendering
	protected VirtualViewportCamera GameCamera;
	protected Batch GameBatch;
	protected OrthogonalTiledMapRenderer TiledMapRenderer;
	
	// User interface
	protected Stage Stage;
	
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
		
		AssetManager = AssetGroupManager.GetAssetGroupManager();
		EntityManager = EngineManager.GetEngineManager();
		ScreenManager = GameScreenManager.GetScreenManager();
		
		GameCamera = Game.GameCamera;
		GameBatch = Game.GameBatch;
		
		Stage = Game.Stage;
		
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
		Update(Delta);
		Stage.act();
		
		Render(Delta);
		EntityManager.update(Delta);
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
		EntityManager.removeAllEntities();
	}
	
	protected void RenderMap(TiledMap Map)
	{
		TiledMapRenderer.setView(GameCamera);
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
