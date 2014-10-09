package com.fusion.util;

import java.util.ArrayList;
import java.util.StringTokenizer;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;

import com.fusion.FusionGame;

/**
 * DebugConsole
 * @author Christopher Ford
 * 
 * Allows the ability to enter commands for debugging.
 * Loading/Unloading assets, switching screens, load entities,
 * etc. Also renders information about the game and current
 * screen.
 */
public class Console extends Actor
{
	// Different types of lines that can be sent
	// to the console
	public enum LineType
	{
		User,
		System,
		Error
	}

	// A line sent to the console, holds the message
	// and what kind of line it is
	private class Line
	{
		protected String Message;
		protected LineType Type;

		protected Line(String Message, LineType Type)
		{
			this.Message = Message;
			this.Type = Type;
		}
	}

	// Game reference
	private FusionGame game;

	// Were input is read from
	private TextField input;

	// Font to be rendered
	private BitmapFont font;

	// Data structure where the lines for the
	// console will be held
	private ArrayList<Line> lines;

	// Allows the parsing of Strings
	private StringTokenizer stringTokenizer;

	private Stage stage;

	// Data managers
	private AssetGroupManager 		assetManager;
	private EngineManager 			entityManager;
	private GameScreenManager 		screenManager;
	protected PhysicsWorldManager 	physicsManager;

	// Current information of the game to
	// be rendered
	private int fps;
	private float realResolutionWidth, realResolutionHeight;
	private float virtualResolutionWidth, virtualResolutionHeight;
	private int numActors;
	private int currentScreenID;
	private int screenStackSize;
	private int numEntities;
	private int numBodies;

	public Console(FusionGame game, TextFieldStyle style, float xPos, float yPos)
	{
		input = new TextField("",  style);
		input.setBounds(xPos, yPos-50, 400, 50);
		input.setBlinkTime(0.3f);
		input.setCursorPosition(0);
		input.setVisible(false);

		font = style.font;

		this.game = game;
		stage = game.getStage();
		stage.addActor(input);

		lines = new ArrayList<Line>();

		assetManager = AssetGroupManager.getAssetGroupManager();
		entityManager = EngineManager.getEngineManager();
		screenManager = GameScreenManager.getScreenManager();
		physicsManager = PhysicsWorldManager.getPhysicsWorldManager();
	}

	public boolean IsActive(){	return input.isVisible();	}

