package com.dvdfu.puncher.entities;

import com.dvdfu.puncher.handlers.GameObject;
import com.dvdfu.puncher.handlers.Vars;

public class Debris extends GameObject {
	private float dx;
	private float dy;
	private boolean dead;

	public Debris(float x, float y, float dx, float dy, String filename) {
		super(x, y, 8, 8);
		setSprite(filename);
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
		dead = (y + spriteHeight / 2 < 0 || y - spriteHeight / 2 > Vars.SCREEN_HEIGHT || x + spriteWidth < 0 || x - spriteWidth > Vars.SCREEN_WIDTH);
		super.update();
	}

	public boolean isDead() {
		return dead;
	}
}
