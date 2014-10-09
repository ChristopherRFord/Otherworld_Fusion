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

import com.fusion.ecs.components.AnimationComponent;
import com.fusion.ecs.components.InputComponent;
import com.fusion.ecs.components.PhysicsComponent;
import com.fusion.ecs.components.TextureComponent;
import com.fusion.FusionGame;
import com.fusion.gfx.VirtualViewportCamera;

import static com.fusion.util.PhysicsWorldManager.*;

/**
 * DesktopInputSystem
 * @author Christopher Ford
 * 
 * Handles input and movement for an entity with an input component.
 */
public class DesktopInputSystem extends EntitySystem
{
	private ImmutableArray<Entity> entities;
	private ComponentMapper<PhysicsComponent> PM = ComponentMapper.getFor(PhysicsComponent.class);
	private ComponentMapper<AnimationComponent> AM = ComponentMapper.getFor(AnimationComponent.class);
	private ComponentMapper<TextureComponent> TM = ComponentMapper.getFor(TextureComponent.class);

	private boolean W, A, S, D;
	private Vector2 velocity;

	private FusionGame game;
	private VirtualViewportCamera gameCamera;
	
	// Different directions an entity can have
	enum Direction
	{
		UP,
		DOWN,
		LEFT,
		RIGHT
	}

	public DesktopInputSystem(FusionGame game)
	{
		super(0);

		velocity = new Vector2(0, 0);

		this.game = game;
		gameCamera = game.getGameCamera();

	}

	@Override
	public void addedToEngine(Engine engine)
	{
		entities = engine.getEntitiesFor(Family.getFor(InputComponent.class));

	}

	@Override
	public void update(float delta)
	{
		if (game.getConsole().IsActive())
			return;

		for (int i = 0; i < entities.size(); i++)
		{
			getInput();

			// Receive entity and components
			Entity entity = entities.get(i);
			PhysicsComponent PC = PM.get(entity);
			AnimationComponent AC = AM.get(entity);
			TextureComponent TC = TM.get(entity);

			AC.animate = false;

			// MOVING RIGHT
			if (D)
			{
				if (!(PC.normals.x < 0))
				{
					AC.animate = true;
					velocity.x = GAME_SPEED;
					setDirection(Direction.RIGHT, AC, TC);
				}
			}
			// MOVING LEFT
			else if (A)
			{
				if (!(PC.normals.x > 0))
				{
					AC.animate = true;
					velocity.x = -GAME_SPEED;
					setDirection(Direction.LEFT, AC, TC);
				}
			}
			else
				velocity.x = 0;

			// MOVING UP
			if (W)
			{
				if (!(PC.normals.y < 0))
				{
					AC.animate = true;
					velocity.y = GAME_SPEED;
					setDirection(Direction.UP, AC, TC);
				}
			}
			// MOVING DOWN
			else if (S)
			{
				if (!(PC.normals.y > 0))
				{
					AC.animate = true;
					velocity.y = -GAME_SPEED;
					setDirection(Direction.DOWN, AC, TC);
				}
			}
			else
				velocity.y = 0;

			PC.normals.set(0, 0);

			// APPLYING THE RECORDED VELOCITY TO THE ENTITIES BDOY
			PC.body.setLinearVelocity(velocity);	

			// MOVING THE CAMERA BASED ON THE LOCATION OF THE ENTITY
			if (PC.originY > gameCamera.GetTopBoundry())
				gameCamera.translate(0, ((GAME_SPEED * SCALING_FACTOR) * TIME_STEP));
			if (PC.originY < gameCamera.GetBottomBoundry())
				gameCamera.translate(0, -((GAME_SPEED * SCALING_FACTOR) * TIME_STEP));

			if (PC.originX > gameCamera.GetRightBoundry())
				gameCamera.translate(((GAME_SPEED * SCALING_FACTOR) * TIME_STEP), 0);
			if (PC.originX < gameCamera.GetLeftBoundry())
				gameCamera.translate(-((GAME_SPEED * SCALING_FACTOR) * TIME_STEP), 0);
		}
	}

	private void getInput()
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

	private void setDirection(Direction nDirection, AnimationComponent AC, TextureComponent TC)
	{
		if (nDirection.equals(Direction.UP))
		{
			AC.setAnimation(Direction.UP.toString());
			TC.setTexture(Direction.UP.toString());
		}
		else if (nDirection.equals(Direction.DOWN))
		{
			AC.setAnimation(Direction.DOWN.toString());
			TC.setTexture(Direction.DOWN.toString());
		}
		else if (nDirection.equals(Direction.RIGHT))
		{
			AC.setAnimation(Direction.RIGHT.toString());
			TC.setTexture(Direction.RIGHT.toString());
		}
		else if (nDirection.equals(Direction.LEFT))
		{
			AC.setAnimation(Direction.LEFT.toString());
			TC.setTexture(Direction.LEFT.toString());
		}
	}
}
