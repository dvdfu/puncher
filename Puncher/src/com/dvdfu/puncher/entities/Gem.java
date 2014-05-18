package com.dvdfu.puncher.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.puncher.handlers.GameObject;
import com.dvdfu.puncher.handlers.Vars;

public class Gem extends GameObject {
	public boolean held;
	private float fallSpeed;

	public Gem(float x) {
		super(x, Vars.SCREEN_HEIGHT + 8, 16, 16);
		setSprite(new TextureRegion(new Texture(Gdx.files.internal("img/gem.png"))));
		held = false;
		fallSpeed = MathUtils.random(0.5f, 2.0f);
		xOffset = -8;
		yOffset = -8;
		xSpriteOffset = -8;
		ySpriteOffset = -8;
	}

	public void update() {
		y -= fallSpeed;
		super.update();
	}
}
