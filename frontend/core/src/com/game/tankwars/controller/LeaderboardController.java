package com.game.tankwars.controller;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.game.tankwars.Callback;
import com.game.tankwars.ConfigReader;
import com.game.tankwars.HTTPRequestHandler;
import com.game.tankwars.TankWarsGame;
import com.game.tankwars.model.User;
import com.game.tankwars.view.LeaderboardScreen;
import com.game.tankwars.view.MainMenuScreen;

public class LeaderboardController {

    private final TankWarsGame tankWarsGame;
    private final Button backButton;
    private final LeaderboardScreen screen;

    public LeaderboardController(final TankWarsGame tankWarsGame, Button backButton, LeaderboardScreen screen) {
        this.tankWarsGame = tankWarsGame;
        this.backButton = backButton;
        this.screen = screen;

        setEventListeners();
        fetchLeaderboard();
    }

    private void setEventListeners() {
        /*
         * Transition back to main menu
         */
        backButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                tankWarsGame.setScreen(new MainMenuScreen(tankWarsGame));
                return true;
            }
        });
    }

    public void fetchLeaderboard() {

        new HTTPRequestHandler(
                new Callback() {
                    @Override
                    public void onResult(Net.HttpResponse response) {
                        Json json = new Json();
                        // Convert the response body to an Array of User objects using the Json instance
                        Array<User> leaderboardUsers = json.fromJson(Array.class, User.class, response.getResultAsString());

                        screen.setLeaderBoard(leaderboardUsers);
                    }

                    @Override
                    public void onFailed(Throwable t) {
                        screen.setLeaderBoard(null);
                    }
                },
                new HttpRequestBuilder()
                        .newRequest()
                        .url(String.format("%s/highscore", ConfigReader.getProperty("backend.url")))
                        .method(Net.HttpMethods.GET)
                        .build())
                .sendRequest();
    }
}
