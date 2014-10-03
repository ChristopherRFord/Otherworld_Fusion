package com.fusion.gfx;

import java.util.ArrayList;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * TiledMap_Fusion
 * @author Christopher
 * 
 * Based on LIBGDX's TiledMap. Has an array which stores
 * the physic bodies for collision tiles for quick loading
 * and unloading in to the physics world.
 */
public class TiledMap_Fusion
{
	private TiledMap Map;
	private ArrayList<Body> BodyList;
	
	public TiledMap_Fusion(TiledMap Map)
	{
		this.Map = Map;
		BodyList = new ArrayList<Body>();
	}
	
	public TiledMap			GetTiledMap(){	return Map;			}
	public ArrayList<Body> 	GetBodyList(){	return BodyList;	}
}
