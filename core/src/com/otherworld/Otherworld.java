package com.otherworld;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.fusion.util.Console;
import com.fusion.FusionGame;
import com.otherworld.screens.StartScreen;
import com.otherworld.style.TextFieldStyle_Otherworld;

public class Otherworld extends FusionGame
{
	
	@Override
	public void Init()
	{
		AssetManager.LoadAssetGroup("xmls/CommonAssets.xml");

		ScreenManager.PutScreen(new StartScreen(1, this));
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
							200, 0);
		Stage.addActor(Console);
	}
	
	@Override
	public void Close()
	{
	}	
}
