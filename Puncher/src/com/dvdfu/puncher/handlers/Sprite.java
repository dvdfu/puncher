package com.dvdfu.puncher.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Sprite {
	private TextureRegion[] frames;
	private float delay;
	private float time;
	private int frame;
	private int length;
	private boolean playedOnce;

	public Sprite() {
		this(new TextureRegion[] { null });
	}

	public Sprite(TextureRegion reg) {
		this(new TextureRegion[] { reg });
	}

	public Sprite(TextureRegion[] reg) {
		frames = reg;
		delay = Vars.SPRITE_SPF;
		time = 0;
		frame = 0;
		length = reg.length;
		playedOnce = false;
	}

	public void setDelay(float f) {
		if (f > 0) {
			delay = f;
		}
	}

	public void setCurrentFrame(int i) {
		frame = i % length;
	}

	public void setSprite(String filename) {
		setSprite(new TextureRegion(new Texture(Gdx.files.internal(filename))));
	}

	public void setSprite(TextureRegion reg) {
		setSprite(new TextureRegion[] { reg });
	}

	public void setSprite(TextureRegion[] reg) {
		if (frames != reg) {
			frames = reg;
			time = 0;
			frame = 0;
			length = reg.length;
		}
	}

	public void update() {
		if (length > 1) {
			time += Vars.SPF;
			while (time >= delay) {
				time -= delay;
				frame++;
				if (frame == length) {
					playedOnce = true;
					frame = 0;
				}
			}
		}
	}

	public TextureRegion getFrame() {
		return frames[frame];
	}

	public boolean playedOnce() {
		return playedOnce;
	}

	public boolean exists() {
		return frames[0] != null;
	}
}
