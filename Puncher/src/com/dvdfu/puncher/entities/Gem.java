package com.dvdfu.puncher.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.puncher.handlers.GameObject;
import com.dvdfu.puncher.handlers.Vars;

public class Gem extends GameObject {
	public enum State {
		FALL, GRAB, COLLECT, REMOVE, DESTROY
	};

	private State state;
	private float fallSpeed;
	private int timer;
	private TextureRegion gemRemove[];

	public Gem(float x) {
		super(x, Vars.SCREEN_HEIGHT + 8, 16, 16);
		setSprite(new TextureRegion(new Texture(Gdx.files.internal("img/gem.png"))));
		fallSpeed = MathUtils.random(0.5f, 1.0f);
		xOffset = -8;
		yOffset = -8;
		xSpriteOffset = -8;
		ySpriteOffset = -8;
		state = State.FALL;
		timer = 0;
		gemRemove = new TextureRegion[4];
		for (int i = 0; i < 4; i++) {
			gemRemove[i] = new TextureRegion(new Texture(Gdx.files.internal("img/gemremove.png")), i * 16, 0, 16, 16);
		}
	}

	public void setState(State state) {
		this.state = state;
	}

	public State getState() {
		return state;
	}
	
	public int getTimer() {
		return timer;
	}

	public void update() {
		super.update();
		switch (state) {
		case FALL:
			y -= fallSpeed;
			if (y + spriteHeight < 0) {
				state = State.REMOVE;
			}
			break;
		case GRAB:
			timer++;
			if (timer == 40) {
				timer = 0;
			}
			break;
		case COLLECT:
			setSprite(gemRemove);
			if (sprite.playedOnce()) {
				state = State.REMOVE;
			}
			break;
		default:
			break;
		}
	}
}
