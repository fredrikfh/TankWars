package com.game.tankwars.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.SerializationException;
import com.game.tankwars.Callback;
import com.game.tankwars.ConfigReader;
import com.game.tankwars.HTTPRequestHandler;
import com.game.tankwars.ResourceManager;
import com.game.tankwars.TankWarsGame;
import com.game.tankwars.model.CurrentUser;
import com.game.tankwars.model.LobbyId;
import com.game.tankwars.model.LobbyStatus;
import com.game.tankwars.view.FindGameScreen;
import com.game.tankwars.view.GameScreen;
import com.game.tankwars.view.MainMenuScreen;


public class FindGameController {

    private final TankWarsGame tankWarsGame;
    private final FindGameScreen screen;
    private final Stage stage;
    private final TextField gamePinField;
    private final TextButton joinLobbyButton, cancelButton;
    private final Button backButton;
    private final Label gamePinWaitingLabel;

    private EventListener backButtonInputListener, gamePinFieldInputListener,
            gamePinFieldClickListener, joinLobbyButtonInputListener,
            createLobbyButtonInputListener, cancelButtonInputListener;

    private String lobbyId = null;
    private final Runnable gameScreenTransition;


    /**
     * Sets the event listeners of the buttons and the text field of the FindGameScreen,
     * and allows for transitioning to MainMenuScreen and to GameScreen. Handles communication
     * with server to create, join and leave a lobbies.
     */
    public FindGameController(final TankWarsGame tankWarsGame, FindGameScreen screen,
                              TextField gamePinField, TextButton joinLobbyButton,
                              Button backButton, TextButton cancelButton,
                              Label gamePinWaitingLabel, final Stage stage) {
        this.tankWarsGame = tankWarsGame;
        this.screen = screen;

        this.gamePinField = gamePinField;
        this.joinLobbyButton = joinLobbyButton;
        this.backButton = backButton;
        this.cancelButton = cancelButton;
        this.gamePinWaitingLabel = gamePinWaitingLabel;
        this.stage = stage;

        // This Runnable will be passed to the "render" thread
        // using Gdx.app.postRunnable() from the threads of HTTP requests
        gameScreenTransition = new Runnable() {
            @Override
            public void run() {
                ResourceManager.getInstance().clear();
                tankWarsGame.setScreen(new GameScreen(tankWarsGame));
            }
        };

        setGamePinFieldFilter();
        defineEventListenersAndFilters();
        setMainListeners();
    }

