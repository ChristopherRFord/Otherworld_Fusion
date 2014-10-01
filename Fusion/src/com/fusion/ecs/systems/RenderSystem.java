package com.fusion.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.fusion.ecs.components.PositionComponent;
import com.fusion.ecs.components.TextureComponent;

/**
 * RenderSystem
 * @author Christopher
 * 
 * Temp system for simply rendering any entity with a
 * texture and a component.
 */
public class RenderSystem extends EntitySystem
{
	private ImmutableArray<Entity> Entities;
	
	private ComponentMapper<PositionComponent> 	PM = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<TextureComponent>	TM = ComponentMapper.getFor(TextureComponent.class);
	
	private Batch Batch;
	 
	public RenderSystem(Batch Batch)
	{
		super(0);
		
		this.Batch = Batch;
	}
	
	@Override
	public void addedToEngine(Engine engine)
	{
		Entities = engine.getEntitiesFor(Family.getFor(PositionComponent.class, TextureComponent.class));
	}
	
	@Override
	public void update(float delta)
	{
		Batch.begin();
		for (int i = 0; i < Entities.size(); i++)
		{
			Entity Entity = Entities.get(i);
			PositionComponent PC = PM.get(Entity);
			TextureComponent TC = TM.get(Entity);
			
			Batch.draw(TC.Texture, PC.X, PC.Y);
		}
		Batch.end();
	}
}
