package com.otherworld;

import com.fusion.FusionGame;
import com.otherworld.screens.StartScreen;

public class Otherworld extends FusionGame
{
	@Override
	public void Init()
	{
		ScreenManager.PutScreen(new StartScreen(1, this));
	}

	@Override
	public void Start()
	{
		ScreenManager.SetScreen(1, this);
	}
	
	@Override
	public void Close()
	{
		
	}
}
