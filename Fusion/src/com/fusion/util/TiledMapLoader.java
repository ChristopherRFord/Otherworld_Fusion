package com.fusion.util;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import com.fusion.FusionGame;
import com.fusion.gfx.TiledMap_Fusion;
import com.fusion.phx.CollisionData;

/**
 * TiledMapLoader
 * @author Christopher Ford
 *
 * A support loader for TiledMaps. Supplies quick loading and unloading
 * of TiledMaps for physics and entities.
 */
public class TiledMapLoader
{	
	public static TiledMap_Fusion currentMap;
	
	public static void loadMap(FusionGame game, TiledMap map)
	{
		currentMap = new TiledMap_Fusion(map);
		
		OrthogonalTiledMapRenderer MapRenderer = game.getTiledMapRenderer();
		MapRenderer.setMap(currentMap.getTiledMap());
		
		createCollision(currentMap);
	}
	
	private static void createCollision(TiledMap_Fusion map)
	{
		int SCALING_FACTOR = PhysicsWorldManager.SCALING_FACTOR;
		World world = PhysicsWorldManager.getPhysicsWorldManager().getWorld();

		TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getTiledMap().getLayers().get(0);
		float tileWidth = collisionLayer.getTileWidth();
		float tileHeight = collisionLayer.getTileHeight();
		float tilesWidth = collisionLayer.getWidth();
		float tilesHeight = collisionLayer.getHeight();

		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(0, 0);
		bodyDef.type = BodyType.StaticBody;

		Body body = world.createBody(bodyDef);
		body.setFixedRotation(false);
		body.setUserData(new CollisionData(null, "TileCollision"));
		map.SetMapBody(body);


		//Loop to cycle through the width of the TMX
		for (int w = 0; w < tilesWidth; w++)
		{
			//Loop to cycle through the height of the TMX
			for (int h = 0; h < tilesHeight; h++)
			{
				//Is there a tile in the collision layer?
				if (collisionLayer.getCell(w, h) != null)
				{
					PolygonShape shape = new PolygonShape();
					shape.setAsBox(tileWidth/(2*SCALING_FACTOR), tileHeight/(2*SCALING_FACTOR),
					new Vector2((w*tileWidth/SCALING_FACTOR) + ((tileWidth/SCALING_FACTOR)/2), ((h*tileHeight)/SCALING_FACTOR) + ((tileHeight/SCALING_FACTOR)/2)), 0);
					
					FixtureDef fixtureDef = new FixtureDef();
					fixtureDef.shape = shape;
					fixtureDef.friction = 0;
					fixtureDef.density = 0;
					
					body.createFixture(fixtureDef);
					shape.dispose();
				}
			}
		}
	}
	
	public static void unloadMap(FusionGame game)
	{
		//Sets the map renderer to null
		game.getTiledMapRenderer().setMap(null);
		
		PhysicsWorldManager.getPhysicsWorldManager().delete(currentMap.getMapBody());
		
		currentMap = null;
	}
}
