package com.fusion.ecs.components;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.fusion.ecs.Component_Fusion;

public class PositionComponent extends Component_Fusion
{
	public int X, Y;
	
	public PositionComponent(){}

	@Override
	public void Init(Element Element)
	{
		Element Data = Element.getChildByName("data");
		X = Data.getInt("X");
		Y = Data.getInt("Y");
	}
}
