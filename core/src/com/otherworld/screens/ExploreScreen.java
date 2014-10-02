package com.otherworld.screens;

import com.badlogic.gdx.maps.tiled.TiledMap;

import com.fusion.FusionGame;
import com.fusion.FusionScreen;

import static com.otherworld.OtherworldConstants.*;

public class ExploreScreen extends FusionScreen
{
	private TiledMap Map;
	
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
		RenderMap(Map);
	}

	@Override
	public void Enter(ScreenSwitchState State)
	{
		switch (State)
		{
		case SET_SCREEN:
			AssetManager.LoadAssetGroup(ASSET_XML_PATH + "Explore.xml");
			Map = AssetManager.Get(TMX_PATH + "Explore.tmx", TiledMap.class);
			TiledMapRenderer.setMap(Map);
			EntityManager.LoadEntity("entity_xmls/Player.xml");
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
			Map.dispose();
			AssetManager.UnloadAssetGroup(ASSET_XML_PATH + "Explore.xml");
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
