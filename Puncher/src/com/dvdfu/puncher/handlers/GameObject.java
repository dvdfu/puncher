package com.dvdfu.puncher.handlers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class GameObject {
	protected Sprite sprite;
	protected Rectangle body;
	protected float x;
	protected float y;
	protected float width;
	protected float height;
	protected float xOffset;
	protected float yOffset;
	protected float xSpriteOffset;
	protected float ySpriteOffset;
	protected float spriteWidth;
	protected float spriteHeight;

	public GameObject() {
		this(0, 0, 0, 0);
	}

	public GameObject(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		body = new Rectangle(x, y, width, height);
		xOffset = 0;
		yOffset = 0;
		xSpriteOffset = 0;
		ySpriteOffset = 0;
		spriteWidth = 0;
		spriteHeight = 0;
		sprite = new Sprite();
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setOffset(float xOffset, float yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public void setSpriteOffset(float xSpriteOffset, float ySpriteOffset) {
		this.xSpriteOffset = xSpriteOffset;
		this.ySpriteOffset = ySpriteOffset;
	}

	public void setBody(float width, float height) {
		this.width = width;
		this.height = height;
	}
	

	public Rectangle getBody() {
		return body;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public Sprite getSprite() {
		return sprite;
	}
	
	public void setSprite(String filename) {
		sprite.setSprite(filename);
		spriteWidth = sprite.getFrame().getRegionWidth();
		spriteHeight = sprite.getFrame().getRegionHeight();
	}
	
	public void setSprite(TextureRegion reg) {
		sprite.setSprite(reg);
		spriteWidth = reg.getRegionWidth();
		spriteHeight = reg.getRegionHeight();
	}

	public void setSprite(TextureRegion[] reg) {
		sprite.setSprite(reg);
		spriteWidth = reg[0].getRegionWidth();
		spriteHeight = reg[0].getRegionHeight();
	}
	
	public void update() {
		sprite.update();
		body.x = x + xOffset;
		body.y = y + yOffset;
	}

	public void render(SpriteBatch sb) {
		if (sprite.exists()) {
			sb.begin();
			sb.draw(sprite.getFrame(), x + xSpriteOffset, y + ySpriteOffset);
			sb.end();
		}
	}

	public void render(ShapeRenderer sr) {
		sr.begin(ShapeType.Line);

		sr.setColor(1, 0, 0, 1);
		sr.rect(x + xOffset, y + yOffset, width - 1, height - 1);
		if (sprite.exists()) {
			sr.setColor(0, 1, 0, 1);
			sr.rect(x + xSpriteOffset, y + ySpriteOffset, spriteWidth - 1, spriteHeight - 1);
		}
		sr.setColor(0, 0, 1, 1);
		sr.line(x - 8, y, x + 8, y);
		sr.line(x, y - 8, x, y + 8);
		

		sr.end();
	}
}
