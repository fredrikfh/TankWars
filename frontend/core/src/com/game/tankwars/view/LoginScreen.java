package com.game.tankwars.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.game.tankwars.ResourceManager;
import com.game.tankwars.TankWarsGame;
import com.game.tankwars.controller.LoginController;


/**
 * Basic login screen with only a username text field.
 * The user is logged in with the provided name when the login button
 * is touched.
 */
public class LoginScreen implements Screen {
    private final TankWarsGame tankWarsGame;
    private Stage stage;
    private TextField usernameField;

    public LoginScreen(final TankWarsGame tankWarsGame) {
        this.tankWarsGame = tankWarsGame;
    }

    @Override
    public void show() {
        stage = new Stage(new ExtendViewport(320, 240), new SpriteBatch());
        Gdx.input.setInputProcessor(stage);

        Skin skin = ResourceManager.getInstance().loadAndGetMenuAssets();

        // TODO: Transition to ErrorScreen if skin == null

        Image backgroundPortrait = new Image(skin.getDrawable("camo-background-portrait"));
        Drawable backgroundBlurred = skin.getDrawable("camo-background-portrait-blurred");
        Image logo = new Image(skin.getDrawable("logo"));
        Drawable headerBox = skin.getDrawable("dark-menu-header");

        Label loginLabel = new Label("Log in", skin.get("header", Label.LabelStyle.class));
        Label usernameLabel = new Label("Username", skin.get("default", Label.LabelStyle.class));

        usernameField = new TextField("",
                        skin.get("default", TextField.TextFieldStyle.class));
        usernameField.setAlignment(Align.center);
        TextButton loginButton = new TextButton("Log in",
                skin.get("default", TextButton.TextButtonStyle.class));

        //--- Layout
        float lw = 2 * stage.getWidth() / 5f;
        float rw = stage.getWidth() - lw;

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.center();
        rootTable.add(backgroundPortrait).width(lw).
                left().minHeight(stage.getHeight()).
                height(backgroundPortrait.getHeight() / backgroundPortrait.getWidth() * lw);

        Table rightTable = new Table();
        rightTable.background(backgroundBlurred);

        Table headerTable = new Table();
        headerTable.background(headerBox);
        headerTable.add(logo).expandX().width(3 * rw / 7f).
                height(logo.getHeight() / logo.getWidth() * 3 * rw / 7f);
        headerTable.add(loginLabel).expandX();

        rightTable.add(headerTable).fillX().height(stage.getHeight() / 4f);
        rightTable.row().expand(1, 1);
        rightTable.add(usernameLabel).width(2 * rw / 3f).bottom().padLeft(10);
        rightTable.row().expand(1, 0);
        rightTable.add(usernameField).width(2 * rw / 3f).height(42).top();
        rightTable.row().expand(1, 4);
        rightTable.add(loginButton).width(2 * rw / 3f).height(28);

        rootTable.add(rightTable).expandX().height(stage.getHeight());
        stage.addActor(rootTable);

        new LoginController(tankWarsGame, loginButton, usernameField, stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }


    @Override
    public void dispose() {
        stage.dispose();
    }


}
