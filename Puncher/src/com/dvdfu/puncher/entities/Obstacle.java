package com.dvdfu.puncher.entities;

import com.dvdfu.puncher.handlers.GameObject;

public class Obstacle extends GameObject {
	public Obstacle(float x, float y) {
		super(x, y, 16, 16);
		setSprite("img/enemy.png");
		xOffset = -8;
		yOffset = -8;
		xSpriteOffset = -8;
		ySpriteOffset = -8;
	}
}
