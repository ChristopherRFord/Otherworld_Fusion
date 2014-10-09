package com.otherworld.screens;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.fusion.FusionGame;
import com.fusion.FusionScreen;
import com.fusion.util.TiledMapLoader;

import static com.otherworld.OtherworldConstants.*;

public class ExploreScreen extends FusionScreen
{
	
	public ExploreScreen(int ID, FusionGame Game)
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
		renderMap();
	}

	@Override
	public void Enter(ScreenSwitchState State)
	{
		switch (State)
		{
		case SET_SCREEN:
			assetManager.loadAssetGroup(ASSET_XML_PATH + "Explore.xml");
			TiledMapLoader.loadMap(game, assetManager.get(TMX_PATH + "Explore.tmx", TiledMap.class));
			entityManager.loadEntity("entity_xmls/Player.xml");
			break;
		case PUSH_SCREEN:
			break;
		case POP_SCREEN:
			break;
		default:
			break;
		}
	}

	@Override
	public void Leave(ScreenSwitchState State)
	{
		switch(State)
		{
		case SET_SCREEN:
			entityManager.deleteAllEntities();
			TiledMapLoader.unloadMap(game);
			assetManager.unloadAssetGroup(ASSET_XML_PATH + "Explore.xml");
			break;
		case PUSH_SCREEN:
			break;
		case POP_SCREEN:
			break;
		}
	}

	@Override
	protected void InitGUI(int Width, int Height)
	{
		
	}
}
