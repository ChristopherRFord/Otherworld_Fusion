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
	public void Init()
	{
		AssetManager.LoadAssetGroup(ASSET_XML_PATH + "CommonAssets.xml");

		ScreenManager.PutScreen(new StartScreen(1, this));
		ScreenManager.PutScreen(new ExploreScreen(2, this));
	}

	@Override
	public void Start()
	{
		ScreenManager.SetScreen(1, this);
	}
	
	@Override
	public void resize(int width, int height)
	{
		super.resize(width, height);
		
		Console = new Console(this, new TextFieldStyle_Otherworld(AssetManager.Get("images/Menu.png", Texture.class), new BitmapFont(), Color.WHITE),
				200, Stage.getHeight());
		Stage.addActor(Console);
	}
	
	@Override
	public void Close()
	{
	}	
}
