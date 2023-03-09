package com.game.tankwars.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.game.tankwars.TankWarsGame;
import com.game.tankwars.model.Tank;

public class GameScreen implements Screen {
    final TankWarsGame tankWarsGame;
    OrthographicCamera camera;
    SpriteBatch batch;
    Tank tank;

    public GameScreen(final TankWarsGame tankWarsGame){
        this.tankWarsGame = tankWarsGame;
        camera = new OrthographicCamera();

        batch = new SpriteBatch();
        tank = new Tank(new Vector2(50, 50), new Texture("tank-khaki.png"));
    }
    @Override
    public void render(float delta) {
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
    public void show() {

    }
    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
