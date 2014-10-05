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
	private Texture Background;
	private Music Music;
	
	private TextButton_Otherworld ExploreButton;
	private TextButton_Otherworld ExitButton;
	
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
		GameBatch.begin();
		GameBatch.draw(Background, 0, 0,  GameCamera.viewportWidth, GameCamera.viewportHeight);
		GameBatch.end();
	}

	@Override
	public void Enter(ScreenSwitchState State)
	{
		AssetManager.LoadAssetGroup(ASSET_XML_PATH + "StartMenu.xml");
		
		Background = AssetManager.Get(IMAGE_PATH + "Background.png", Texture.class);
		Music = AssetManager.Get(MUSIC_PATH + "MenuTheme.mp3", Music.class);
		
		Music.setLooping(true);
		Music.play();
	}

	@Override
	public void Leave(ScreenSwitchState State)
	{
		Music.stop();
		
		ExploreButton.remove();
		ExitButton.remove();
		
		AssetManager.UnloadAssetGroup(ASSET_XML_PATH + "StartMenu.xml");
	}

	@Override
	protected void InitGUI(int Width, int Height)
	{
		Vector2 ButtonBounds = new Vector2(120, 60);
		
		Vector2 ExploreLocation = new Vector2(100, 100);
		Vector2 ExitLocation = new Vector2(ExploreLocation.x + ButtonBounds.x, ExploreLocation.y);
		
		ExploreButton = new TextButton_Otherworld("Explore", AssetManager.Get(IMAGE_PATH + "Menu.png", Texture.class), ExploreLocation, ButtonBounds);
		ExploreButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button)
			{
				
				ScreenManager.SetScreen(2, Game);
			}
		});
		Stage.addActor(ExploreButton);
		
		ExitButton = new TextButton_Otherworld("Exit", AssetManager.Get(IMAGE_PATH + "Menu.png", Texture.class), ExitLocation, ButtonBounds);
		Stage.addActor(ExitButton);
	}
}
