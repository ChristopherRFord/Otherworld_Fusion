package com.fusion.gfx;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import com.fusion.util.PhysicsWorldManager;

import static com.fusion.util.PhysicsWorldManager.SCALING_FACTOR;


/**
 * DebugRenderer
 * @author Christopher Ford
 * 
 * This class is used to render lines for the movement of the camera,
 * bodies for physics and anything else that requires debugging
 */
public class DebugRenderer
{
	// Actual Renderers
	private Box2DDebugRenderer box2DDebugRenderer;
	private	ShapeRenderer shapeRenderer;
	
	// Cameras
	private VirtualViewportCamera gameCamera;
	private VirtualViewportCamera physicsCamera;
	
	// Physics World
	private PhysicsWorldManager physicsManager;
	
	public DebugRenderer(VirtualViewportCamera gameCamera, VirtualViewportCamera physicsCamera)
	{
		this.gameCamera = gameCamera;
		this.physicsCamera = physicsCamera;
		
		box2DDebugRenderer = new Box2DDebugRenderer();
		shapeRenderer = new ShapeRenderer();
		
		physicsManager = PhysicsWorldManager.getPhysicsWorldManager();
	}
	
	public void debugRender(boolean debug)
	{
		if (!debug) return;
		
		physicsCamera.position.set(gameCamera.position.x / SCALING_FACTOR, gameCamera.position.y/SCALING_FACTOR, 0);
		physicsCamera.update();
		box2DDebugRenderer.render(physicsManager.getWorld(), physicsCamera.combined);
		
		shapeRenderer.setProjectionMatrix(gameCamera.combined);
		
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.line(gameCamera.blposition.x + 0, gameCamera.GetTopBoundry(), gameCamera.blposition.x + gameCamera.viewportWidth, gameCamera.GetTopBoundry());
		shapeRenderer.line(gameCamera.blposition.x + 0, gameCamera.GetBottomBoundry(), gameCamera.blposition.x + gameCamera.viewportWidth, gameCamera.GetBottomBoundry());
		shapeRenderer.line(gameCamera.GetRightBoundry(), gameCamera.blposition.y + 0, gameCamera.GetRightBoundry(), gameCamera.blposition.y + gameCamera.viewportHeight);
		shapeRenderer.line(gameCamera.GetLeftBoundry(), gameCamera.blposition.y + 0, gameCamera.GetLeftBoundry(), gameCamera.blposition.y + gameCamera.viewportHeight);
		shapeRenderer.end();
	}
}
