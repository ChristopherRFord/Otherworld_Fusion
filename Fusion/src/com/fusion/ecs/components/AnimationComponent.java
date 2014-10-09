package com.fusion.ecs.components;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.XmlReader.Element;

import com.fusion.ecs.Component_Fusion;
import com.fusion.util.AssetGroupManager;

/**
 * AnimationComponent
 * @author Christopher Ford
 *
 * This component contains a hashmap of animations. This is so an
 * entity can have multiple animations.
 */
public class AnimationComponent extends Component_Fusion
{
	private HashMap<String, Animation> animations;

	private Texture spriteSheet;
	private int numRows, numCols;

	private int width, height;
	private String[] keys;

	public Animation currentAnimation;
	public float speed;
	public boolean animate;

	@Override
	public boolean init(Element element)
	{
		Element datas = element.getChildByName("data");
		animations = new HashMap<String, Animation>();
		
		for (int i = 0; i < datas.getChildCount(); i++)
		{
			Element data = datas.getChild(i);

			String location = data.get("SpriteSheet");
			width = data.getInt("W");
			height = data.getInt("H");
			String keysFull = data.get("Keys");
			speed = data.getFloat("Speed");

			keys = keysFull.split(",");

			spriteSheet = AssetGroupManager.getAssetGroupManager().get(location, Texture.class);
			if (spriteSheet == null) return false;

			numCols = spriteSheet.getWidth()/width;
			numRows = spriteSheet.getHeight()/height;

			TextureRegion[][] spriteSheetArray = TextureRegion.split(spriteSheet, width, height);

			for (int r = 0; r < numRows; r++)
			{
				TextureRegion[] tmp = new TextureRegion[numCols];

				for (int c = 0; c < numCols; c++)
					tmp[c] = spriteSheetArray[r][c];

				animations.put(keys[r], new Animation(speed, tmp));
			}

			currentAnimation = animations.get(keys[0]);
		}

		return true;
	}

	@Override
	public void destroyComponent()
	{
		animations.clear();
	}

	public void setAnimation(String key)
	{
		currentAnimation = animations.get(key);
	}
}
