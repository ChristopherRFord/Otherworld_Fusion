package com.fusion.util;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.fusion.FusionScreen;
import com.fusion.FusionScreen.ScreenSwitchState;

import java.util.HashMap;
import java.util.Stack;


/**
 * GameScreenmanager
 * @author Christopher Ford
 *
 * A manager that stores all of the states in the game and
 * controls the functionality of switching between the.
 * Uses a stack to quick switch between states. GameScreenManager
 * implements the Singleton design pattern and can be accessed
 * from anywhere in the program.
 */
public class GameScreenManager
{
	private static GameScreenManager singleton = null;
	
	private HashMap<Integer, FusionScreen> ScreenMap;
	private Stack<FusionScreen> CurrentScreen;
	
	private GameScreenManager()
	{
		ScreenMap = new HashMap<Integer, FusionScreen>();
		CurrentScreen = new Stack<FusionScreen>();
	}

	/**
	 * GetScreenManager
	 * @return - The singleton GameScreenManager variable
	 * 
	 * The first time this function is called the singleton object
	 * is privately constructed and the returned
	 */
	public static GameScreenManager GetScreenManager()
	{
		if (singleton == null)
		{
			singleton = new GameScreenManager();
		}
		
		return singleton;
	}
	
	// ------------------------
	
	/**
	 * PutScreen
	 * @param NewScreen - Screen to be added to the hash map
	 * @return a boolean value, true if the screen is added, false otherwise
	 * 
	 * Adds a new screen into the hash map
	 */
	public boolean PutScreen(FusionScreen NewScreen)
	{
		if (ScreenMap.get(NewScreen.getID()) != null) return false;
		else
		{
			ScreenMap.put(NewScreen.getID(), NewScreen);
			return true;
		}
	}
	
	public FusionScreen GetScreen(int ID){	return ScreenMap.get(ID);	}
	// ------------------------
	
	/**
	 * SetScreen
	 * @param ID - ID of the new current screen
	 * @param Game - Game that allows us to set the screen
	 * 
	 * Clears the stack because we only want to use that for menu
	 * and battle screen switching. Pushes the screen on the stack
	 * and makes that the current screen
	 */
	public boolean SetScreen(int ID, Game Game)
	{	
		if (ScreenMap.get(ID) == null) return false;
		
		while(!CurrentScreen.isEmpty())
		{
			Gdx.app.log("-Leaving Screen", CurrentScreen.peek().getID() + "");
			CurrentScreen.pop().Leave(ScreenSwitchState.SET_SCREEN);
		}
		
		CurrentScreen.push(ScreenMap.get(ID));
		
		Gdx.app.log("-Entering Screen", ID + "");
		ScreenMap.get(ID).Enter(ScreenSwitchState.SET_SCREEN);
		Game.setScreen(CurrentScreen.peek());
		
		return true;
	}

	/**
	 * PushSetScreen
	 * @param ID - ID of the new current screen
	 * @param Game - Game that allows us to set the screen
	 * 
	 * Pushes the new current screen to the stack and
	 * makes that the current screen
	 */
	public void PushSetScreen(int ID, Game Game)
	{
		if (!CurrentScreen.isEmpty())
		{
			int oldID = CurrentScreen.peek().getID();
			Gdx.app.log("-Leaving Screen", oldID + "");
			ScreenMap.get(oldID).Leave(ScreenSwitchState.PUSH_SCREEN);
		}
		
		CurrentScreen.push(ScreenMap.get(ID));
		
		Gdx.app.log("-Entering Screen", ID + "");
		ScreenMap.get(ID).Enter(ScreenSwitchState.PUSH_SCREEN);
		Game.setScreen(CurrentScreen.peek());
	}
	
	/**
	 * PopSetScreen
	 * @param Game - Game that allows us to set the screen
	 * 
	 * Checks if there is a screen under the current screen on the stack.
	 * If there isn't then return and do nothing, if there is then pop
	 * the current screen and make the new peek the current screen
	 */
	public void PopSetScreen(Game Game)
	{
		if (CurrentScreen.size() <= 1) return;
		
		int oldID = CurrentScreen.peek().getID();
		
		Gdx.app.log("-Leaving Screen", oldID + "");
		ScreenMap.get(oldID).Leave(ScreenSwitchState.POP_SCREEN);
		CurrentScreen.pop();
		
		int newID = CurrentScreen.peek().getID();
		
		Gdx.app.log("-Entering Screen", newID  + "");
		ScreenMap.get(newID).Enter(ScreenSwitchState.POP_SCREEN);
		Game.setScreen(CurrentScreen.peek());
	}
	
	public FusionScreen GetCurrentScreen()	{	return CurrentScreen.peek();	}
	public int GetScreenSize()				{	return CurrentScreen.size();	}
}

