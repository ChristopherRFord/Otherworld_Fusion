package com.otherworld;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.fusion.FusionGame;
import com.fusion.util.Console;

import static com.otherworld.OtherworldConstants.*;
import com.otherworld.screens.ExploreScreen;
import com.otherworld.screens.StartScreen;
import com.otherworld.style.TextFieldStyle_Otherworld;

public class Otherworld extends FusionGame
{
	
	@Override
	public void init()
	{
		assetManager.loadAssetGroup(ASSET_XML_PATH + "CommonAssets.xml");

		screenManager.putScreen(new StartScreen(1, this));
		screenManager.putScreen(new ExploreScreen(2, this));
	}

	@Override
	public void start()
	{
		screenManager.setScreen(1, this);
	}
	
	@Override
	public void resize(int width, int height)
	{
		super.resize(width, height);
		
		console = new Console(this, new TextFieldStyle_Otherworld(assetManager.get("images/Menu.png", Texture.class), new BitmapFont(), Color.WHITE),
				200, stage.getHeight());
		stage.addActor(console);
	}
	
	@Override
	public void close()
	{
	}	
}
