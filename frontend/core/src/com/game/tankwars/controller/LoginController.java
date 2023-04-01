package com.game.tankwars.controller;

import static com.game.tankwars.model.CurrentUser.getCurrentUser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.game.tankwars.Callback;
import com.game.tankwars.ReceiverHandler;
import com.game.tankwars.TankWarsGame;
import com.game.tankwars.model.CurrentUser;
import com.game.tankwars.model.User;
import com.game.tankwars.view.MainMenuScreen;


/**
 * Todo: Login user on backend
 *
 * Listens to the login button on the LoginScreen and logs in
 * the user with the username provided in the username text field.
 * Transitions to MainMenuScreen on login.
 */
public class LoginController {

    private final TankWarsGame tankWarsGame;
    private final Stage stage;
    private final TextButton loginButton;
    private final TextField usernameField;
    private int retries = 0;
    private boolean failed = false;
    CurrentUser currentUser;

    public LoginController(final TankWarsGame tankWarsGame, TextButton loginButton, TextField usernameField, Stage stage) {
        this.tankWarsGame = tankWarsGame;
        this.loginButton = loginButton;
        this.usernameField = usernameField;
        this.stage = stage;
        currentUser = getCurrentUser();
        setEventListeners();
    }

    public void setEventListeners() {
        /*
         * Calls the handleInput method when the login button is pressed,
         * passing the username typed in the username field as an argument.
         */
        loginButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                handleInput(usernameField.getText());
                return true;
            }
        });

        /*
         * Unfocus text field and remove keyboard when enter is pressed,
         * and move camera back to original position.
         */
        usernameField.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                super.keyDown(event, keycode);
                if (keycode == 66) {
                    usernameField.getOnscreenKeyboard().show(false);
                    stage.unfocus(usernameField);
                    stage.getViewport().setScreenY(0);
                    stage.getViewport().apply();
                }
                return true;
            }
        });

        /*
         * Move camera down when text field is clicked
         * to make the field appear above the keyboard.
         */
        usernameField.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                stage.getViewport().setScreenY((int) (2 * stage.getHeight() / 3));
                stage.getViewport().apply();
            }
        });
    }

    public void handleInput(String username) {
        retries = 0;
        failed = false;
        fetchUser(username);
        while (getCurrentUser().getUser() == null && !failed){
        }
        if (!failed)
            tankWarsGame.setScreen(new MainMenuScreen(tankWarsGame));
    }

    public void fetchUser(final String username) {
        Callback callback = new Callback() {
            @Override
            public void onResult(String result) {
                Json json = new Json();
                User user = json.fromJson(User.class, result);
                currentUser.setUser(user);
            }

            @Override
            public void onFailed(Throwable t) {
                retries += 1;
                if (retries < ReceiverHandler.MAX_RETRIES) {
                    fetchUser(username);
                } else {
                    failed = true;
                }
            }
        };

        String url = String.format("http://localhost:80/user/create/%s", username);
        Net.HttpRequest httpRequest = new HttpRequestBuilder()
                .newRequest()
                .method(Net.HttpMethods.POST)
                .url(url)
                .build();
        Gdx.net.sendHttpRequest(httpRequest, new ReceiverHandler(callback));
    }
}
