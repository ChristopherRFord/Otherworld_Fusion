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
	public TextButton_Otherworld(String Text, Texture Texture, Vector2 Pos, Vector2 Bounds)
	{
		super(Text, new TextButtonStyle_Otherworld(Texture));
		setBounds(Pos.x, Pos.y, Bounds.x, Bounds.y);
	}
}

class TextButtonStyle_Otherworld extends TextButtonStyle
{
	protected TextButtonStyle_Otherworld(Texture Texture)
	{
		super();
		
		Skin Skin = new Skin();
		
		NinePatch UP 	= new NinePatch(Texture, 8, 8, 8, 8);
		NinePatch OVER	= new NinePatch(Texture, 8, 8, 8, 8);
		NinePatch DOWN 	= new NinePatch(Texture, 8, 8, 8, 8);
		
		OVER.setColor(Color.CYAN);
		DOWN.setColor(Color.BLUE);
		
		Skin.add("UP", UP);
		Skin.add("OVER", OVER);
		Skin.add("DOWN", DOWN);
		
		up 		= Skin.getDrawable("UP");
		over 	= Skin.getDrawable("OVER");
		down 	= Skin.getDrawable("DOWN");
		
		font = new BitmapFont();
		fontColor = Color.WHITE;
	}
}