    /**
     * Game pin field only allows for integers
     * and the first integer must be greater than 0
     */
    private void setGamePinFieldFilter() {
        gamePinField.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                return Character.isDigit(c) &&
                        (!textField.getText().isEmpty() || (textField.getText().isEmpty() && c != '0'));
            }
        });
    }

    private void defineEventListenersAndFilters() {
        /*
         * Transition back to MainMenuScreen
         */
        backButtonInputListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                tankWarsGame.setScreen(new MainMenuScreen(tankWarsGame));
                return true;
            }
        };

        /*
         * Remove keyboard and reset camera position when "Enter" button is pressed.
         * Enable the joinLobbyButton when gamePinField contains at least one character.
         */
        gamePinFieldInputListener = new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                super.keyTyped(event, character);

                if (event.getKeyCode() == 66) {
                    gamePinField.getOnscreenKeyboard().show(false);
                    stage.unfocus(gamePinField);
                    stage.getViewport().setScreenY(0);
                    stage.getViewport().apply();
                }

                joinLobbyButton.setDisabled(gamePinField.getText().length() == 0);
                return true;
            }
        };

        /*
         * Move camera down when text field is clicked
         * to make the field appear above the keyboard.
         */
        gamePinFieldClickListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                stage.getViewport().setScreenY((int) (2 * stage.getHeight() / 3));
                stage.getViewport().apply();
            }
        };

        /*
         * Send HTTP request to join lobby with game pin specified in text field.
         * Avoid sending request if joinLobbyButton is disabled.
         */
        joinLobbyButtonInputListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (joinLobbyButton.isDisabled()) return true;

                joinLobby();
                return true;
            }
        };


        /*
         * Exit from waiting window and render normal FindGameScreen
         */
        cancelButtonInputListener = new InputListener() {
          @Override
          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
              exitLobby();
              return true;
          }
        };
    }


    public void setMainListeners() {
        backButton.addListener(backButtonInputListener);
        gamePinField.addListener(gamePinFieldInputListener);
        gamePinField.addListener(gamePinFieldClickListener);
        joinLobbyButton.addListener(joinLobbyButtonInputListener);
    }

    public void removeMainListeners() {
        backButton.removeListener(backButtonInputListener);
        gamePinField.removeListener(gamePinFieldInputListener);
        gamePinField.removeListener(gamePinFieldClickListener);
        joinLobbyButton.removeListener(joinLobbyButtonInputListener);
    }

    public void setWaitingWindowListeners() {
        cancelButton.addListener(cancelButtonInputListener);
    }

    public void removeWaitingWindowListeners() {
        cancelButton.removeListener(cancelButtonInputListener);
    }

    /**
     * Send HTTP request to join a lobby using the game pin in the text field.
     * On success, poll the server for the fill status of the lobby.
     */
    private void joinLobby() {
        lobbyId = gamePinField.getText();

        new HTTPRequestHandler(new Callback() {
            @Override
            public boolean onResult(Net.HttpResponse response) {
                HttpStatus status = response.getStatus();

                if (status.getStatusCode() == 200 || status.getStatusCode() == 201) {
                    removeMainListeners();
                    setWaitingWindowListeners();

                    gamePinWaitingLabel.setText("Game pin: " + lobbyId);
                    screen.showWaitingWindow();

                    checkLobbyStatus();
                    return true;
                } else if (status.getStatusCode() == 409) {
                    removeMainListeners();
                    setWaitingWindowListeners();

                    gamePinWaitingLabel.setText("Game pin: " + lobbyId);
                    screen.showWaitingWindow();
                    return true;
                } else if (status.getStatusCode() == 404 || status.getStatusCode() == 429) {
                    System.err.println(response.getResultAsString());
                }

                return false;
            }

            @Override
            public void onFailed(Throwable t) {
                System.err.println("Join lobby request failed:\n" + t);
            }
        }, new HttpRequestBuilder()
                .newRequest()
                .url(String.format("%s/lobby/%s/join", ConfigReader.getProperty("backend.url"), lobbyId))
                .method(Net.HttpMethods.POST)
                .header("Content-Type", "application/json")
                .content(String.format("{\"username\": \"%s\"}", CurrentUser.getCurrentUser().getUser().username))
                .build())
                .sendRequest();
    }

    /**
     * Send HTTP request while waiting for lobby to fill,
     * polling for the lobby's fill status with a set backoff period between requests.
     * Transition to game screen when lobby is full and set gameId in CurrentUser.
     */
    private void checkLobbyStatus() {
        if (lobbyId == null) return;

        new HTTPRequestHandler(new Callback() {
            @Override
            public boolean onResult(Net.HttpResponse response) {
                HttpStatus status = response.getStatus();

                if (status.getStatusCode() == 200 && !response.getResultAsString().isEmpty()) {
                    CurrentUser.getCurrentUser().setGameId(response.getResultAsString());
                    Gdx.app.postRunnable(gameScreenTransition);
                    return true;
                } else if (status.getStatusCode() == 404) {

                    System.out.println("Awaiting opponent...");

                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }

                    if (lobbyId != null) checkLobbyStatus();
                    return true;
                }

                return false;
            }

            @Override
            public void onFailed(Throwable t) {
                System.err.println("Check lobby status request failed:\n" + t);
            }
        }, new HttpRequestBuilder()
                .newRequest()
                .url(String.format("%s/game/%s", ConfigReader.getProperty("backend.url"), lobbyId))
                .method(Net.HttpMethods.GET)
                .build())
                .sendRequest();
    }

    /**
     * Send HTTP request to exit the joined lobby while the lobby
     * is not yet full. Hide the waiting window.
     */
    private void exitLobby() {
        if (lobbyId == null) return;

        new HTTPRequestHandler(new Callback() {
            @Override
            public boolean onResult(Net.HttpResponse response) {
                if (response.getStatus().getStatusCode() == 200) {
                    removeWaitingWindowListeners();
                    setMainListeners();

                    lobbyId = null;
                    screen.hideWaitingWindow();
                    return true;
                }

                return false;
            }

            @Override
            public void onFailed(Throwable t) {
                System.err.println("Exit lobby request failed:\n" + t);
            }
        }, new HttpRequestBuilder()
                .newRequest()
                .url(String.format("%s/lobby/%s/leave", ConfigReader.getProperty("backend.url"), lobbyId))
                .method(Net.HttpMethods.POST)
                .header("Content-Type", "application/json")
                .content(String.format("{\"username\": \"%s\"}", CurrentUser.getCurrentUser().getUser().username))
                .build())
                .sendRequest();
    }
}
