package com.mygdx.game.desktop;

import Engine.System.Platforms.Desktop.Desktop;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = false;
		config.vSyncEnabled = true;
		//config.foregroundFPS = 120;
		new LwjglApplication(new Desktop(), config);
	}
}
