package com.game.tankwars.view;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.game.tankwars.TankWarsGame;

public class MainMenuScreen implements Screen {
    final TankWarsGame tankWarsGame;
    OrthographicCamera camera;

    public MainMenuScreen(final TankWarsGame tankWarsGame){
        this.tankWarsGame = tankWarsGame;
        camera = new OrthographicCamera();

        //NB! Temporarily directing to GameScreen, this is subject to change
        GameScreen gameScreen = new GameScreen(this.tankWarsGame);
        tankWarsGame.setScreen(gameScreen);
    }

    @Override
    public void render(float delta) {

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
