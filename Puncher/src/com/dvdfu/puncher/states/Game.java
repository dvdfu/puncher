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
import com.dvdfu.puncher.entities.Player;
import com.dvdfu.puncher.handlers.Camera2D;
import com.dvdfu.puncher.handlers.Input;
import com.dvdfu.puncher.handlers.InputProcessor;
import com.dvdfu.puncher.handlers.Vars;

public class Game implements ApplicationListener {
	private Player player;
	private ArrayList<Gem> gems;
	private ArrayList<Debris> deb;
	private ShapeRenderer sr;
	private SpriteBatch sb;
	private Camera2D cc;
	public static Vector3 screenInput;

	public void create() {
		Gdx.input.setInputProcessor(new InputProcessor());
		player = new Player();
		sr = new ShapeRenderer();
		sb = new SpriteBatch();
		cc = new Camera2D(Vars.SCREEN_WIDTH, Vars.SCREEN_HEIGHT);
		// cc.setPan(20);
		cc.setTarget(Vars.SCREEN_WIDTH / 2, Vars.SCREEN_HEIGHT / 2);
		gems = new ArrayList<Gem>();
		for (int i = 0; i < 60; i++) {
			gems.add(new Gem(MathUtils.random(Vars.SCREEN_WIDTH)));
		}
		deb = new ArrayList<Debris>();
		screenInput = new Vector3();
	}

	public void dispose() {}

	public void render() {
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
		for (int i = 0; i < gems.size(); i++) {
			Gem g = gems.get(i);
			g.update();
			g.render(sb);
			if (g.dead) {
				g.setPosition(g.getX(), Vars.SCREEN_HEIGHT + 8);
			}
			//if (g.held) {
				//g.setPosition(player.getX(), player.getY() + 32);
			//}
			if (g.getBody().overlaps(player.getBody())) {
				if (player.attacking() && !g.held) {
					deb.add(new Debris(g.getX(), g.getY(), MathUtils.random(-4, 4), MathUtils.random(8)));
					deb.add(new Debris(g.getX(), g.getY(), MathUtils.random(-4, 4), MathUtils.random(8)));
					deb.add(new Debris(g.getX(), g.getY(), MathUtils.random(-4, 4), MathUtils.random(8)));
					deb.add(new Debris(g.getX(), g.getY(), MathUtils.random(-4, 4), MathUtils.random(8)));
					gems.remove(g);
					i --;
				} else if (player.moving()) {
					g.held = true;
					player.carry(g);
				}
			}
		}
		for (int i = 0; i < deb.size(); i++) {
			Debris d = deb.get(i);
			d.update();
			d.render(sb);
			if (d.isDead()) {
				deb.remove(d);
				i++;
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
