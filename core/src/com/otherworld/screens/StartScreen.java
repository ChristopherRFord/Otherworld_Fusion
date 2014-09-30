package com.otherworld.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.audio.Music;

import com.fusion.FusionGame;
import com.fusion.FusionScreen;

import com.otherworld.style.TextButton_Otherworld;
import static com.otherworld.OtherworldConstants.*;

public class StartScreen extends FusionScreen
{
	private Texture Background;
	private Music Music;
	
	private TextButton_Otherworld PlayButton;
	
	public StartScreen(int ID, FusionGame Game)
	{
		super(ID, Game);
	}

	@Override
	protected void Update(float Delta)
	{
		
	}

	@Override
	protected void Render(float Delta)
	{
		GameBatch.draw(Background, 0, 0,  GameCamera.viewportWidth, GameCamera.viewportHeight);
	}

	@Override
	public void Enter(ScreenSwitchState State)
	{
		AssetManager.LoadAssetGroup(XML_PATH + "StartMenu.xml");
		
		Background = AssetManager.Get(IMAGE_PATH + "Background.png", Texture.class);
		Music = AssetManager.Get(MUSIC_PATH + "MenuTheme.mp3", Music.class);
		
		Music.play();
	}

	@Override
	public void Leave(ScreenSwitchState State)
	{
		Music.stop();
		
		PlayButton.remove();
		
		AssetManager.UnloadAssetGroup(XML_PATH + "StartMenu.xml");
	}

	@Override
	protected void InitGUI(int Width, int Height)
	{
		Vector2 ButtonBounds = new Vector2(120, 60);
		
		Vector2 PlayLocation = new Vector2(100, 100);
		
		PlayButton = new TextButton_Otherworld("Play", AssetManager.Get(IMAGE_PATH + "Menu.png", Texture.class), PlayLocation, ButtonBounds);
		Stage.addActor(PlayButton);
	}

}
