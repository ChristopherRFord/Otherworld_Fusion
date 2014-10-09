package com.fusion.ecs.components;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import com.fusion.ecs.Component_Fusion;
import com.fusion.phx.CollisionData;
import com.fusion.util.PhysicsWorldManager;

import static com.fusion.util.PhysicsWorldManager.*;

public class PhysicsComponent extends Component_Fusion
{	
	private PhysicsWorldManager PhysicsManager;
	
	public Body body;
	
	private int width, height;
	private float scaledW, scaledH;
	
	public float actualX, actualY;
	public float originX, originY;
	public float scaledX, scaledY;
	
	public String tag;
	
	public boolean isColliding;
	public Vector2 normals;

	@Override
	public boolean init(Element element)
	{
		PhysicsManager = PhysicsWorldManager.getPhysicsWorldManager();
		
		Element data = element.getChildByName("data");
		
		float x = data.getFloat("X");
		float y = data.getFloat("Y");
		float r = data.getFloat("R");
		width = data.getInt("W");
		height = data.getInt("H");
		String type = data.get("Body");
		tag = data.get("Tag");
		
		scaledW = width/SCALING_FACTOR;
		scaledH = height/SCALING_FACTOR;
		
		actualX = x + (width/2);
		actualY = y + (height/2);
		
		originX = actualX + (width/2);
		originY = actualY + (height/2);
		
		scaledX = (x/SCALING_FACTOR) + scaledW;
		scaledY = (y/SCALING_FACTOR) + scaledH;
		
		BodyDef bodyDef = new BodyDef();
		
		switch (type)
		{
		case "Dynamic":
			bodyDef.type = BodyType.DynamicBody;
			break;
		case "Static":
			bodyDef.type = BodyType.StaticBody;
			break;
		case "Kinematic":
			bodyDef.type = BodyType.KinematicBody;
			break;
		}
		
		bodyDef.position.set(scaledX + (scaledW/2), scaledY + (scaledH/2));
		body = PhysicsManager.getWorld().createBody(bodyDef);
		body.setFixedRotation(false);
		body.setUserData(new CollisionData(parent, tag));
		
		CircleShape circle = new CircleShape();
		circle.setRadius(r/(SCALING_FACTOR * 2));

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.friction = 0;
		fixtureDef.density = 0;

		body.createFixture(fixtureDef);

		circle.dispose();
		
		normals = new Vector2();
		
		return true;
	}
	
	@Override
	public void destroyComponent()
	{
		PhysicsManager.delete(body);
	}
	
	public void move()
	{
		Vector2 scaledPosition = body.getPosition();
		
		scaledX = scaledPosition.x;
		scaledY = scaledPosition.y;
		
		actualX = (scaledPosition.x * SCALING_FACTOR) - (width/2);
		actualY = (scaledPosition.y * SCALING_FACTOR) - (height/2);
		
		originX = scaledPosition.x * SCALING_FACTOR;
		originY = scaledPosition.y * SCALING_FACTOR;
	}
}
