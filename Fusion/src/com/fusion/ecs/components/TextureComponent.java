package com.fusion.ecs.components;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.XmlReader.Element;

import com.fusion.ecs.Component_Fusion;
import com.fusion.util.AssetGroupManager;

/**
 * TextureComponent
 * @author Christopher Ford
 * 
 * This component contains a HashMap of textures. This is so an
 * entity can have multiple textures.
 */
public class TextureComponent extends Component_Fusion
{
	public TextureRegion currentTexture;
	private HashMap<String, TextureRegion> textures;

	private Texture spriteSheet;
	private int numRows;

	private int width, height;
	private String[] keys;

	@Override
	public boolean init(Element element)
	{
		Element datas = element.getChildByName("data");
		textures = new HashMap<String, TextureRegion>();
		
		for (int i = 0; i < datas.getChildCount(); i++)
		{
			Element data = datas.getChild(i);

			String location = data.get("SpriteSheet");
			width = data.getInt("W");
			height = data.getInt("H");
			String keysFull = data.get("Keys");

			keys = keysFull.split(",");

			spriteSheet = AssetGroupManager.getAssetGroupManager().get(location, Texture.class);
			if (spriteSheet == null) return false;

			numRows = spriteSheet.getHeight()/height;

			TextureRegion[][] spriteSheetArray = TextureRegion.split(spriteSheet, width, height);
			
			
			for (int r = 0; r < numRows; r++)
				textures.put(keys[r], spriteSheetArray[r][0]);
			
			currentTexture = textures.get(keys[0]);
		}
		
		return true;
	}

	@Override
	public void destroyComponent(){}
	
	public void setTexture(String key)
	{
		currentTexture = textures.get(key);
	}
}
