package com.otherworld.style;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class TextFieldStyle_Otherworld extends TextFieldStyle
{
	public TextFieldStyle_Otherworld(Texture texture, BitmapFont font, Color fontColor)
	{
		super();
		

		background = new NinePatchDrawable(new NinePatch(texture, 8, 8, 8, 8));
		this.font = font;
		this.fontColor = fontColor;
	}
}
