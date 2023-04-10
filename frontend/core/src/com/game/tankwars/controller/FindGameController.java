package com.game.tankwars.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
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
    private final TextButton joinLobbyButton, createLobbyButton, cancelButton;
    private final Button backButton;
    private final Label gamePinWaitingLabel;

    private EventListener backButtonInputListener, gamePinFieldInputListener,
            gamePinFieldClickListener, joinLobbyButtonInputListener,
            createLobbyButtonInputListener, cancelButtonInputListener;

    private String lobbyId = null;
    private final Runnable gameScreenTransition;


    /**
     * TODO: Ensure that user is logged in -> e.g. auth method in Auth/Utils class
     * Sets the event listeners of the buttons and the text field of the FindGameScreen,
     * and allows for transitioning to MainMenuScreen and to GameScreen. Handles communication
     * with server to create, join and leave a lobbies.
     */
    public FindGameController(final TankWarsGame tankWarsGame, FindGameScreen screen,
                              TextField gamePinField, TextButton joinLobbyButton,
                              TextButton createLobbyButton, Button backButton,
                              TextButton cancelButton, Label gamePinWaitingLabel,
                              final Stage stage) {
        this.tankWarsGame = tankWarsGame;
        this.screen = screen;

        this.gamePinField = gamePinField;
        this.joinLobbyButton = joinLobbyButton;
        this.createLobbyButton = createLobbyButton;
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

        defineEventListeners();
        setMainListeners();
    }

    private void defineEventListeners() {

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
         * Create new lobby and display waiting window.
         * Poll server for lobby status to know when to transition to game screen.
         */
        createLobbyButtonInputListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                createLobby();
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
        createLobbyButton.addListener(createLobbyButtonInputListener);
    }

    public void removeMainListeners() {
        backButton.removeListener(backButtonInputListener);
        gamePinField.removeListener(gamePinFieldInputListener);
        gamePinField.removeListener(gamePinFieldClickListener);
        joinLobbyButton.removeListener(joinLobbyButtonInputListener);
        createLobbyButton.removeListener(createLobbyButtonInputListener);
    }

    public void setWaitingWindowListeners() {
        cancelButton.addListener(cancelButtonInputListener);
    }

    public void removeWaitingWindowListeners() {
        cancelButton.removeListener(cancelButtonInputListener);
    }


    /**
     * Send HTTP request to create a new lobby.
     * On success, a waiting window is shown, and polling for
     * the lobby status is initiated.
     */
    private void createLobby() {
        new HTTPRequestHandler(new Callback() {
            @Override
            public void onResult(String result) {
                try {
                    LobbyId lobbyIdClass = new Json().fromJson(LobbyId.class, result);
                    lobbyId = lobbyIdClass.getLobbyId();

                    removeMainListeners();
                    setWaitingWindowListeners();

                    gamePinWaitingLabel.setText("Game pin: " + lobbyId);
                    screen.showWaitingWindow();

                    checkLobbyStatus();
                } catch (SerializationException e) {
                    System.err.println("Invalid HTTP response on create lobby");
                }
            }

            @Override
            public void onFailed(Throwable t) {
                System.err.println("Create lobby request failed:\n" + t);
            }
        }, new HttpRequestBuilder()
                .newRequest()
                .url(String.format("%s/lobby/create", ConfigReader.getProperty("backend.url")))
                .method(Net.HttpMethods.POST)
                .header("Content-Type", "application/json")
                .content(String.format("{\"userId\": \"%s\"}", CurrentUser.getCurrentUser().getUser().id))
                .build())
                .sendRequest();
    }

    /**
     * Send HTTP request while waiting for lobby to fill,
     * polling for the lobby's fill status with a set backoff period between requests.
     * Transition to game screen when lobby is full.
     */
    private void checkLobbyStatus() {
        new HTTPRequestHandler(new Callback() {
            @Override
            public void onResult(String result) {
                try {
                    LobbyStatus lobbyStatus = new Json().fromJson(LobbyStatus.class, result);

                    if (lobbyStatus.isFull()) {
                        Gdx.app.postRunnable(gameScreenTransition);
                    } else if (lobbyId != null) {
                        System.out.println("Awaiting opponent...");

                        try {
                            Thread.sleep(1500);
                            if (lobbyId != null) checkLobbyStatus();
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                            exitLobby();
                        }
                    }
                } catch (SerializationException e) {
                    System.err.println("Invalid HTTP response on check lobby status");
                    exitLobby();
                }
            }

            @Override
            public void onFailed(Throwable t) {
                System.err.println("Check lobby status request failed:\n" + t);
                exitLobby();
            }
        }, new HttpRequestBuilder()
                .newRequest()
                .url(String.format("%s/lobby/%s/status",
                        ConfigReader.getProperty("backend.url"), lobbyId))
                .method(Net.HttpMethods.GET)
                .build())
                .sendRequest();
    }

    /**
     * Send HTTP request to exit the joined lobby while the lobby
     * is not yet full. Hide the waiting window.
     */
    private void exitLobby() {
        new HTTPRequestHandler(new Callback() {
            @Override
            public void onResult(String result) {
                removeWaitingWindowListeners();
                setMainListeners();

                lobbyId = null;
                screen.hideWaitingWindow();
            }

            @Override
            public void onFailed(Throwable t) {
                System.err.println("Exit lobby request failed:\n" + t);
                removeWaitingWindowListeners();
                setMainListeners();

                lobbyId = null;
                screen.hideWaitingWindow();
            }
        }, new HttpRequestBuilder()
                .newRequest()
                .url(String.format("%s/lobby/%s/leave",
                        ConfigReader.getProperty("backend.url"), lobbyId))
                .method(Net.HttpMethods.POST)
                .header("Content-Type", "application/json")
                .content(String.format("{\"userId\": \"%s\"}", CurrentUser.getCurrentUser().getUser().id))
                .build())
                .sendRequest();
    }

    /**
     * Send HTTP request to join a lobby using the game pin in the text field.
     * On success, transition to game screen.
     */
    private void joinLobby() {
        new HTTPRequestHandler(new Callback() {
            @Override
            public void onResult(String result) {
                try {
                    new Json().fromJson(LobbyId.class, result);

                    Gdx.app.postRunnable(gameScreenTransition);
                } catch (SerializationException e) {
                    System.out.println(result);
                }
            }

            @Override
            public void onFailed(Throwable t) {
                System.err.println("Join lobby request failed:\n" + t);
            }
        }, new HttpRequestBuilder()
                .newRequest()
                .url(String.format("%s/lobby/%s/join",
                        ConfigReader.getProperty("backend.url"), gamePinField.getText()))
                .method(Net.HttpMethods.POST)
                .header("Content-Type", "application/json")
                .content(String.format("{\"userId\": \"%s\"}", CurrentUser.getCurrentUser().getUser().id))
                .build())
                .sendRequest();
    }
}