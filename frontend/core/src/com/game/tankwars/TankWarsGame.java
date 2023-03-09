/* NOTE: This class now extends the GDX Game-class.
*  Previous input handling code is moved to the GameScreen-Class (for now).
*/
package com.game.tankwars;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.game.tankwars.model.Tank;
import com.game.tankwars.view.MainMenuScreen;

public class TankWarsGame extends Game {

	@Override
	public void create() {

		MainMenuScreen mainMenuScreen = new MainMenuScreen(this);
		this.setScreen(mainMenuScreen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {

	}
}
