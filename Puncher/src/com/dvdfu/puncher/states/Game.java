package com.dvdfu.puncher.states;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
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
		for (int i = 0; i < 10; i++) {
			objects.add(new Obstacle(MathUtils.random(Vars.SCREEN_WIDTH), MathUtils.random(Vars.SCREEN_HEIGHT)));
		}
		screenInput = new Vector3();
		timer = 0;
	}

	public void dispose() {}

	public void render() {
		timer++;
		if (timer == 30) {
			objects.add(new Gem(MathUtils.random(Vars.SCREEN_WIDTH)));
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
			GameObject o = objects.get(i);
			if (!player.attacking()) {
				o.update();
			}
			o.render(sb);
			if (o instanceof Obstacle) {
				if (o.getBody().overlaps(player.getBody())) {
					if (player.attacking()) {
						objects.add(new Debris(o.getX(), o.getY(), MathUtils.random(-4, 4), MathUtils.random(8), "img/debris2.png"));
						objects.add(new Debris(o.getX(), o.getY(), MathUtils.random(-4, 4), MathUtils.random(8), "img/debris2.png"));
						objects.add(new Debris(o.getX(), o.getY(), MathUtils.random(-4, 4), MathUtils.random(8), "img/debris2.png"));
						objects.add(new Debris(o.getX(), o.getY(), MathUtils.random(-4, 4), MathUtils.random(8), "img/debris2.png"));
						objects.remove(o);
						i--;
					}
				}
			} else if (o instanceof Gem) {
				if (o.getY() + 8 < 0) {
					o.setDead(true);
				}
				if (o.getBody().overlaps(player.getBody())) {
					if (player.attacking() && !((Gem) o).held) {
						o.setDead(true);
					} else if (player.moving()) {
						((Gem) o).held = true;
						player.carry((Gem) o);
					}
				}
				if (o.getDead()) {
					objects.add(new Debris(o.getX(), o.getY(), MathUtils.random(-4, 4), MathUtils.random(8), "img/debris.png"));
					objects.add(new Debris(o.getX(), o.getY(), MathUtils.random(-4, 4), MathUtils.random(8), "img/debris.png"));
					objects.add(new Debris(o.getX(), o.getY(), MathUtils.random(-4, 4), MathUtils.random(8), "img/debris.png"));
					objects.add(new Debris(o.getX(), o.getY(), MathUtils.random(-4, 4), MathUtils.random(8), "img/debris.png"));
					objects.remove(o);
					i--;
				}
			} else if (o instanceof Debris) {
				if (((Debris) o).isDead()) {
					objects.remove(o);
					i--;
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
