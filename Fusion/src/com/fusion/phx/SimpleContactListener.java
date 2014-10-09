package com.fusion.phx;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import com.fusion.ecs.components.PhysicsComponent;

/**
 * SimpleConatctListener
 * @author Christopher Ford
 *
 * Handles simple collision function, like handing normals
 * off to an entity for direction, or getting an event
 * from another entity so it can be executed properly.
 */
public class SimpleContactListener implements ContactListener
{
	ComponentMapper<PhysicsComponent> PM = ComponentMapper.getFor(PhysicsComponent.class);
	
	@Override
	public void beginContact(Contact contact){}
	@Override
	public void endContact(Contact contact){}
	
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse){}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
	{
		Entity entity = findEntity(contact.getFixtureA(), contact.getFixtureB());
		if (entity == null) return;
		
		PhysicsComponent pc = PM.get(entity);
		if (PM == null) return;
		
		pc.isColliding = true;
		pc.normals = oldManifold.getLocalNormal();
	}

	private Entity findEntity(Fixture fixtureA, Fixture fixtureB)
	{
		CollisionData dataA = (CollisionData) fixtureA.getBody().getUserData();
		CollisionData dataB = (CollisionData) fixtureB.getBody().getUserData();

		if (dataA.tag.equals("Player"))
			return dataA.parent;
		if (dataB.tag.equals("Player"))
			return dataB.parent;

		return null;
	}

}
