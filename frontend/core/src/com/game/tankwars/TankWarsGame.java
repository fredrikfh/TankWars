/* NOTE: This class now extends the GDX Game-class.
*  Previous input handling code is moved to the GameScreen-Class (for now).
*/
package com.game.tankwars;

import com.badlogic.gdx.Game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.game.tankwars.view.FindGameScreen;
import com.game.tankwars.view.GameScreen;
import com.game.tankwars.view.LoginScreen;

public class TankWarsGame extends Game {

	public static final float SCALE = 10;
	public static int VIEWPORT_WIDTH = 320;
	public static int VIEWPORT_HEIGHT = 240;

	public static final int GAMEPORT_WIDTH = 720;
	public static final int GAMEPORT_HEIGHT = 480;

	private SpriteBatch batch;
	private BitmapFont font;

	@Override
	public void create() {
		this.setScreen(new LoginScreen(this));
	}

	public int getViewportWidth(){
		return VIEWPORT_WIDTH;
	}

	public int getViewportHeight(){
		return VIEWPORT_HEIGHT;
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		ResourceManager.getInstance().dispose();
	}
}
