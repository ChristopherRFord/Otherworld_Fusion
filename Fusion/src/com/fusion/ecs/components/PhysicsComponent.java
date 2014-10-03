package com.fusion.ecs.components;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import com.fusion.ecs.Component_Fusion;
import com.fusion.util.PhysicsWorldManager;

import static com.fusion.util.PhysicsWorldManager.*;

public class PhysicsComponent extends Component_Fusion
{	
	private PhysicsWorldManager PhysicsManager;
	
	public Body Body;
	
	private int Width, Height;
	private float ScaledW, ScaledH;
	
	public float ActualX, ActualY;
	public float OriginX, OriginY;
	public float ScaledX, ScaledY;

	@Override
	public boolean Init(Element Element)
	{
		PhysicsManager = PhysicsWorldManager.GetPhysicsWorldManager();
		
		Element Data = Element.getChildByName("data");
		
		float x = Data.getFloat("X");
		float y = Data.getFloat("Y");
		float r = Data.getFloat("R");
		Width = Data.getInt("W");
		Height = Data.getInt("H");
		String Type = Data.get("Body");
		
		ScaledW = Width/SCALING_FACTOR;
		ScaledH = Height/SCALING_FACTOR;
		
		ActualX = x + (Width/2);
		ActualY = y + (Height/2);
		
		OriginX = ActualX + (Width/2);
		OriginY = ActualY + (Height/2);
		
		ScaledX = (x/SCALING_FACTOR) + ScaledW;
		ScaledY = (y/SCALING_FACTOR) + ScaledH;
		
		BodyDef BodyDef = new BodyDef();
		
		switch (Type)
		{
		case "Dynamic":
			BodyDef.type = BodyType.DynamicBody;
			break;
		case "Static":
			BodyDef.type = BodyType.StaticBody;
			break;
		case "Kinematic":
			BodyDef.type = BodyType.KinematicBody;
			break;
		}
		
		BodyDef.position.set(ScaledX + (ScaledW/2), ScaledY + (ScaledH/2));
		Body = PhysicsManager.GetWorld().createBody(BodyDef);
		
		CircleShape circle = new CircleShape();
		circle.setRadius(r/(SCALING_FACTOR * 2));

		FixtureDef FixtureDef = new FixtureDef();
		FixtureDef.shape = circle;
		FixtureDef.friction = 0;
		FixtureDef.density = 100;

		Body.createFixture(FixtureDef);

		circle.dispose();
		
		return true;
	}
	
	@Override
	public void DestroyComponent()
	{
		PhysicsManager.Delete(Body);
	}
	
	public void Move()
	{
		Vector2 ScaledPosition = Body.getPosition();
		
		ScaledX = ScaledPosition.x;
		ScaledY = ScaledPosition.y;
		
		ActualX = (ScaledPosition.x * SCALING_FACTOR) - (Width/2);
		ActualY = (ScaledPosition.y * SCALING_FACTOR) - (Height/2);
		
		OriginX = ScaledPosition.x * SCALING_FACTOR;
		OriginY = ScaledPosition.y * SCALING_FACTOR;
	}
}
