package com.fusion.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.fusion.gfx.VirtualViewport;
import com.fusion.util.GameScreenManager;

public class DebugConsole
{
	
	private Batch Batch;
	private BitmapFont Font;
	
	private VirtualViewport VirtualViewport;
	
	private GameScreenManager ScreenManager;
	
	private int FPS;
	private int CurrentScreen;
	private int ScreenStackSize;
	private int RealWidth, RealHeight;
	private float VirtualWidth, VirtualHeight;
	
	private float xLocation, yLocation;

	
	private float Offset;
	
	private boolean IsRenderable;
	
	public DebugConsole(Batch Batch)
	{
		this.Batch = Batch;
		
		Font = new BitmapFont();
		Font.setColor(Color.WHITE);
		Font.setScale(1.5f);
		Offset = Font.getXHeight() * 2;
		
		ScreenManager = GameScreenManager.GetScreenManager();
	}
	
	public void act()
	{
		FPS = Gdx.graphics.getFramesPerSecond();
		CurrentScreen =	ScreenManager.GetCurrentScreen().getID();
		ScreenStackSize = ScreenManager.GetScreenSize();
		RealWidth = Gdx.graphics.getWidth(); RealHeight = Gdx.graphics.getHeight();
		VirtualWidth = VirtualViewport.virtualWidth; VirtualHeight = VirtualViewport.virtualHeight;
		
		xLocation = VirtualWidth * .1f;
		yLocation = VirtualHeight * .9f;
	}
	
	public void draw()
	{
		Batch.begin();
		Font.draw(Batch, "FPS: " + FPS, xLocation, yLocation);
		Font.draw(Batch, "Screen ID: " + CurrentScreen, xLocation, yLocation - Offset);
		Font.draw(Batch, "ScreenStackSize: " + ScreenStackSize, xLocation, yLocation - (Offset*2));
		Font.draw(Batch, "RealResolution: " + RealWidth + "," + RealHeight, xLocation, yLocation - (Offset*3));
		Font.draw(Batch, "VirtualResolution: " + VirtualWidth + "," + VirtualHeight, xLocation, yLocation - (Offset*4));
		Batch.end();
	}
	
	public boolean 	getRenderable()		{	return IsRenderable;	}
	public void 	setRenderableFalse(){	IsRenderable = false;	}
	public void swapRenderable()
	{
		IsRenderable = !IsRenderable;
	}
	
	public void setColor(Color Color){	Font.setColor(Color);	}
	
	public void setVirtualViewport(VirtualViewport VirtualViewport)
	{
		this.VirtualViewport = VirtualViewport;
	}
}
