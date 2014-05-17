package com.dvdfu.puncher.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.puncher.handlers.GameObject;
import com.dvdfu.puncher.handlers.Vars;

public class Debris extends GameObject {
	private float dx;
	private float dy;
	private boolean dead;

	public Debris(float x, float y, float dx, float dy) {
		super(x, y, 8, 8);
		setSprite(new TextureRegion(new Texture(Gdx.files.internal("img/debris.png"))));
		dead = false;
		this.dx = dx;
		this.dy = dy;
		xOffset = -4;
		yOffset = -4;
		xSpriteOffset = -4;
		ySpriteOffset = -4;
	}

	public void update() {
		x += dx;
		y += dy;
		dy -= 0.4;
		dead = y + 4 < 0;
		super.update();
	}

	public boolean isDead() {
		return dead;
	}
}
