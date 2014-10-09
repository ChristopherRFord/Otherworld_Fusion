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
	
	private HashMap<Integer, FusionScreen> screenMap;
	private Stack<FusionScreen> currentScreen;
	
	private GameScreenManager()
	{
		screenMap = new HashMap<Integer, FusionScreen>();
		currentScreen = new Stack<FusionScreen>();
	}

	/**
	 * getScreenManager
	 * @return - The singleton GameScreenManager variable
	 * 
	 * The first time this function is called the singleton object
	 * is privately constructed and the returned
	 */
	public static GameScreenManager getScreenManager()
	{
		if (singleton == null)
		{
			singleton = new GameScreenManager();
		}
		
		return singleton;
	}
	
	// ------------------------
	
	/**
	 * putScreen
	 * @param newScreen - Screen to be added to the hash map
	 * @return a boolean value, true if the screen is added, false otherwise
	 * 
	 * Adds a new screen into the hash map
	 */
	public boolean putScreen(FusionScreen newScreen)
	{
		if (screenMap.get(newScreen.getID()) != null) return false;
		else
		{
			screenMap.put(newScreen.getID(), newScreen);
			return true;
		}
	}
	
	public FusionScreen getScreen(int ID){	return screenMap.get(ID);	}
	// ------------------------
	
	/**
	 * setScreen
	 * @param ID - ID of the new current screen
	 * @param game - Game that allows us to set the screen
	 * 
	 * Clears the stack because we only want to use that for menu
	 * and battle screen switching. Pushes the screen on the stack
	 * and makes that the current screen
	 */
	public boolean setScreen(int ID, Game game)
	{	
		if (screenMap.get(ID) == null) return false;
		
		while(!currentScreen.isEmpty())
		{
			Gdx.app.log("-Leaving Screen", currentScreen.peek().getID() + "");
			currentScreen.pop().Leave(ScreenSwitchState.SET_SCREEN);
		}
		
		currentScreen.push(screenMap.get(ID));
		Gdx.app.log("-Entering Screen", ID + "");
		screenMap.get(ID).Enter(ScreenSwitchState.SET_SCREEN);
		game.setScreen(currentScreen.peek());
		
		return true;
	}

	/**
	 * pushSetScreen
	 * @param ID - ID of the new current screen
	 * @param game - Game that allows us to set the screen
	 * 
	 * Pushes the new current screen to the stack and
	 * makes that the current screen
	 */
	public void pushSetScreen(int ID, Game game)
	{
		if (!currentScreen.isEmpty())
		{
			int oldID = currentScreen.peek().getID();
			Gdx.app.log("-Leaving Screen", oldID + "");
			screenMap.get(oldID).Leave(ScreenSwitchState.PUSH_SCREEN);
		}
		
		currentScreen.push(screenMap.get(ID));
		
		Gdx.app.log("-Entering Screen", ID + "");
		screenMap.get(ID).Enter(ScreenSwitchState.PUSH_SCREEN);
		game.setScreen(currentScreen.peek());
	}
	
	/**
	 * popSetScreen
	 * @param game - Game that allows us to set the screen
	 * 
	 * Checks if there is a screen under the current screen on the stack.
	 * If there isn't then return and do nothing, if there is then pop
	 * the current screen and make the new peek the current screen
	 */
	public void popSetScreen(Game game)
	{
		if (currentScreen.size() <= 1) return;
		
		int oldID = currentScreen.peek().getID();
		
		Gdx.app.log("-Leaving Screen", oldID + "");
		screenMap.get(oldID).Leave(ScreenSwitchState.POP_SCREEN);
		currentScreen.pop();
		
		int newID = currentScreen.peek().getID();
		
		Gdx.app.log("-Entering Screen", newID  + "");
		screenMap.get(newID).Enter(ScreenSwitchState.POP_SCREEN);
		game.setScreen(currentScreen.peek());
	}
	
	public FusionScreen getCurrentScreen()	{	return currentScreen.peek();	}
	public int getScreenSize()				{	return currentScreen.size();	}
}

