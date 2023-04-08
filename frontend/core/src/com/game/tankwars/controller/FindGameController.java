package com.game.tankwars.controller;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.tankwars.ResourceManager;
import com.game.tankwars.TankWarsGame;
import com.game.tankwars.view.GameScreen;
import com.game.tankwars.view.MainMenuScreen;

public class FindGameController {

    private final TankWarsGame tankWarsGame;
    private final Stage stage;
    private final TextField gamePinField;
    private final TextButton joinLobbyButton, createLobbyButton;
    private final Button backButton;

    /**
     * Sets the event listeners of the buttons and the text field of the FindGameScreen,
     * and allows for transitioning to MainMenuScreen and to GameScreen.
     */
    public FindGameController(final TankWarsGame tankWarsGame,
                              TextField gamePinField, TextButton joinLobbyButton,
                              TextButton createLobbyButton, Button backButton, final Stage stage) {
        this.tankWarsGame = tankWarsGame;
        this.gamePinField = gamePinField;
        this.joinLobbyButton = joinLobbyButton;
        this.createLobbyButton = createLobbyButton;
        this.backButton = backButton;
        this.stage = stage;

        setEventListeners();
    }

    public void setEventListeners() {

        /*
         * Transitions back to MainMenuScreen
         */
        backButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                tankWarsGame.setScreen(new MainMenuScreen(tankWarsGame));
                return true;
            }
        });

        /*
         * Filters text field input:
         * Max 4 characters long and only digits
         */
        gamePinField.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                return textField.getText().length() < 4 && Character.isDigit(c);
            }
        });

        /*
         * Enables the joinLobbyButton when the gamePinField contains 4 digits,
         * and disables it otherwise
         */
        gamePinField.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                super.keyTyped(event, character);

                if (event.getKeyCode() == 66) {
                    gamePinField.getOnscreenKeyboard().show(false);
                    stage.unfocus(gamePinField);
                    stage.getViewport().setScreenY(0);
                    stage.getViewport().apply();
                }

                joinLobbyButton.setDisabled(gamePinField.getText().length() != 4);

                return true;
            }
        });

        /*
         * Move camera down when text field is clicked
         * to make the field appear above the keyboard.
         */
        gamePinField.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                stage.getViewport().setScreenY((int) (2 * stage.getHeight() / 3));
                stage.getViewport().apply();
            }
        });

        /*
         * Disables input listener when the button is disabled.
         * TODO: Join a lobby by sending a request to the backend
         */
        joinLobbyButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (joinLobbyButton.isDisabled()) return true;

                System.out.println("Game pin: " + gamePinField.getText() + " - yet to be implemented");
                return true;
            }
        });

        /*
         * TODO: Create a lobby by sending request to backend - Transition to waiting screen?
         */
        createLobbyButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ResourceManager.getInstance().clear();
                tankWarsGame.setScreen(new GameScreen(tankWarsGame));
                return true;
            }
        });
    }

}
