package com.fusion;

import com.badlogic.gdx.Game;

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

	@Override
	public void create()
	{
		
		Init();
	}
	
	// Initialize the game
	public abstract void Init();
	
	// Pushing the game into the first screen
	public abstract void Start();
}
