package com.game.tankwars.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.game.tankwars.TankWarsGame;
import com.game.tankwars.controller.LeaderboardController;
import com.game.tankwars.model.User;

import java.awt.Label;

public class LeaderboardScreen implements Screen {

    final TankWarsGame tankWarsGame;
    OrthographicCamera camera;
    SpriteBatch batch;
    private BitmapFont font;

    //LibGDX have tables or something, should be investigated
    Table table;

    private Array<User> leaderboardUsers;
    LeaderboardController leaderboardController;



    public LeaderboardScreen (final TankWarsGame tankWarsGame) {
        this.tankWarsGame = tankWarsGame;
        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(3f);
        font.setColor(Color.BLACK);

        leaderboardController = new LeaderboardController();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(255, 255, 255, 1);
        int height = Gdx.graphics.getHeight() - 75;
        batch.begin();
        leaderboardUsers = leaderboardController.getLeaderboard();

        if (leaderboardUsers == null) {
            font.draw(batch, "No connection", 50, height);
        }
        else if (leaderboardUsers.size > 0) {
            for (User user : leaderboardUsers) {
                font.draw(batch, user.username, (Gdx.graphics.getWidth() >> 1) - 300, height);
                font.draw(batch, user.wins + "", (Gdx.graphics.getWidth() >> 1), height);
                height = height - 75;
            }
        }
        else {
            font.draw(batch, "No users", 50, height);
        }
        batch.end();
    }

    @Override
    public void dispose() {

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

}
