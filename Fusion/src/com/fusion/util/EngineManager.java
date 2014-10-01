package com.fusion.util;

import java.io.IOException;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.fusion.ecs.Component_Fusion;
import com.fusion.ecs.components.InputComponent;
import com.fusion.ecs.components.PositionComponent;
import com.fusion.ecs.components.TextureComponent;
import com.fusion.err.EntityLoadingException;

enum EntityType
{
	PositionComponent,
	TextureComponent,
	InputComponent
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
	private int NumEntities;
	
	private XmlReader XmlReader;
	
	
	// Private constructor
	private EngineManager()
	{
		Engine = new Engine();
		ComponentFactory = new ComponentFactory();
		NumEntities = 0;

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
		}
		catch (IOException | EntityLoadingException | SerializationException e)
		{
			Gdx.app.log(e.getClass() + "", e.getMessage());
			return false;
		}
		
		addEntity(Entity);
		return true;
	}
	
	public void update(float Delta)
	{
		Engine.update(Delta);
	}
	
	public void addSystem(EntitySystem System)
	{
		Engine.addSystem(System);
	}
	
	public void addEntity(Entity Entity)
	{
		Engine.addEntity(Entity);
		NumEntities++;
	}
	
	public void removeEnitty(Entity Entity)
	{
		if (NumEntities <= 0) return;
		
		Engine.removeEntity(Entity);
		NumEntities--;
	}
	
	public void removeAllEntities()
	{
		Engine.removeAllEntities();
		NumEntities = 0;
	}
	
	public int 		GetNumEntities(){	return NumEntities;	}
	public Engine 	GetEngine()		{	return Engine;		}
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
		if (Type.equals(EntityType.PositionComponent.toString()))
		{
			PositionComponent P = new PositionComponent();
			P.Init(Element);
			P.SetParent(Parent);
			return P;
		}
		else if (Type.equals(EntityType.TextureComponent.toString()))
		{
			TextureComponent T = new TextureComponent();
			T.Init(Element);
			T.SetParent(Parent);
			return T;
		}
		else if (Type.equals(EntityType.InputComponent.toString()))
		{
			InputComponent I = new InputComponent();
			I.Init(Element);
			I.SetParent(Parent);
			return I;
		}
		
		return null;
	}
}
