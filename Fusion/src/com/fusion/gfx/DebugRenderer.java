package com.fusion.gfx;

import static com.fusion.util.PhysicsWorldManager.SCALING_FACTOR;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.fusion.util.PhysicsWorldManager;

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
	private Box2DDebugRenderer Box2DDebugRenderer;
	private	ShapeRenderer ShapeRenderer;
	
	// Cameras
	private VirtualViewportCamera GameCamera;
	private VirtualViewportCamera PhysicsCamera;
	
	// Physics World
	private PhysicsWorldManager PhysicsManager;
	
	public DebugRenderer(VirtualViewportCamera GameCamera, VirtualViewportCamera PhysicsCamera)
	{
		this.GameCamera = GameCamera;
		this.PhysicsCamera = PhysicsCamera;
		
		Box2DDebugRenderer = new Box2DDebugRenderer();
		ShapeRenderer = new ShapeRenderer();
		
		PhysicsManager = PhysicsWorldManager.GetPhysicsWorldManager();
	}
	
	public void DebugRender(boolean Debug)
	{
		if (!Debug) return;
		
		PhysicsCamera.position.set(GameCamera.position.x / SCALING_FACTOR, GameCamera.position.y/SCALING_FACTOR, 0);
		PhysicsCamera.update();
		Box2DDebugRenderer.render(PhysicsManager.GetWorld(), PhysicsCamera.combined);
		
		ShapeRenderer.setProjectionMatrix(GameCamera.combined);
		
		ShapeRenderer.begin(ShapeType.Line);
		ShapeRenderer.line(GameCamera.blposition.x + 0, GameCamera.GetTopBoundry(), GameCamera.blposition.x + GameCamera.viewportWidth, GameCamera.GetTopBoundry());
		ShapeRenderer.line(GameCamera.blposition.x + 0, GameCamera.GetBottomBoundry(), GameCamera.blposition.x + GameCamera.viewportWidth, GameCamera.GetBottomBoundry());
		ShapeRenderer.line(GameCamera.GetRightBoundry(), GameCamera.blposition.y + 0, GameCamera.GetRightBoundry(), GameCamera.blposition.y + GameCamera.viewportHeight);
		ShapeRenderer.line(GameCamera.GetLeftBoundry(), GameCamera.blposition.y + 0, GameCamera.GetLeftBoundry(), GameCamera.blposition.y + GameCamera.viewportHeight);
		ShapeRenderer.end();
	}
}
