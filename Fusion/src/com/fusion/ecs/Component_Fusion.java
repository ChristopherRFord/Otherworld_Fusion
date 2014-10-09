package com.fusion.ecs;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * Component_Fusion
 * @author Christopher Ford
 *
 * Template for a component. Based off of Ashey's Component,
 * this child class adds an entity parent and forces future
 * child classes to inherit a Init method which initializes
 * the component with data read from an XML file.
 */
public abstract class Component_Fusion extends Component
{
	protected Entity parent;
	
	public Entity getParent(){	return parent;	}
	public void setParent(Entity parent){	this.parent = parent;	}	
	
	public abstract boolean init(Element element);
	public abstract void destroyComponent();
}
