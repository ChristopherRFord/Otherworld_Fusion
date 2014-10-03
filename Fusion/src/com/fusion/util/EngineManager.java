package com.fusion.util;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import com.fusion.ecs.Component_Fusion;
import com.fusion.ecs.components.InputComponent;
import com.fusion.ecs.components.PhysicsComponent;
import com.fusion.ecs.components.TextureComponent;
import com.fusion.err.EntityLoadingException;

enum EntityType
{
	TextureComponent,
	InputComponent,
	PhysicsComponent
}

/**
 * EngineManager
 * @author Christopher Ford
 * 
 * Loads and unloads a group of components and puts
 * them into an entity by parsing a XML file. Everything
 * else is Ashley Engine functionality. EngineManager
 * employs the Singleton design pattern
 * and can be accessed from any where in the Fusion
 * framework and game.
 */
public class EngineManager
{
	private static EngineManager Singleton = null;
	
	private Engine Engine;
	private ComponentFactory ComponentFactory;
	private ArrayList<Entity> Entities;
	
	private XmlReader XmlReader;
	
	
	// Private constructor
	private EngineManager()
	{
		Engine = new Engine();
		ComponentFactory = new ComponentFactory();
		Entities = new ArrayList<Entity>();

		XmlReader = new XmlReader();
	}
	
	// Static singleton retrieve message
	public static EngineManager GetEngineManager()
	{
		if (Singleton == null)
			Singleton = new EngineManager();

		return Singleton;
	}
	
	/**
	 * LoadEntity
	 * @param Location - Location in project directory of XML file
	 * 
	 * Parses a XML file in the project directory. Loads every individual
	 * component into an Entity.
	 */
	public boolean LoadEntity(String Location)
	{
		Entity Entity = new Entity();
		
		try
		{
			Gdx.app.log("-Loading XML", Location);
			
			// Loading in the XML file
			XmlReader.Element xml = XmlReader.parse(Gdx.files.classpath(Location));
			
			//Getting the components and storing them in an array
			Array<XmlReader.Element> components = xml.getChildrenByName("component");
			
			//Initializing the components
			for (XmlReader.Element element : components)
			{
				String type = element.getChildByName("type").getText();
				Component_Fusion component = ComponentFactory.CreateComponent(type, element, Entity);
				
				if (component == null) throw new EntityLoadingException(Location, type);
				Entity.add(component);
			}
			
			addEntity(Entity);
		}
		catch (IOException | EntityLoadingException | SerializationException e)
		{
			Gdx.app.log(e.getClass() + "", e.getMessage());
			return false;
		}
		
		return true;
	}
	
	public void addEntity(Entity Entity)
	{
		Engine.addEntity(Entity);
		Entities.add(Entity);
	}
	
	public void removeEnitty(Entity Entity)
	{
		if (Entities.isEmpty()) return;
		
		ImmutableArray<Component> Components = Entity.getComponents();
		
		for (int i = 0; i < Components.size(); i++)
		{
			Component_Fusion C = (Component_Fusion) Components.get(i);
			C.DestroyComponent();
		}
		
		Engine.removeEntity(Entity);
		Entities.remove(Entity);
	}
	
	public void removeAllEntities()
	{
		if (Entities.isEmpty()) return;
		
		for (int i = 0; i < Entities.size(); i++)
		{
			Entity E = Entities.get(i);
			
			ImmutableArray<Component> Components = E.getComponents();
			
			for (int j = 0; j < Components.size(); j++)
			{
				Component_Fusion C = (Component_Fusion) Components.get(j);
				C.DestroyComponent();
			}
		}
		
		Engine.removeAllEntities();
		Entities.clear();
	}
	
	public ArrayList<Entity>	GetEntitiy()	{	return Entities;		}
	public int 					GetNumEntities(){	return Entities.size();	}
	public Engine 				GetEngine()		{	return Engine;			}
}

/**
 * ComponentFactory
 * @author Chrisopher Ford
 *
 * ComponentFactory creates, inits ands returns
 * components read from an XML file.
 */
class ComponentFactory
{
	protected ComponentFactory(){}
	
	protected Component_Fusion CreateComponent(String Type, Element Element, Entity Parent)
	{
		if (Type.equals(EntityType.TextureComponent.toString()))
		{
			TextureComponent T = new TextureComponent();
			if (!T.Init(Element)) return null;
			T.SetParent(Parent);
			return T;
		}
		else if (Type.equals(EntityType.InputComponent.toString()))
		{
			InputComponent I = new InputComponent();
			if (!I.Init(Element)) return null;
			I.SetParent(Parent);
			return I;
		}
		else if (Type.equals(EntityType.PhysicsComponent.toString()))
		{
			PhysicsComponent Py = new PhysicsComponent();
			if (!Py.Init(Element)) return null;
			Py.SetParent(Parent);
			return Py;
		}
		
		return null;
	}
}
