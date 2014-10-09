package com.fusion.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.fusion.ecs.components.AnimationComponent;
import com.fusion.ecs.components.PhysicsComponent;
import com.fusion.ecs.components.TextureComponent;


/**
 * RenderSystem
 * @author Christopher
 * 
 * System for rendering textures and animations
 * from an entity
 */
public class RenderSystem extends EntitySystem
{
	private ImmutableArray<Entity> entities;
	
	private ComponentMapper<PhysicsComponent> 	PM = ComponentMapper.getFor(PhysicsComponent.class);
	private ComponentMapper<TextureComponent>	TM = ComponentMapper.getFor(TextureComponent.class);
	private ComponentMapper<AnimationComponent> AM = ComponentMapper.getFor(AnimationComponent.class);
	
	private float elapsedTime;
	
	private Batch batch;
	 
	public RenderSystem(Batch batch)
	{
		super(0);
		
		this.batch = batch;
	}
	
	@Override
	public void addedToEngine(Engine engine)
	{
		entities = engine.getEntitiesFor(Family.getFor(PhysicsComponent.class, TextureComponent.class));
	}
	
	@Override
	public void update(float delta)
	{
		elapsedTime += Gdx.graphics.getDeltaTime();
		batch.begin();
		for (int i = 0; i < entities.size(); i++)
		{
			Entity entity = entities.get(i);
			PhysicsComponent PC = PM.get(entity);
			TextureComponent TC = TM.get(entity);
			AnimationComponent AC = AM.get(entity);
			
			if (AC != null && AC.animate)
			{
				batch.draw(AC.currentAnimation.getKeyFrame(elapsedTime, true), PC.actualX, PC.actualY);
			}
			else
				batch.draw(TC.currentTexture, PC.actualX, PC.actualY);
		}
		batch.end();
	}
}
