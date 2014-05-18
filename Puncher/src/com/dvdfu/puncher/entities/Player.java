package com.dvdfu.puncher.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.dvdfu.puncher.handlers.GameObject;
import com.dvdfu.puncher.handlers.Input;
import com.dvdfu.puncher.handlers.Vars;
import com.dvdfu.puncher.states.Game;

public class Player extends GameObject {
	public enum State {
		IDLE, DRAG, RELEASE, HURT, ITEM
	};

	private Array<Gem> gems;
	private State state;
	private int springCount;
	private Vector2 springMiddle;
	private float t;
	private float dy;
	private float dx;
	private float angle;
	private TextureRegion joint;

	public Player() {
		super(Vars.SCREEN_WIDTH / 2, 80, 32, 32);
		gems = new Array<Gem>();
		springMiddle = new Vector2(Vars.SCREEN_WIDTH / 2, Vars.SCREEN_HEIGHT / 4);
		xOffset = -width / 2;
		yOffset = -height / 2;
		switchState(State.IDLE);
		springCount = 12;
		setSprite("img/blob.png");
		joint = new TextureRegion(new Texture(Gdx.files.internal("img/joint.png")));
		xSpriteOffset = -16;
		ySpriteOffset = -16;
	}

	private void switchState(State s) {
		Vars.timescale = 1;
		t = 0;
		switch (s) {
		case IDLE:
			setSprite("img/blob.png");
			dy = y - springMiddle.y;
			dx = x - springMiddle.x;
			for (Gem g : gems) {
				g.setState(Gem.State.COLLECT1);
			}
			break;
		case DRAG:
			setSprite("img/hold.png");
			break;
		case RELEASE:
			setSprite("img/hurt.png");
			dy = (springMiddle.y - y) * 3;
			dx = (springMiddle.x - x) * 3;
			break;
		}
		state = s;
	}

	public void update() {
		switch (state) {
		case IDLE:
			if (t % 3 >= 2 && gems.size > 0) {
				Gem g = gems.get(gems.size - 1);
				g.setState(Gem.State.COLLECT2);
				gems.removeIndex(gems.size - 1);
			}
			y = springMiddle.y + dy * MathUtils.cos(t / 8) / (1 + t / 8);
			x = springMiddle.x + dx * MathUtils.cos(t / 8) / (1 + t / 8);
			if (Input.MouseDown() && t > Math.PI / 2) {
				if (new Rectangle(x - 32, y - 32, 64, 64).contains(Game.screenInput.x, Game.screenInput.y)) {
					switchState(State.DRAG);
				}
			}
			break;
		case DRAG:
			if (Input.MouseDown()) {
				x = Game.screenInput.x;
				y = Game.screenInput.y;
			} else {
				if (Game.screenInput.y > springMiddle.y) {
					switchState(State.IDLE);
				} else {
					switchState(State.RELEASE);
				}
			}
			if (gems.size > 0) {
				for (int i = 0; i < gems.size; i++) {
					Gem g = gems.get(i);
					if (g.getState() == Gem.State.GRAB) {
						int layer = 1;
						int innerGems = 0;
						int outerGemLimit = 6;
						while (true) {
							if (innerGems + outerGemLimit > i) {
								break;
							}
							innerGems += outerGemLimit;
							layer++;
							outerGemLimit += 6;
						}
						float radius = layer * 16;
						float angle = MathUtils.PI2 * (i - innerGems) / (gems.size - innerGems);
						if (gems.size >= outerGemLimit + innerGems) {
							angle = MathUtils.PI2 * (i % outerGemLimit) / outerGemLimit;
						}
						if (layer % 2 == 0) {
							g.setPosition(x + radius * MathUtils.cos(angle - t / 40), y + radius * MathUtils.sin(angle - t / 40));
						} else {
							g.setPosition(x + radius * MathUtils.cos(angle + t / 40), y + radius * MathUtils.sin(angle + t / 40));
						}
					}
				}
			}
			break;
		case RELEASE:
			y = springMiddle.y + dy * MathUtils.sin(t / 16);
			x = springMiddle.x + dx * MathUtils.sin(t / 16);
			if (t / 16 > MathUtils.PI * 3 / 4) {
				switchState(State.IDLE);
			}
			break;
		}
		t += Vars.timescale;
		angle = MathUtils.atan2(y - springMiddle.y, x - springMiddle.x) * MathUtils.radiansToDegrees + 180;
		super.update();
	}

	public void carry(Gem gem) {
		if (!gems.contains(gem, false)) {
			gems.add(gem);
		}
	}

	public boolean attacking() {
		return state == State.RELEASE;
	}

	public boolean moving() {
		return state == State.DRAG;
	}

	public State getState() {
		return state;
	}

	public void renderJoint(SpriteBatch sb) {
		sb.begin();
		if (y < springMiddle.y) {
			if (state == State.DRAG) {
				float px = springMiddle.x + (springMiddle.x - x) * 3;
				float py = springMiddle.y + (springMiddle.y - y) * 3;
				for (int i = 0; i <= springCount; i++) {
					sb.draw(joint, x + i * (px - x) / springCount - 4, y + i * (py - y) / springCount - 4);
				}
			}
		}
		sb.end();
	}

	public void render(SpriteBatch sb) {
		sb.begin();
		if (sprite.exists()) {
			// sb.draw(sprite.getFrame(), x + xSpriteOffset, y + ySpriteOffset,
			// -xSpriteOffset, -ySpriteOffset, spriteWidth, spriteHeight, 1, 1,
			// angle, true);
			sb.draw(sprite.getFrame(), x + xSpriteOffset, y + ySpriteOffset);
		}
		sb.end();
	}
}
