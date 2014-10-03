package com.fusion.util;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

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
	private static PhysicsWorldManager Singleton = null;
	public static int SCALING_FACTOR = 16;
	public static int GAME_SPEED = 10;
	public static float TIME_STEP = 1/45f;
	
	private World World;
	private ArrayList<Body> DeleteQueue;
	
	private PhysicsWorldManager()
	{
		World = new World(new Vector2(0, 0), true);
		
		DeleteQueue = new ArrayList<Body>();
	}
	
	public static PhysicsWorldManager GetPhysicsWorldManager()
	{
		if (Singleton == null)
		{
			Singleton = new PhysicsWorldManager();
		}
		
		return Singleton;
	}
	
	/**
	 * Step
	 *
	 * Does a normal physics world step and checks the delete queue.
	 */
	public void Step(float timeStep, int velocityIterations, int positionIterations)
	{
		World.step(timeStep, velocityIterations, positionIterations);
		DeleteQueue();
	}
	
	/**
	 * DeleteAll
	 * 
	 * Checks if the delete queue is empty. If it's not
	 * then delete all of the items in queue out of the World
	 * and clear the queue.
	 */
	private void DeleteQueue()
	{
		if (DeleteQueue.size() <= 0) return;
		
		for (int i = 0; i < DeleteQueue.size(); i++)
			World.destroyBody(DeleteQueue.get(i));

		DeleteQueue.clear();
	}
	
	/**
	 * Delete
	 * @param body - Body to be deleted
	 * 
	 * Adds a body to the delete queue
	 */
	public void Delete(Body body)
	{
		DeleteQueue.add(body);
	}
	
	public World GetWorld(){	return World;	}
}
