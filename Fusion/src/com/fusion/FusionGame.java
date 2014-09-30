package com.fusion;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import com.fusion.gfx.VirtualViewportFactory;
import com.fusion.gfx.VirtualViewport;
import com.fusion.gfx.VirtualViewportCamera;
import com.fusion.util.*;

/**
 * FusionGame
 * @author Christopher Ford
 *
 * The first entry point for the game. This class is used to
 * load common assets, create screens and just start the game
 * in general.
 */
public abstract class FusionGame extends Game
{
	protected AssetGroupManager AssetManager;
	protected GameScreenManager ScreenManager;
	
	protected VirtualViewportFactory VirtualViewportFactory;
	protected VirtualViewportCamera GameCamera;
	protected VirtualViewportCamera UICamera;
	
	protected Batch GameBatch;
	protected Batch UIBatch;
	
	protected Stage Stage;
	
	protected Console Console;
	
	private boolean Start;

	@Override
	public void create()
	{
		AssetManager = AssetGroupManager.GetAssetGroupManager();
		ScreenManager = GameScreenManager.GetScreenManager();
		
		VirtualViewportFactory = new VirtualViewportFactory(800, 480, 854, 600);
		GameCamera = new VirtualViewportCamera();
		UICamera = new VirtualViewportCamera();
		
		GameBatch = new SpriteBatch();
		UIBatch = new SpriteBatch();
		
		Start = false;
	}
	
	@Override
	public void resize(int width, int height)
	{
		VirtualViewport VirtualViewport = VirtualViewportFactory.getVirtualViewport(width, height);
		
		GameCamera.setVirtualViewport(VirtualViewport);
		GameCamera.updateViewport();
		GameCamera.position.set(VirtualViewport.getWidth()/2, VirtualViewport.getHeight()/2, 0);
		
		UICamera.setVirtualViewport(VirtualViewport);
		UICamera.updateViewport();
		UICamera.position.set(VirtualViewport.getWidth()/2, VirtualViewport.getHeight()/2, 0);
		
		if (Stage != null) Stage.dispose();
		
		Stage = new Stage(new StretchViewport(VirtualViewport.getWidth(), VirtualViewport.getHeight()), UIBatch);
		Gdx.input.setInputProcessor(Stage);
		
		super.resize(width, height);
		
		if (!Start)
		{
			Start = true;
			Init();
			Start();
		}
	}
	
	@Override
	public void render()
	{
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		GameBatch.setProjectionMatrix(GameCamera.projection);
		GameCamera.update();
		
		UIBatch.setProjectionMatrix(UICamera.projection);
		UICamera.update();
		
		Stage.act();
		Stage.draw();
		
		super.render();
	}
	
	@Override
	public void dispose()
	{
		AssetManager.dispose();
		
		Close();
	}
	
	public abstract void Init();
	public abstract void Start();
	public abstract void Close();
	
	public VirtualViewportCamera GetGameCamera()	{	return GameCamera;		}
	public VirtualViewportCamera GetUICamera()		{	return UICamera;		}
	
	public Batch GetGameBatch()						{	return GameBatch;		}
	public Batch GetUIBatch()						{	return UIBatch;			}
	
	public Stage GetStage()							{	return Stage;			}
	
	public Console GetConsole()						{	return Console;			}
}
