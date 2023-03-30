/* NOTE: This class now extends the GDX Game-class.
*  Previous input handling code is moved to the GameScreen-Class (for now).
*/
package com.game.tankwars;

import com.badlogic.gdx.Game;
import com.game.tankwars.view.LoginScreen;

public class TankWarsGame extends Game {

	public static int VIEWPORT_WIDTH = 320;
	public static int VIEWPORT_HEIGHT = 240;

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
