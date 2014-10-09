package com.otherworld.style;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class TextButton_Otherworld extends TextButton
{
	public TextButton_Otherworld(String text, Texture texture, Vector2 pos, Vector2 bounds)
	{
		super(text, new TextButtonStyle_Otherworld(texture));
		setBounds(pos.x, pos.y, bounds.x, bounds.y);
	}
}

class TextButtonStyle_Otherworld extends TextButtonStyle
{
	protected TextButtonStyle_Otherworld(Texture texture)
	{
		super();
		
		Skin skin = new Skin();
		
		NinePatch UP 	= new NinePatch(texture, 8, 8, 8, 8);
		NinePatch OVER	= new NinePatch(texture, 8, 8, 8, 8);
		NinePatch DOWN 	= new NinePatch(texture, 8, 8, 8, 8);
		
		OVER.setColor(Color.CYAN);
		DOWN.setColor(Color.BLUE);
		
		skin.add("UP", UP);
		skin.add("OVER", OVER);
		skin.add("DOWN", DOWN);
		
		up 		= skin.getDrawable("UP");
		over 	= skin.getDrawable("OVER");
		down 	= skin.getDrawable("DOWN");
		
		font = new BitmapFont();
		fontColor = Color.WHITE;
	}
}
