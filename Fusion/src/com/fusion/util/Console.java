package com.fusion.util;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
	private enum LineType
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
	private FusionGame Game;

	// Were input is read from
	private TextField Input;

	// Font to be rendered
	private BitmapFont Font;

	// Data structure where the lines for the
	// console will be held
	private ArrayList<Line> Lines;
	
	// Allows the parsing of Strings
	private StringTokenizer sT;
	
	// Data managers
	private AssetGroupManager AssetManager;
	//private GameScreenManager ScreenManager;

	// Current information of the game to
	// be rendered
	private int FPS;
	private float RealResolutionWidth, RealResolutionHeight;
	private float VirtualResolutionWidth, VirtualResolutionHeight;

	public Console(FusionGame Game, TextFieldStyle Style, int xPos, int yPos)
	{
		Input = new TextField("",  Style);
		Input.setBounds(xPos, yPos, 400, 50);
		Input.setBlinkTime(0.3f);
		Input.setCursorPosition(0);
		Input.setVisible(false);

		Font = Style.font;

		this.Game = Game;
		this.Game.GetStage().addActor(Input);

		Lines = new ArrayList<Line>();
		
		AssetManager = AssetGroupManager.GetAssetGroupManager();
		//ScreenManager = GameScreenManager.GetScreenManager();
	}

	@Override
	public void act(float delta)
	{
		// Hide or unhide the console
		if (Gdx.input.isKeyJustPressed(Keys.BACKSLASH) || Gdx.input.isTouched(4))
		{
			if (!Input.isVisible()) Input.setVisible(true);
			else					Input.setVisible(false);

			Input.setText("");
		}

		// Insert text from user
		if (Gdx.input.isKeyJustPressed(Keys.ENTER) && Input.isVisible())
		{
			Add(Input.getText(), LineType.User);
			Input.setText("");
			
			// only allowing 10 lines to be rendered
			// at a time
			if (Lines.size() >= 10) Lines.remove(0);
		}

		// Get information about game / current screen
		if (Input.isVisible())
		{
			FPS = Gdx.graphics.getFramesPerSecond();
			RealResolutionWidth = Gdx.graphics.getWidth();
			RealResolutionHeight = Gdx.graphics.getHeight();
			VirtualResolutionWidth = Game.GetUICamera().viewportWidth;
			VirtualResolutionHeight = Game.GetUICamera().viewportHeight;
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{	
		// Don't render anything if it's not visible
		if (!Input.isVisible()) return;

		// Going through the Lines array list and render lines
		// in color depending on their line type
		for(int i = Lines.size()-1, y = 0; i != -1; i--, y += 20)
		{
			Line line = Lines.get(i);
			switch(line.Type)
			{
			case User:
				Font.setColor(Color.WHITE);
				break;
			case System:
				Font.setColor(Color.GREEN);
				break;
			case Error:
				Font.setColor(Color.RED);
				break;
			default:
				break;
			}
			Font.draw(batch, line.Message, Input.getX() + 10, (Input.getCenterY() + 40) + y);
		}

		// Render the game / screen information
		Font.setColor(Color.GREEN);
		Font.draw(batch, "FPS:" + FPS, Input.getX() + Input.getWidth(), Input.getCenterY() + 40);
		Font.draw(batch, "Device Resolution: " + RealResolutionWidth + "," + RealResolutionHeight, Input.getX() + Input.getWidth(), Input.getCenterY() + 60);
		Font.draw(batch, "Virtual Resolution: " + VirtualResolutionWidth + "," + VirtualResolutionHeight, Input.getX() + Input.getWidth(), Input.getCenterY() + 80);		
	}

	/**
	 * Add
	 * @param message - Message user/system/error is passing over
	 * @param type - Type of message it is
	 * 
	 * Creates a line and adds the message/type to it. Checks if
	 * it's a command and if it is then execute it
	 */
	private void Add(String message, LineType type)
	{
		Lines.add(new Line(message, type));

		if (type == LineType.User)	ExectureCommand(message);
	}

	/**
	 * ExecuteCommand
	 * @param line - Command to be executes
	 * @return If the command was successfully executed
	 * 
	 * Parses the recieved message and checks if it is
	 * a valid command or not
	 */
	private boolean ExectureCommand(String Message)
	{
		String command = Message;
		sT = new StringTokenizer(command + " ");
		switch(command)
		{
		case "version":
			Add("Version .001", LineType.System);
			return true;
		case "help":
			Add("Help message", LineType.System);
			return true;
		case "clear":
			Lines.clear();
			return true;
		case "quit":
			Gdx.app.exit();
			return true;
		}

		sT.nextToken();

		// LOADING AN XML
		if (command.startsWith("loadXML"))
		{
			if (!sT.hasMoreTokens())
			{
				Add("loadXML not used correctly", LineType.Error);
				return false;
			}
			String nextToken = sT.nextToken();

			if (!AssetManager.LoadAssetGroup(nextToken))
			{
				Add("Failed to load XML at location '" + nextToken + "'", LineType.Error);
				return false;
			}
			return true;
		}
		
		// UNLOADING AN XML
		if (command.startsWith("unloadXML"))
		{
			if (!sT.hasMoreTokens())
			{
				Add("loadXML not used correctly", LineType.Error);
				return false;
			}
			String nextToken = sT.nextToken();
			
			if (!AssetManager.UnloadAssetGroup(nextToken))
			{
				Add("Failed to unload XML at location '" + nextToken + "'", LineType.Error);
				return false;
			}
			return true;
		}
		
		Add("Command not recognized", LineType.Error);
		return false;
	}
}
