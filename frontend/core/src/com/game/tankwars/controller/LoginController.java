package com.game.tankwars.controller;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.tankwars.ResourceManager;
import com.game.tankwars.TankWarsGame;
import com.game.tankwars.view.GameScreen;

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

    public LoginController(final TankWarsGame tankWarsGame, final TextButton loginButton, final TextField usernameField, final Stage stage) {
        this.tankWarsGame = tankWarsGame;
        this.loginButton = loginButton;
        this.usernameField = usernameField;
        this.stage = stage;

        setEventListeners();
    }

    public void setEventListeners() {
        loginButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                handleInput(usernameField.getText());
                return true;
            }
        });

        usernameField.addListener(new InputListener() {
            /**
             * Unfocus text field and remove keyboard when enter is pressed,
             * and move camera back to original position.
             */
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

        usernameField.addListener(new ClickListener() {
            /**
             * Move camera down when text field is clicked
             * to make the field appear above the keyboard.
             */
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                stage.getViewport().setScreenY((int) (2 * stage.getHeight() / 3));
                stage.getViewport().apply();
            }
        });
    }

    public void handleInput(String username) {
        System.out.println(username);

        // TODO: Move clear line to MainMenuController when the main menu is operational
        ResourceManager.getInstance().clear();
        tankWarsGame.setScreen(new GameScreen(tankWarsGame));
    }

}
