package com.game.tankwars;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.game.tankwars.TankWarsGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(720, 1600);
		config.setForegroundFPS(60);
		config.setTitle("Tank Wars");
		new Lwjgl3Application(new TankWarsGame(), config);
	}
}
