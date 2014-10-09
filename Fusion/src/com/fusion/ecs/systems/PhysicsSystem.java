package com.fusion.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import com.fusion.ecs.components.PhysicsComponent;

/**
 * PhysicsSystem
 * @author ChristopherFord
 *
 * System for updating entities with a physics body
 */
public class PhysicsSystem extends EntitySystem
{
	private ImmutableArray<Entity> entities;
	
	private ComponentMapper<PhysicsComponent> PM = ComponentMapper.getFor(PhysicsComponent.class);
	 
	public PhysicsSystem()
	{
		super(0);
	}
	
	@Override
	public void addedToEngine(Engine engine)
	{
		entities = engine.getEntitiesFor(Family.getFor(PhysicsComponent.class));
	}
	
	@Override
	public void update(float delta)
	{
		for (int i = 0; i < entities.size(); i++)
		{
			Entity entity = entities.get(i);
			PhysicsComponent PC = PM.get(entity);
			
			PC.move();
		}
	}
}
