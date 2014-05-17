package com.dvdfu.puncher.states;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.dvdfu.puncher.entities.Player;
import com.dvdfu.puncher.handlers.Camera2D;
import com.dvdfu.puncher.handlers.Input;
import com.dvdfu.puncher.handlers.InputProcessor;
import com.dvdfu.puncher.handlers.Vars;

public class Game implements ApplicationListener {
	private Player go;
	private ShapeRenderer sr;
	private SpriteBatch sb;
	private Camera2D cc;
	public static Vector3 screenInput;

	public void create() {
		Gdx.input.setInputProcessor(new InputProcessor());
		go = new Player();
		sr = new ShapeRenderer();
		sb = new SpriteBatch();
		cc = new Camera2D(Vars.SCREEN_WIDTH, Vars.SCREEN_HEIGHT);
		// cc.setPan(20);
		cc.setTarget(Vars.SCREEN_WIDTH / 2, Vars.SCREEN_HEIGHT / 2);
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
		go.update();
		//
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
		//
		go.render(sr);
		go.render(sb);
		//
		Input.update();
	}

	public void resize(int width, int height) {}

	public void pause() {}

	public void resume() {}
}
