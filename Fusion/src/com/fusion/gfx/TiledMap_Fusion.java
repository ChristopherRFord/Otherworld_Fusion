package com.fusion.gfx;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * TiledMap_Fusion
 * @author Christopher Ford
 * 
 * Based on LIBGDX's TiledMap. Has an array which stores
 * the physic bodies for collision tiles for quick loading
 * and unloading in to the physics world.
 */
public class TiledMap_Fusion
{
	private TiledMap map;
	private Body mapBody;
	
	public TiledMap_Fusion(TiledMap map)
	{
		this.map = map;
	}
	
	public TiledMap			getTiledMap()				{	return map;					}
	public Body			 	getMapBody()				{	return mapBody;				}
	public void				SetMapBody(Body mapBody)	{	this.mapBody = mapBody;		}
}
