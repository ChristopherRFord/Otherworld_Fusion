package com.fusion.gfx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * VirtualViewportCamera
 * @author Christopher Ford
 *
 * A LIBGDX OrthographicCamera that is forced
 * to use a Fusion VirtualViewport
 */
public class VirtualViewportCamera extends OrthographicCamera
{

	Vector3 tmp = new Vector3();
	Vector2 origin = new Vector2();
	VirtualViewport virtualViewport;
	
	public Vector2 blposition = new Vector2();

	public void setVirtualViewport(VirtualViewport virtualViewport) {	this.virtualViewport = virtualViewport;	}
	public VirtualViewportCamera(VirtualViewport virtualViewport) 	{	this(virtualViewport, 0f, 0f);			}
	
	public VirtualViewportCamera(){}

	public VirtualViewportCamera(VirtualViewport virtualViewport, float cx, float cy)
	{
		this.virtualViewport = virtualViewport;
		this.origin.set(cx, cy);
	}

	@Override
	public void update()
	{
		
		float left = zoom * -viewportWidth / 2 + virtualViewport.getVirtualWidth() * origin.x;
		float right = zoom * viewportWidth / 2 + virtualViewport.getVirtualWidth() * origin.x;
		float top = zoom * viewportHeight / 2 + virtualViewport.getVirtualHeight() * origin.y;
		float bottom = zoom * -viewportHeight / 2 + virtualViewport.getVirtualHeight() * origin.y;

		projection.setToOrtho(left, right, bottom, top, Math.abs(near), Math.abs(far));
		view.setToLookAt(position, tmp.set(position).add(direction), up);
		combined.set(projection);
		Matrix4.mul(combined.val, view.val);
		invProjectionView.set(combined);
		Matrix4.inv(invProjectionView.val);
		frustum.update(invProjectionView);
		
		blposition.set(position.x - viewportWidth/2, position.y - viewportHeight/2);
	}

	public void updateViewport()
	{
		setToOrtho(false, virtualViewport.getWidth(), virtualViewport.getHeight());
	}
	
	public VirtualViewport getVirtualViewport()
	{
		return virtualViewport;
	}
	
	public float GetTopBoundry()		{	return blposition.y + (viewportHeight * .8f);	}
	public float GetBottomBoundry()		{	return blposition.y + (viewportHeight * .2f);	}
	
	public float GetRightBoundry()		{	return blposition.x + (viewportWidth * .8f);	}
	public float GetLeftBoundry()		{	return blposition.x + (viewportWidth *.2f);		}
}