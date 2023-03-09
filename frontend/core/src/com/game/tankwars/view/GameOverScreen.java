package com.game.tankwars.view;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.game.tankwars.TankWarsGame;

public class GameOverScreen implements Screen {

    final TankWarsGame tankWarsGame;
    OrthographicCamera camera;

    public GameOverScreen(final TankWarsGame tankWarsGame){
        this.tankWarsGame = tankWarsGame;
        camera = new OrthographicCamera();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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
