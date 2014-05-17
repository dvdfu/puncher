package com.dvdfu.puncher;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dvdfu.puncher.handlers.Vars;
import com.dvdfu.puncher.states.Game;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Puncher";
		cfg.width = Vars.WINDOW_WIDTH;
		cfg.height = Vars.WINDOW_HEIGHT;
		cfg.resizable = false;
		new LwjglApplication(new Game(), cfg);
	}
}
