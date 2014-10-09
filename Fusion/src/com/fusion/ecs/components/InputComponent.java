package com.fusion.ecs.components;

import com.badlogic.gdx.utils.XmlReader.Element;

import com.fusion.ecs.Component_Fusion;

public class InputComponent extends Component_Fusion
{
	@Override
	public boolean init(Element Element)
	{
		return true;
	}

	@Override
	public void destroyComponent() {}
}
