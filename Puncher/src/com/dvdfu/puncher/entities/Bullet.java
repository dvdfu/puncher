package com.dvdfu.puncher.entities;

import com.dvdfu.puncher.handlers.GameObject;
import com.dvdfu.puncher.handlers.Vars;

public class Bullet extends GameObject {
	public Bullet(float x, float y) {
		super(x, y, 8, 4);
		setSprite("img/bullet.png");
		xOffset = -4;
		yOffset = -4;
		xSpriteOffset = -4;
		ySpriteOffset = -4;
	}
	
	public void update() {
		y += 4;
		super.update();
	}
	
	public boolean isDead() {
		return y > Vars.SCREEN_HEIGHT + 4;
	}
	
	public void kill() {
		y = Vars.SCREEN_HEIGHT + 4;
	}
}
