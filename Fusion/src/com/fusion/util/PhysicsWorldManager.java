package com.fusion.util;

import java.util.ArrayList;

import box2dLight.RayHandler;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * PhysicsWorldManager
 * @author Christopher Ford
 * 
 * A manager that handles storing, adding and deletion
 * of physics body to the world. Also holds modifiable
 * static information such as the scaling factor from
 * physics to the actual world, the speed of the game
 * and the time step that the world takes.
 */
public class PhysicsWorldManager
{
	public static int SCALING_FACTOR = 16;
	public static int GAME_SPEED = 10;
	public static float TIME_STEP = 1/45f;
	
	private static PhysicsWorldManager singleton = null;
	
	private World world;
	private RayHandler lightWorld;
	private ArrayList<Body> deleteQueue;
	
	private PhysicsWorldManager()
	{
		world = new World(new Vector2(0, 0), true);
		lightWorld = new RayHandler(world);
		
		deleteQueue = new ArrayList<Body>();
	}
	
	public static PhysicsWorldManager getPhysicsWorldManager()
	{
		if (singleton == null)
		{
			singleton = new PhysicsWorldManager();
		}
		
		return singleton;
	}
	
	/**
	 * step
	 *
	 * Does a normal physics world step and checks the delete queue.
	 */
	public void step(float timeStep, int velocityIterations, int positionIterations)
	{
		world.step(timeStep, velocityIterations, positionIterations);
		deleteQueue();
	}
	
	/**
	 * deleteAll
	 * 
	 * Checks if the delete queue is empty. If it's not
	 * then delete all of the items in queue out of the World
	 * and clear the queue.
	 */
	private void deleteQueue()
	{
		if (deleteQueue.size() <= 0) return;
		
		for (int i = 0; i < deleteQueue.size(); i++)
			world.destroyBody(deleteQueue.get(i));

		deleteQueue.clear();
	}
	
	/**
	 * delete
	 * @param body - Body to be deleted
	 * 
	 * Adds a body to the delete queue
	 */
	public void delete(Body body)
	{
		deleteQueue.add(body);
	}
	
	public void deleteAll()
	{
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		
		for (int i = 0; i < bodies.size; i++)
		{
			deleteQueue.add(bodies.get(i));
		}
	}
	
	public World 		getWorld()		{	return world;		}
	public RayHandler 	getLightWorld()	{	return lightWorld;	}
}
