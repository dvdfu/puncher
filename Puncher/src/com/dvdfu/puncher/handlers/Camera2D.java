package com.dvdfu.puncher.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class Camera2D extends OrthographicCamera {
	private float pan;
	private Vector2 target;

	public Camera2D(float viewWidth, float viewHeight) {
		super(viewWidth, viewHeight);
		setToOrtho(false, viewWidth, viewHeight);
		pan = 0;
		target = new Vector2(0, 0);
	}

	public void setPan(float pan) {
		this.pan = pan;
	}

	public void setTarget(float x, float y) {
		target.set(x, y);
	}

	public void update() {
		if (target != null) {
			if (pan <= 1) {
				position.x = target.x;
				position.y = target.y;
			} else if (Math.abs(position.len() - target.len()) > 1) {
				position.x += (target.x - position.x) / pan;
				position.y += (target.y - position.y) / pan;
			}
		}
		super.update();
	}
}
