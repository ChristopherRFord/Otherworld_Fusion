package com.fusion;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
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
	protected AssetGroupManager AssetManager;
	protected GameScreenManager ScreenManager;
	
	protected VirtualViewportCamera GameCamera;
	protected VirtualViewportCamera UICamera;
	
	protected Batch GameBatch;
	protected Batch UIBatch;
	
	protected Stage Stage;
	
	// Different ways a screen can be entered
	// and left
	public enum ScreenSwitchState
	{
		SET_SCREEN,
		
		PUSH_SCREEN,
		POP_SCREEN;
	}
	
	protected final int ID;
	protected final FusionGame Game;
	
	public FusionScreen(int ID, FusionGame Game)
	{
		this.ID = ID;
		this.Game = Game;
		
		AssetManager = AssetGroupManager.GetAssetGroupManager();
		ScreenManager = GameScreenManager.GetScreenManager();
		
		GameCamera = Game.GameCamera;
		UICamera = Game.UICamera;
		
		GameBatch = Game.GameBatch;
		
		Stage = Game.Stage;
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
		
		GameBatch.begin();
		Render(Delta);
		GameBatch.end();
	}

	@Override
	public void resize(int Width, int Height)
	{
		InitGUI(Width, Height);
	}

	@Override
	public void show(){}
	@Override
	public void hide(){}
	@Override
	public void pause(){}
	@Override
	public void resume(){}
	@Override
	public void dispose(){}
}
