/* NOTE: This class now extends the GDX Game-class.
*  Previous input handling code is moved to the GameScreen-Class (for now).
*/
package com.game.tankwars;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.game.tankwars.model.Box2dWorld;
import com.game.tankwars.model.Bullet;
import com.game.tankwars.model.Tank;
import com.game.tankwars.view.LoginScreen;
import com.game.tankwars.view.MainMenuScreen;

public class TankWarsGame extends Game {

	public static int VIEWPORT_WIDTH = 80;
	public static int VIEWPORT_HEIGHT = 50;

	private SpriteBatch batch;
	private BitmapFont font;

	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
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
		batch.dispose();
		font.dispose();
	}
}
