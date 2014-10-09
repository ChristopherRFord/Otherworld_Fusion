package com.otherworld.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.audio.Music;
import com.fusion.FusionGame;
import com.fusion.FusionScreen;
import com.otherworld.style.TextButton_Otherworld;

import static com.otherworld.OtherworldConstants.*;

public class StartScreen extends FusionScreen
{
	private Texture background;
	private Music music;
	
	private TextButton_Otherworld exploreButton;
	private TextButton_Otherworld exitButton;
	
	public StartScreen(int ID, FusionGame game)
	{
		super(ID, game);
	}

	@Override
	protected void Update(float delta)
	{
		
	}

	@Override
	protected void Render(float delta)
	{
		gameBatch.begin();
		gameBatch.draw(background, 0, 0,  gameCamera.viewportWidth, gameCamera.viewportHeight);
		gameBatch.end();
	}

	@Override
	public void Enter(ScreenSwitchState state)
	{
		assetManager.loadAssetGroup(ASSET_XML_PATH + "StartMenu.xml");
		
		background = assetManager.get(IMAGE_PATH + "Background.png", Texture.class);
		music = assetManager.get(MUSIC_PATH + "MenuTheme.mp3", Music.class);
		
		music.setLooping(true);
		//Music.play();
	}

	@Override
	public void Leave(ScreenSwitchState state)
	{
		//Music.stop();
		
		exploreButton.remove();
		exitButton.remove();
		
		assetManager.unloadAssetGroup(ASSET_XML_PATH + "StartMenu.xml");
	}

	@Override
	protected void InitGUI(int width, int height)
	{
		Vector2 buttonBounds = new Vector2(gameCamera.viewportWidth * .15f, gameCamera.viewportHeight * .1f);
		
		Vector2 exploreLocation = new Vector2(gameCamera.viewportWidth * .125f, gameCamera.viewportHeight * .165f);
		Vector2 exitLocation = new Vector2(exploreLocation.x + buttonBounds.x, exploreLocation.y);
		
		exploreButton = new TextButton_Otherworld("Explore", assetManager.get(IMAGE_PATH + "Menu.png", Texture.class), exploreLocation, buttonBounds);
		exploreButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button)
			{
				screenManager.setScreen(2, game);
			}
		});
		stage.addActor(exploreButton);
		
		exitButton = new TextButton_Otherworld("Exit", assetManager.get(IMAGE_PATH + "Menu.png", Texture.class), exitLocation, buttonBounds);
		stage.addActor(exitButton);
	}
}
