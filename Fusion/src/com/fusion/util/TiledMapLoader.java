package com.fusion.util;

import java.util.ArrayList;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.fusion.FusionGame;
import com.fusion.gfx.TiledMap_Fusion;

/**
 * TiledMapLoader
 * @author Christopher Ford
 *
 * A support loader for TiledMaps. Supplies quick loading and unloading
 * of TiledMaps for physics and entities.
 */
public class TiledMapLoader
{	
	public static TiledMap_Fusion CurrentMap;
	
	public static void LoadMap(FusionGame Game, TiledMap Map)
	{
		CurrentMap = new TiledMap_Fusion(Map);
		
		OrthogonalTiledMapRenderer MapRenderer = Game.GetTiledMapRenderer();
		MapRenderer.setMap(CurrentMap.GetTiledMap());
		
		CreateCollision(CurrentMap);
	}
	
	private static void CreateCollision(TiledMap_Fusion Map)
	{
		int SCALING_FACTOR = PhysicsWorldManager.SCALING_FACTOR;
		World World = PhysicsWorldManager.GetPhysicsWorldManager().GetWorld();

		TiledMapTileLayer CollisionLayer = (TiledMapTileLayer) Map.GetTiledMap().getLayers().get(0);
		float TileWidth = CollisionLayer.getTileWidth();
		float TileHeight = CollisionLayer.getTileHeight();
		float TilesWidth = CollisionLayer.getWidth();
		float TilesHeight = CollisionLayer.getHeight();

		BodyDef BodyDef = new BodyDef();
		BodyDef.type = BodyType.StaticBody;

		PolygonShape Shape = new PolygonShape();
		Shape.setAsBox(TileWidth/(2*SCALING_FACTOR), TileHeight/(2*SCALING_FACTOR));

		FixtureDef FixtureDef = new FixtureDef();
		FixtureDef.shape = Shape;
		FixtureDef.friction = 0;


		//Loop to cycle through the width of the TMX
		for (int w = 0; w < TilesWidth; w++)
		{
			//Loop to cycle through the height of the TMX
			for (int h = 0; h < TilesHeight; h++)
			{
				//Is there a tile in the collision layer?
				if (CollisionLayer.getCell(w, h) != null)
				{
					//If there is, create the collision and add it to the World and bodyList
					BodyDef.position.set(((w*TileWidth)/SCALING_FACTOR) + ((TileWidth/SCALING_FACTOR)/2),
											((h*TileHeight)/SCALING_FACTOR) + ((TileHeight/SCALING_FACTOR)/2));
					Body TileBody = World.createBody(BodyDef);
					TileBody.createFixture(FixtureDef);
					
					Map.GetBodyList().add(TileBody);
				}
			}
		}

		//Dispose the shape for memory
		Shape.dispose();
	}
	
	public static void UnloadMap(FusionGame Game)
	{
		//Sets the map renderer to null
		Game.GetTiledMapRenderer().setMap(null);
		
		ArrayList<Body> BodyList= CurrentMap.GetBodyList();
		
		PhysicsWorldManager PhysicsManager = PhysicsWorldManager.GetPhysicsWorldManager();

		//Take all of the bodies out of the world
		for (int i = 0; i < BodyList.size(); i++)		PhysicsManager.Delete(BodyList.get(i));
		

		//Clear the lists
		BodyList.clear();
		
		CurrentMap = null;
	}
}
