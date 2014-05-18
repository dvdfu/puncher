package com.dvdfu.puncher.entities;

import com.dvdfu.puncher.handlers.GameObject;

public class Obstacle extends GameObject {
	public enum State {
		FALL, REMOVE, DESTROY
	}
	private State state;
	public Obstacle(float x, float y) {
		super(x, y, 16, 16);
		setSprite("img/enemy.png");
		xOffset = -8;
		yOffset = -8;
		xSpriteOffset = -8;
		ySpriteOffset = -8;
		state = State.FALL;
	}
	
	public State getState() {
		return state;
	}
	
	public void update() {
		super.update();
		switch (state) {
		case FALL:
			y -= 0.5f;
			if (y + spriteHeight / 2 < 0) {
				state = State.REMOVE;
			}
			break;
		}
	}
}
