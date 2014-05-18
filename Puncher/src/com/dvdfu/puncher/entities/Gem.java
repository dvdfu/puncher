package com.dvdfu.puncher.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.puncher.handlers.GameObject;
import com.dvdfu.puncher.handlers.Vars;

public class Gem extends GameObject {
	public enum State {
		FALL, GRAB, COLLECT1, COLLECT2, EXIT, DESTROY
	};

	private State state;
	private float fallSpeed;

	public Gem(float x) {
		super(x, Vars.SCREEN_HEIGHT + 8, 16, 16);
		setSprite(new TextureRegion(new Texture(Gdx.files.internal("img/gem.png"))));
		fallSpeed = MathUtils.random(0.5f, 2.0f);
		xOffset = -8;
		yOffset = -8;
		xSpriteOffset = -8;
		ySpriteOffset = -8;
		state = State.FALL;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public State getState() {
		return state;
	}

	public void update() {
		switch (state) {
		case FALL:
			y -= fallSpeed;
			if (y + spriteHeight < 0) {
				state = State.EXIT;
			}
			break;
		case COLLECT2:
			x += (-width - x) / 10;
			y += (-height - y) / 10;
			if (y + spriteHeight < 0) {
				state = State.EXIT;
			}
			break;
		default:
			break;
		}
		super.update();
	}
}
