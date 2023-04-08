package com.game.tankwars.controller;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.game.tankwars.ResourceManager;
import com.game.tankwars.TankWarsGame;
import com.game.tankwars.view.FindGameScreen;
import com.game.tankwars.view.GameScreen;
import com.game.tankwars.view.LeaderboardScreen;
import com.game.tankwars.view.LoginScreen;

/**
 * Sets the event listeners for the buttons
 * on the MainMenuScreen
 */
public class MainMenuController {

    private final TankWarsGame tankWarsGame;
    private final TextButton findGameButton, highScoreButton, settingsButton, logoutButton;

    public MainMenuController(final TankWarsGame tankWarsGame, TextButton findGameButton,
                              TextButton highScoreButton, TextButton settingsButton,
                              TextButton logoutButton) {
        this.tankWarsGame = tankWarsGame;
        this.findGameButton = findGameButton;
        this.highScoreButton = highScoreButton;
        this.settingsButton = settingsButton;
        this.logoutButton = logoutButton;

        setEventListeners();
    }

    private void setEventListeners() {
        /*
         * Transition to FindGameScreen
         */
        findGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                tankWarsGame.setScreen(new FindGameScreen(tankWarsGame));
                return true;
            }
        });

        /*
         * Transition to LeaderboardScreen
         */
        highScoreButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                tankWarsGame.setScreen(new LeaderboardScreen(tankWarsGame));
                return true;
            }
        });

        /*
         * Transition to SettingsScreen (Dummy button for now)
         * TODO: Implement the SettingsScreen class and add the transition
         */
        settingsButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Settings screen: yet to be implemented");
                return true;
            }
        });

        /*
         * Transition to LoginScreen
         * Log out the user
         * TODO: Log out the user - must be done after login functionality is implemented
         */
        logoutButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                tankWarsGame.setScreen(new LoginScreen(tankWarsGame));
                return true;
            }
        });
    }
}
