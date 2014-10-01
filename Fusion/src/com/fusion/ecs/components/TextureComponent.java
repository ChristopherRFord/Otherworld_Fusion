package com.fusion.ecs.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.fusion.ecs.Component_Fusion;
import com.fusion.util.AssetGroupManager;

public class TextureComponent extends Component_Fusion
{
	public Texture Texture;
	
	public TextureComponent(){}

	@Override
	public void Init(Element Element)
	{
		Element Data = Element.getChildByName("data");
		Texture = AssetGroupManager.GetAssetGroupManager().Get(Data.get("Texture"), Texture.class);
	}
}
