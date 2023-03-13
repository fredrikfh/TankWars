/* NOTE: This class now extends the GDX Game-class.
*  Previous input handling code is moved to the GameScreen-Class (for now).
*/
package com.game.tankwars;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.game.tankwars.model.Tank;
import com.game.tankwars.view.MainMenuScreen;

public class TankWarsGame extends Game {

	private SpriteBatch batch;
	private BitmapFont font;
	private OrthographicCamera camera;

	@Override
	public void create() {

		batch = new SpriteBatch();
		// Font from https://www.fontspace.com/roll-accurate-font-f32330
		font = generateFontFromTTFFile("RollAccurate-mvrx.ttf");
		// Camera size set to main menu dimensions: portrait mode
		camera = new OrthographicCamera(224f,
				224f * Gdx.graphics.getHeight() / Gdx.graphics.getWidth());

		MainMenuScreen mainMenuScreen = new MainMenuScreen(this, batch, font, camera);
		this.setScreen(mainMenuScreen);
	}

	/**
	 * Generates a BitmapFont from a .ttf font file with higher scaling than the original
	 * to allow for better font resolution.
	 *
	 * @param internalPath file path to the .ttf file relative to assets folder
	 * @return BitmapFont
	 */
	private BitmapFont generateFontFromTTFFile(String internalPath) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(internalPath));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 32; // Set max font size
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();

		return font;
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {

	}
}
