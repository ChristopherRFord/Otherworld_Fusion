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
import com.fusion.ecs.components.AnimationComponent;
import com.fusion.ecs.components.InputComponent;
import com.fusion.ecs.components.LightComponent;
import com.fusion.ecs.components.PhysicsComponent;
import com.fusion.ecs.components.TextureComponent;
import com.fusion.err.EntityLoadingException;

enum EntityType
{
	TextureComponent,
	InputComponent,
	PhysicsComponent,
	AnimationComponent,
	LightComponent
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
	private static EngineManager singleton = null;

	private Engine engine;
	private ComponentFactory componentFactory;
	private ArrayList<Entity> entities;

	private XmlReader xmlReader;

	// Private constructor
	private EngineManager()
	{
		engine = new Engine();
		componentFactory = new ComponentFactory();
		entities = new ArrayList<Entity>();

		xmlReader = new XmlReader();
	}

	// Static singleton retrieve message
	public static EngineManager getEngineManager()
	{
		if (singleton == null)
			singleton = new EngineManager();

		return singleton;
	}

	/**
	 * loadEntity
	 * @param location - Location in project directory of XML file
	 * 
	 * Parses a XML file in the project directory. Loads every individual
	 * component into an Entity.
	 */
	public boolean loadEntity(String location)
	{
		Entity Entity = new Entity();

		try
		{
			Gdx.app.log("-Loading XML", location);

			// Loading in the XML file
			XmlReader.Element xml = xmlReader.parse(Gdx.files.classpath(location));

			//Getting the components and storing them in an array
			Array<XmlReader.Element> components = xml.getChildrenByName("component");

			if (components.size != 0)
			{
				//Initializing the components
				for (XmlReader.Element element : components)
				{
					String type = element.getChildByName("type").getText();
					Component_Fusion component = componentFactory.createComponent(type, element, Entity);

					if (component == null) throw new EntityLoadingException(location, type);
					Entity.add(component);
				}

				addEntity(Entity);
			}
		}
		catch (IOException | EntityLoadingException | SerializationException e)
		{
			Gdx.app.log(e.getClass() + "", e.getMessage());
			return false;
		}

		return true;
	}

	public void addEntity(Entity entity)
	{
		engine.addEntity(entity);
		entities.add(entity);
	}

	public void deleteEnitty(Entity entity)
	{
		if (entities.isEmpty()) return;

		ImmutableArray<Component> components = entity.getComponents();

		for (int i = 0; i < components.size(); i++)
		{
			Component_Fusion component = (Component_Fusion) components.get(i);
			component.destroyComponent();
		}

		engine.removeEntity(entity);
		entities.remove(entity);
	}

	public void deleteAllEntities()
	{
		if (entities.isEmpty()) return;

		for (int i = 0; i < entities.size(); i++)
		{
			Entity entity = entities.get(i);

			ImmutableArray<Component> components = entity.getComponents();

			for (int j = 0; j < components.size(); j++)
			{
				Component_Fusion component = (Component_Fusion) components.get(j);
				component.destroyComponent();
			}
		}

		engine.removeAllEntities();
		entities.clear();
	}

	public ArrayList<Entity>	getEntitiy()	{	return entities;		}
	public int 					getNumEntities(){	return entities.size();	}
	public Engine 				getEngine()		{	return engine;			}
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

	protected Component_Fusion createComponent(String type, Element element, Entity parent)
	{
		if (type.equals(EntityType.TextureComponent.toString()))
		{
			TextureComponent texutreComponent = new TextureComponent();
			texutreComponent.setParent(parent);
			if (!texutreComponent.init(element)) return null;
			return texutreComponent;
		}
		else if (type.equals(EntityType.InputComponent.toString()))
		{
			InputComponent inputComponent = new InputComponent();
			inputComponent.setParent(parent);
			if (!inputComponent.init(element)) return null;
			return inputComponent;
		}
		else if (type.equals(EntityType.PhysicsComponent.toString()))
		{
			PhysicsComponent physicsComponent = new PhysicsComponent();
			physicsComponent.setParent(parent);
			if (!physicsComponent.init(element)) return null;
			return physicsComponent;
		}
		else if (type.equals(EntityType.AnimationComponent.toString()))
		{
			AnimationComponent animationComponent = new AnimationComponent();
			animationComponent.setParent(parent);
			if (!animationComponent.init(element)) return null;
			return animationComponent;
		}
		else if (type.equals(EntityType.LightComponent.toString()))
		{
			LightComponent lightComponent = new LightComponent();
			lightComponent.setParent(parent);
			if (!lightComponent.init(element)) return null;
			return lightComponent;
		}
		return null;
	}
}
