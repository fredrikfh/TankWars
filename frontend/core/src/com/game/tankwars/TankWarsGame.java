package com.game.tankwars;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.game.tankwars.model.Tank;

public class TankWarsGame extends ApplicationAdapter {
	SpriteBatch batch;

	Tank tank;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		tank = new Tank(new Vector2(50, 50), new Texture("tank-khaki.png"));
	}

	@Override
	public void render () {

		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			tank.moveRight();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			tank.moveLeft();
		}

		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin();
		batch.draw(tank.getTexture(), tank.getPosition().x, tank.getPosition().y, Tank.TANK_WIDTH, Tank.TANK_HEIGHT);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		tank.getTexture().dispose();
	}
}
