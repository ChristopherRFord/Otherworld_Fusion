package com.fusion.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.math.Vector3;
import com.fusion.FusionGame;
import com.fusion.ecs.components.PhysicsComponent;
import com.fusion.gfx.VirtualViewportCamera;

import static com.fusion.util.PhysicsWorldManager.*;

public class DesktopInputSystem extends EntitySystem
{
	private ImmutableArray<Entity> Entities;
	
	private ComponentMapper<PhysicsComponent> PM = ComponentMapper.getFor(PhysicsComponent.class);
	
	private Vector2 Velocity;
	
	private boolean W, A, S, D;
	
	private FusionGame Game;
	private VirtualViewportCamera GameCamera;
	
	
	public DesktopInputSystem(FusionGame Game)
	{
		super(0);
		
		Velocity = new Vector2(0, 0);
		
		this.Game = Game;
		GameCamera = Game.GetGameCamera();
	}
	
	@Override
	public void addedToEngine(Engine engine)
	{
		Entities = engine.getEntitiesFor(Family.getFor(PhysicsComponent.class));
	}
	
	@Override
	public void update(float delta)
	{
		if (Game.GetConsole().IsActive())
		{
			return;
		}
		
		for (int i = 0; i < Entities.size(); i++)
		{
			GetInput();
			
			Entity Entity = Entities.get(i);
			PhysicsComponent PC = PM.get(Entity);
			
			Vector2 LinearVelocity = PC.Body.getLinearVelocity();
			
			if (W && LinearVelocity.y <= GAME_SPEED) 			Velocity.y = GAME_SPEED;
			else if (S && LinearVelocity.y >= -GAME_SPEED)		Velocity.y = -GAME_SPEED;
			else												Velocity.y = 0;
			
			if (D && LinearVelocity.x <= GAME_SPEED) 			Velocity.x = GAME_SPEED;
			else if (A && LinearVelocity.x >= -GAME_SPEED)		Velocity.x = -GAME_SPEED;
			else												Velocity.x = 0;
			
			PC.Body.setLinearVelocity(Velocity);	
			
			if (PC.OriginY > GameCamera.GetTopBoundry())
				GameCamera.translate(0, ((GAME_SPEED * SCALING_FACTOR) * TIME_STEP));
			if (PC.OriginY < GameCamera.GetBottomBoundry())
				GameCamera.translate(0, -((GAME_SPEED * SCALING_FACTOR) * TIME_STEP));
			
			if (PC.OriginX > GameCamera.GetRightBoundry())
				GameCamera.translate(((GAME_SPEED * SCALING_FACTOR) * TIME_STEP), 0);
			if (PC.OriginX < GameCamera.GetLeftBoundry())
				GameCamera.translate(-((GAME_SPEED * SCALING_FACTOR) * TIME_STEP), 0);
		}
	}
	
	private void GetInput()
	{
		if (Gdx.input.isKeyPressed(Keys.W)) 	W = true;
		else									W = false;
		
		if (Gdx.input.isKeyPressed(Keys.A))		A = true;
		else									A = false;
		
		if (Gdx.input.isKeyPressed(Keys.S)) 	S = true;
		else									S = false;
		
		if (Gdx.input.isKeyPressed(Keys.D))		D = true;
		else									D = false;
	}
}
