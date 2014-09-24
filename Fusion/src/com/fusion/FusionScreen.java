package com.fusion;

import com.badlogic.gdx.Screen;

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
	}
	
	public int getID(){	return ID;	}

	protected 	abstract void Update(float Delta);
	protected 	abstract void Render(float Delta);
	public 		abstract void Enter(ScreenSwitchState State);
	public 		abstract void Leave(ScreenSwitchState State);
	protected 	abstract void InitGUI();
	
	@Override
	public void render(float Delta)
	{
		
	}

	@Override
	public void resize(int Width, int Height)
	{
		
	}

	@Override
	public void show()
	{
		
	}

	@Override
	public void hide()
	{
		
	}

	@Override
	public void pause(){}

	@Override
	public void resume(){}

	@Override
	public void dispose(){}
}