	@Override
	public void act(float delta)
	{
		// Hide or unhide the console
		if (Gdx.input.isKeyJustPressed(Keys.BACKSLASH) || Gdx.input.isTouched(3))
		{
			if (!input.isVisible()) input.setVisible(true);
			else					input.setVisible(false);

			input.setText("");
		}

		// Insert text from user
		if (Gdx.input.isKeyJustPressed(Keys.ENTER) && input.isVisible() && !input.getText().equals(""))
		{
			add(input.getText(), LineType.User);
			input.setText("");

			// only allowing 10 lines to be rendered
			// at a time
			if (lines.size() >= 10) lines.remove(0);
		}

		// Get information about game / current screen
		if (input.isVisible())
		{
			fps = Gdx.graphics.getFramesPerSecond();
			realResolutionWidth = Gdx.graphics.getWidth();
			realResolutionHeight = Gdx.graphics.getHeight();
			virtualResolutionWidth = game.getGameCamera().viewportWidth;
			virtualResolutionHeight = game.getGameCamera().viewportHeight;
			numActors = stage.getActors().size;
			currentScreenID = screenManager.getCurrentScreen().getID();
			screenStackSize = screenManager.getScreenSize();
			numEntities = entityManager.getNumEntities();
			numBodies = physicsManager.getWorld().getBodyCount();
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{	
		// Don't render anything if it's not visible
		if (!input.isVisible()) return;

		// Going through the Lines array list and render lines
		// in color depending on their line type
		for(int i = lines.size()-1, y = 0; i != -1; i--, y += 20)
		{
			Line line = lines.get(i);
			switch(line.Type)
			{
			case User:
				font.setColor(Color.WHITE);
				break;
			case System:
				font.setColor(Color.GREEN);
				break;
			case Error:
				font.setColor(Color.RED);
				break;
			default:
				break;
			}
			font.draw(batch, line.Message, input.getX() + 10, (input.getY()) - y);
		}

		// Render the game / screen information
		font.setColor(Color.GREEN);
		font.draw(batch, "FPS:" + fps, input.getX() + input.getWidth(), input.getCenterY() - 20);
		font.draw(batch, "Device Resolution: " + realResolutionWidth + "," + realResolutionHeight, input.getX() + input.getWidth(), input.getCenterY() - 40);
		font.draw(batch, "Virtual Resolution: " + virtualResolutionWidth + "," + virtualResolutionHeight, input.getX() + input.getWidth(), input.getCenterY() - 60);
		font.draw(batch, "Number of Actors: " + numActors, input.getX() + input.getWidth(), input.getCenterY() - 80);
		font.draw(batch, "Current Screen ID: " + currentScreenID, input.getX() + input.getWidth(), input.getCenterY() - 100);
		font.draw(batch, "Screen Stack Size: " + screenStackSize, input.getX() + input.getWidth(), input.getCenterY() - 120);
		font.draw(batch, "Number of Entities: " + numEntities, input.getX() + input.getWidth(), input.getCenterY() - 140);
		font.draw(batch, "Number of Bodies: " + numBodies, input.getX() + input.getWidth(), input.getCenterY() - 160);
	}

	/**
	 * Add
	 * @param message - Message user/system/error is passing over
	 * @param type - Type of message it is
	 * 
	 * Creates a line and adds the message/type to it. Checks if
	 * it's a command and if it is then execute it
	 */
	public void add(String message, LineType type)
	{
		lines.add(new Line(message, type));

		if (type == LineType.User)	exectureCommand(message);
	}

	/**
	 * executeCommand
	 * @param line - Command to be executes
	 * @return If the command was successfully executed
	 * 
	 * Parses the recieved message and checks if it is
	 * a valid command or not
	 */
	private boolean exectureCommand(String Message)
	{
		stringTokenizer = new StringTokenizer(Message + " ");
		String command = stringTokenizer.nextToken();
		switch(command)
		{
		case "version":
			add("Version .001", LineType.System);
			return true;
		case "help":
			add("Help message", LineType.System);
			return true;
		case "clear":
			lines.clear();
			return true;
		case "clearEntities":
			entityManager.deleteAllEntities();
			return true;
		case "unloadMap":
			TiledMapLoader.unloadMap(game);
			return true;
		case "debug":
			game.toggleDebug();
			return true;
		case "quit":
			Gdx.app.exit();
			return true;
		}

		String nextToken;

		// LOADING AN ASSETGROUP
		if (command.equals("loadAssetGroup"))
		{
			if (!stringTokenizer.hasMoreTokens())
			{
				add("loadAssetGroup not used correctly - loadAssetGroup LOCATION_OF_XML", LineType.Error);
				return false;
			}
			nextToken = stringTokenizer.nextToken();

			if (!assetManager.loadAssetGroup(nextToken))
			{
				add("Failed to load XML at location '" + nextToken + "'", LineType.Error);
				return false;
			}
			return true;
		}

		// UNLOADING AN ASSETGROUP
		if (command.equals("unloadAssetGroup"))
		{
			if (!stringTokenizer.hasMoreTokens())
			{
				add("unloadAssetGroup not used correctly - unlaodAssetGroup LOCATION_OF_XML", LineType.Error);
				return false;
			}
			nextToken = stringTokenizer.nextToken();

			if (!assetManager.unloadAssetGroup(nextToken))
			{
				add("Failed to unload XML at location '" + nextToken + "'", LineType.Error);
				return false;
			}
			return true;
		}

		// LOADS AN ENTITY XML
		if (command.equals("loadEntity"))
		{
			if (!stringTokenizer.hasMoreTokens())
			{
				add("loadEntity not used correctly - loadEntity LOCATION_OF_XML", LineType.Error);
				return false;
			}

			nextToken = stringTokenizer.nextToken();

			if (!entityManager.loadEntity(nextToken))
			{
				add("Failed to load XML at location '" + nextToken + "'", LineType.Error);
				return false;
			}
			return true;
		}

		// LOADS A TMX MAP
		if (command.equals("loadMap"))
		{
			if (!stringTokenizer.hasMoreTokens())
			{
				add("loadMap not used correctly", LineType.Error);
				return false;
			}
			if (TiledMapLoader.currentMap != null)
			{
				add("A map is already loaded", LineType.Error);
				return false;
			}

			nextToken = stringTokenizer.nextToken();
			TiledMap Map = assetManager.get(nextToken, TiledMap.class);

			if (Map == null)
			{
				add("Failed to load TMX at location '" + nextToken + "'", LineType.Error);
				return false;
			}

			TiledMapLoader.loadMap(game, Map);
			return true;

		}

		// SWITCHING SCREENS
		if (command.equals("setScreen"))
		{
			if (!stringTokenizer.hasMoreTokens())
			{
				add("setScreen not used correctly", LineType.Error);
				return false;
			}

			try
			{
				int screen = Integer.parseInt(stringTokenizer.nextToken());
				if (!screenManager.setScreen(screen, game))
				{
					add("Screen " + screen + " does not exist", LineType.Error);
					return false;
				}
			}
			catch (NumberFormatException e)
			{
				add("Must pass a screen number to setScreen", LineType.Error);
				return false;
			}
			return true;
		}

		// SETTING SPEED
		if (command.equals("setGameSpeed"))
		{
			if (!stringTokenizer.hasMoreTokens())
			{
				add("setGameSpeed not used correctly", LineType.Error);
				return false;
			}

			try
			{
				int speed = Integer.parseInt(stringTokenizer.nextToken());
				PhysicsWorldManager.GAME_SPEED = speed;
			}
			catch (NumberFormatException e)
			{
				add("Must pass a speed number to setGameSpeed", LineType.Error);
				return false;
			}
			return true;
		}

		if (command.equals("setAmbientLight"))
		{
			if (!stringTokenizer.hasMoreTokens())
			{
				add("setAmbientLight not used correctly", LineType.Error);
				return false;
			}

			String color = stringTokenizer.nextToken();
			RayHandler LightWorld = game.getLightWorld();

			switch (color)
			{
			case "WHITE":
				LightWorld.setAmbientLight(Color.WHITE);
				break;
			case "BLACK":
				LightWorld.setAmbientLight(Color.BLACK);
				break;
			case "RED":
				LightWorld.setAmbientLight(Color.RED);
				break;
			case "BLUE":
				LightWorld.setAmbientLight(Color.BLUE);
				break;
			case "GREEN":
				LightWorld.setAmbientLight(Color.GREEN);
				break;
			default:
				add("Must pass a valid color to setAmbientLight", LineType.Error);
				return false;
			}
			return true;
		}

		add("Command not recognized", LineType.Error);
		return false;
	}
}
