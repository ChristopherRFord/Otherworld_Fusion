package com.fusion.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import com.fusion.ecs.components.PhysicsComponent;

public class PhysicsSystem extends EntitySystem
{
	private ImmutableArray<Entity> Entities;
	
	private ComponentMapper<PhysicsComponent> PM = ComponentMapper.getFor(PhysicsComponent.class);
	
	 
	public PhysicsSystem()
	{
		super(0);
	}
	
	@Override
	public void addedToEngine(Engine engine)
	{
		Entities = engine.getEntitiesFor(Family.getFor(PhysicsComponent.class));
	}
	
	@Override
	public void update(float delta)
	{
		for (int i = 0; i < Entities.size(); i++)
		{
			Entity Entity = Entities.get(i);
			PhysicsComponent PC = PM.get(Entity);
			
			
			PC.Move();
		}
	}
}
