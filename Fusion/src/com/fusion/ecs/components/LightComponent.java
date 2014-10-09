package com.fusion.ecs.components;

import box2dLight.Light;
import box2dLight.RayHandler;
import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.fusion.ecs.Component_Fusion;
import com.fusion.util.PhysicsWorldManager;

/**
 * LightComponent
 * @author Christopher
 *
 * Using Box2DLights, this component contains information that renders
 * a light on the screen.
 */
public class LightComponent extends Component_Fusion
{
	public Light light;
	public String type;
	
	@Override
	public boolean init(Element element)
	{
		RayHandler lightWorld = PhysicsWorldManager.getPhysicsWorldManager().getLightWorld();
		
		Element data = element.getChildByName("data");
		type = data.get("Type");
		int rays = data.getInt("Rays");
		float x = data.getFloat("X"), Y = data.getFloat("Y");
		float distance = data.getFloat("Distance");
		float red = data.getFloat("Red"); float Blue = data.getFloat("Blue");
		float green = data.getFloat("Green"); float Alpha = data.getFloat("Alpha");
		switch(type)
		{
		case "Point":
			light = new PointLight(lightWorld, rays);
			break;
		}
		
		light.setPosition(x, Y);
		light.setDistance(distance);
		light.setColor(new Color(red, Blue, green, Alpha));
		
		return true;
	}

	@Override
	public void destroyComponent()
	{
		light.remove();
	}
	
}
