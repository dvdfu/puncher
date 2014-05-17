package com.dvdfu.puncher.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dvdfu.puncher.handlers.GameObject;
import com.dvdfu.puncher.handlers.Input;
import com.dvdfu.puncher.handlers.Vars;
import com.dvdfu.puncher.states.Game;

public class Player extends GameObject {
	private enum State {
		IDLE, HELD, SHOT, HURT, ITEM
	};

	private State state;
	private int springCount;
	private Vector2 springMiddle;
	private float t;
	private float dy;
	private float dx;
	private float angle;
	private TextureRegion joint;

	public Player() {
		super(Vars.SCREEN_WIDTH / 2, 80, 24, 24);
		springMiddle = new Vector2(Vars.SCREEN_WIDTH / 2, Vars.SCREEN_HEIGHT / 4);
		xOffset = -width / 2;
		yOffset = -height / 2;
		switchState(State.IDLE);
		springCount = 12;
		setSprite(new TextureRegion(new Texture(Gdx.files.internal("img/blob.png"))));
		joint = new TextureRegion(new Texture(Gdx.files.internal("img/joint.png")));
		xSpriteOffset = -16;
		ySpriteOffset = -16;
	}

	private void switchState(State s) {
		Vars.timescale = 1;
		switch (s) {
		case IDLE:
			t = 0;
			dy = y - springMiddle.y;
			dx = x - springMiddle.x;
			break;
		case HELD:
			break;
		case SHOT:
			t = 0;
			dy = (springMiddle.y - y) * 3;
			dx = (springMiddle.x - x) * 3;
			break;
		}
		state = s;
	}

	public void update() {
		switch (state) {
		case IDLE:
			y = springMiddle.y + dy * MathUtils.cos(t) / (1 + t);
			x = springMiddle.x + dx * MathUtils.cos(t) / (1 + t);
			if (Input.MouseDown() && t > Math.PI / 2) {
				if (new Rectangle(x - 32, y - 32, 64, 64).contains(Game.screenInput.x, Game.screenInput.y)) {
					switchState(State.HELD);
				}
			}
			t += Vars.timescale / 4;
			break;
		case HELD:
			if (Input.MouseDown()) {
				x = Game.screenInput.x;
				y = Game.screenInput.y;
			} else {
				if (Game.screenInput.y > springMiddle.y) {
					switchState(State.IDLE);
				} else {
					switchState(State.SHOT);
				}
			}
			break;
		case SHOT:
			y = springMiddle.y + dy * MathUtils.sin(t);
			x = springMiddle.x + dx * MathUtils.sin(t);
			if (t > MathUtils.PI * 3 / 4) {
				switchState(State.IDLE);
			}
			t += Vars.timescale / 8;
			break;
		}
		angle = MathUtils.atan2(y - springMiddle.y, x - springMiddle.x) * MathUtils.radiansToDegrees + 180;
		super.update();
	}
	
	public boolean attacking() {
		return state == State.SHOT;
	}

	public void render(ShapeRenderer sr) {
		// super.render(sr);
		sr.begin(ShapeType.Filled);
		if (y < springMiddle.y) {
			if (state == State.HELD) {
				float px = springMiddle.x + (springMiddle.x - x) * 3;
				float py = springMiddle.y + (springMiddle.y - y) * 3;
				int n = 128;
				for (int i = 0; i < n; i++) {
					sr.setColor(1, 0, 0, 1);
					sr.circle(x + i * (px - x) / n, y + i * (py - y) / n, 16);
				}
			}
		}
		sr.end();
	}

	public void render(SpriteBatch sb) {
		sb.begin();
		for (int i = 0; i < springCount; i++) {
			sb.draw(joint, springMiddle.x + i * (x - springMiddle.x) / springCount - 4, springMiddle.y - i * (springMiddle.y - y) / springCount - 4);
		}
		if (sprite.exists()) {
			// sb.draw(sprite.getFrame(), x + xSpriteOffset, y + ySpriteOffset, -xSpriteOffset, -ySpriteOffset, spriteWidth, spriteHeight, 1, 1, angle, true);
			sb.draw(sprite.getFrame(), x + xSpriteOffset, y + ySpriteOffset);
		}
		sb.end();
	}
}
