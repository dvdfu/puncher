package com.dvdfu.puncher.states;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.dvdfu.puncher.entities.Bullet;
import com.dvdfu.puncher.entities.Debris;
import com.dvdfu.puncher.entities.Gem;
import com.dvdfu.puncher.entities.Obstacle;
import com.dvdfu.puncher.entities.Player;
import com.dvdfu.puncher.handlers.Camera2D;
import com.dvdfu.puncher.handlers.GameObject;
import com.dvdfu.puncher.handlers.Input;
import com.dvdfu.puncher.handlers.InputProcessor;
import com.dvdfu.puncher.handlers.Vars;

public class Game implements ApplicationListener {
	private Player player;
	private ArrayList<GameObject> objects;
	private ShapeRenderer sr;
	private SpriteBatch sb;
	private Camera2D cc;
	public static Vector3 screenInput;
	private int timer;

	public void create() {
		Gdx.input.setInputProcessor(new InputProcessor());
		player = new Player();
		sr = new ShapeRenderer();
		sb = new SpriteBatch();
		cc = new Camera2D(Vars.SCREEN_WIDTH, Vars.SCREEN_HEIGHT);
		// cc.setPan(20);
		cc.setTarget(Vars.SCREEN_WIDTH / 2, Vars.SCREEN_HEIGHT / 2);
		objects = new ArrayList<GameObject>();
		screenInput = new Vector3();
		timer = 0;
	}

	public void dispose() {}

	private void newObstacle(float x, float y, float r) {
		if (r <= 1) {
			float rx = x + MathUtils.random(-1, 1) * 16;
			float ry = y + MathUtils.random(-1, 1) * 16;
			objects.add(new Obstacle(rx, ry));
			newObstacle(rx, ry, MathUtils.random(2));
		}
	}

	public void render() {
		Gdx.graphics.setTitle("" + objects.size());
		timer++;
		if (timer == 5) {
			float rx = (int) (MathUtils.random(Vars.SCREEN_WIDTH) / 16) * 16 + 8;
			float ry = Vars.SCREEN_HEIGHT + 8;
			objects.add(new Gem(MathUtils.random(Vars.SCREEN_WIDTH)));
			//objects.add(new Obstacle(rx, ry));
			//newObstacle(rx, ry, MathUtils.random(2));
			timer = 0;
		}
		screenInput.set(Input.mouse.x, Input.mouse.y, 0);
		cc.unproject(screenInput);
		cc.update();
		sr.setProjectionMatrix(cc.combined);
		sb.setProjectionMatrix(cc.combined);
		//
		player.update();
		//
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
		//
		player.renderJoint(sb);
		for (int i = 0; i < objects.size(); i++) {
			GameObject object = objects.get(i);
			if (!player.attacking()) {
				object.update();
			}
			object.render(sb);
			if (object instanceof Obstacle) {
				Obstacle o = (Obstacle) object;
				if (o.getBody().overlaps(player.getBody())) {
					if (player.attacking()) {
						for (int j = 0; j < 4; j++) {
							float rx = MathUtils.random(-4, 4);
							float ry = MathUtils.random(-4, 4);
							objects.add(new Debris(o.getX() + rx, o.getY() + ry, rx, ry, "img/debris2.png"));
						}
						objects.remove(o);
						i--;
					}
				}
				if (o.getState() == Obstacle.State.REMOVE) {
					objects.remove(o);
					i--;
				}
			} else if (object instanceof Gem) {
				Gem g = (Gem) object;
				if (g.getBody().overlaps(player.getBody()) && g.getState() != Gem.State.GRAB) {
					if (player.getState() == Player.State.DRAG) {
						g.setState(Gem.State.GRAB);
						player.carry(g);
					} else if (player.getState() == Player.State.RELEASE) {
						g.setState(Gem.State.DESTROY);
					}
				}
				if (g.getState() == Gem.State.REMOVE) {
					objects.remove(g);
					i--;
				} else if (g.getState() == Gem.State.DESTROY) {
					for (int j = 0; j < 4; j++) {
						float rx = MathUtils.random(-4, 4);
						float ry = MathUtils.random(8);
						objects.add(new Debris(g.getX() + rx, g.getY() + ry, rx, ry, "img/debris.png"));
					}
					objects.remove(g);
					i--;
				} /*else if (g.getState() == Gem.State.GRAB) {
					if (g.getTimer() == 10) {
						objects.add(new Bullet(g.getX(), g.getY()));
					}
				}*/
			} else if (object instanceof Debris) {
				if (((Debris) object).isDead()) {
					objects.remove(object);
					i--;
				}
			} else if (object instanceof Bullet) {
				if (((Bullet) object).isDead()) {
					objects.remove(object);
					i--;
				}
				for (GameObject go : objects) {
					if (go instanceof Obstacle && go.getBody().overlaps(object.getBody())) {
						((Bullet) object).kill();
					}
				}
			}
		}
		player.render(sb);
		//
		Input.update();
	}

	public void resize(int width, int height) {}

	public void pause() {}

	public void resume() {}
}
