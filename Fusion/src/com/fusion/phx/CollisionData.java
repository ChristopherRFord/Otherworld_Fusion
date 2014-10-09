package com.fusion.phx;

import com.badlogic.ashley.core.Entity;

/**
 * CollisionData
 * @author Christopher
 *
 * This contains data that will be attached to a 
 * physics body
 */
public class CollisionData
{
	public Entity parent;
	public String tag;
	
	public CollisionData(Entity parent, String tag)
	{
		this.parent = parent;
		this.tag = tag;
	}
}
