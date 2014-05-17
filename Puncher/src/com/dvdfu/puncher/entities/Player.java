package com.dvdfu.puncher.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.puncher.handlers.GameObject;
import com.dvdfu.puncher.handlers.Input;
import com.dvdfu.puncher.states.Game;

public class Player extends GameObject {
	private enum State {
		IDLE, HELD, SHOT
	};

	private State state;
	private int springCount;
	private float springLength;
	private float springMiddle;
	private float t;
	private float dy;

	public Player() {
		super(0, 100, 32, 32);
		xOffset = -16;
		switchState(State.IDLE);
		springCount = 12;
		springLength = 32;
		springMiddle = 80;
	}

	private void switchState(State s) {
		t = 0;
		switch (s) {
		case IDLE:
			dy = y - springMiddle;
			break;
		case HELD:
			break;
		case SHOT:
			dy = springMiddle - y;
			break;
		}
		state = s;
	}

	public void update() {
		switch (state) {
		case IDLE:
			t += 0.2;
			y = springMiddle + dy * MathUtils.cos(t) / (1 + t);
			if (Input.MouseDown() && t > 5) {
				if (body.contains(Game.screenInput.x, Game.screenInput.y)) {
					switchState(State.HELD);
				}
			}
			break;
		case HELD:
			float mouseY = Game.screenInput.y;
			if (Input.MouseDown()) {
				if (mouseY > 0) {
					if (mouseY >= springCount * springLength) {
						y = springCount * springLength - 1;
					} else {
						y = mouseY;
					}
				} else {
					y = 0;
				}
			} else {
				if (mouseY > springMiddle) {
					switchState(State.IDLE);
				} else {
					switchState(State.SHOT);
				}
			}
			break;
		case SHOT:
			y += dy / 2;
			if (y + dy / 2 > springMiddle + dy * 3) {
				y = springMiddle + dy * 3;
				switchState(State.IDLE);
			}
			break;
		}
		x = Game.screenInput.x;
		super.update();
	}

	public void render(ShapeRenderer sr) {
		sr.begin(ShapeType.Line);
		if (y < springCount * springLength) {
			float springHeight = y / springCount;
			float springWidth = (float) Math.sqrt((double) (springLength * springLength - springHeight * springHeight)) / 2;
			for (int i = 0; i < springCount; i++) {
				sr.line(x - springWidth, i * springHeight, x + springWidth, (i + 1) * springHeight);
			}
			for (int i = 0; i < springCount; i++) {
				sr.line(x + springWidth, i * springHeight, x - springWidth, (i + 1) * springHeight);
			}
			if (state == State.HELD) {
				sr.line(x, y, x, springMiddle + (springMiddle - y) * 3 + height);
			}
		}
		sr.end();
		super.render(sr);
	}
}
