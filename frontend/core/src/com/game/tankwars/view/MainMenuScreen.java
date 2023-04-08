package com.game.tankwars.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.game.tankwars.ResourceManager;
import com.game.tankwars.TankWarsGame;
import com.game.tankwars.controller.MainMenuController;

/**
 * Main menu screen with buttons leading to the different screens in the application.
 *
 * The following transitions from the buttons are supported by the MainMenuController:
 * Find game button transitions to GameScreen
 * Highscore button transitions to LeaderboardScreen
 * Settings button transitions to no screen as of now
 * Log out button transitions to log in screen
 */
public class MainMenuScreen extends InputAdapter implements Screen {

    private final TankWarsGame tankWarsGame;
    private Stage stage;

    public MainMenuScreen(final TankWarsGame tankWarsGame) {
        this.tankWarsGame = tankWarsGame;
    }

    @Override
    public void show() {
        stage = new Stage(new ExtendViewport(tankWarsGame.getViewportWidth(), tankWarsGame.getViewportHeight()), new SpriteBatch());
        Gdx.input.setInputProcessor(stage);

        Skin skin = ResourceManager.getInstance().loadAndGetMenuAssets();

        Image background = new Image(skin.getDrawable("camo-background-landscape"));

        Image logo = new Image(skin.getDrawable("logo"));
        Drawable headerBox = skin.getDrawable("dark-menu-header");
        Label loginLabel = new Label("Main Menu", skin.get("header", Label.LabelStyle.class));

        Drawable panelBackground = skin.getDrawable("transparent-white-box");
        TextButton findGameButton = new TextButton("Find game",
                skin.get("default", TextButton.TextButtonStyle.class));
        TextButton highScoreButton = new TextButton("Highscore",
                skin.get("default", TextButton.TextButtonStyle.class));
        TextButton settingsButton = new TextButton("Settings",
                skin.get("default", TextButton.TextButtonStyle.class));
        TextButton logoutButton = new TextButton("Log out",
                skin.get("default", TextButton.TextButtonStyle.class));

        //--- Layout
        Table rootTable = new Table();
        rootTable.setFillParent(true);

        float logoWidth = stage.getWidth() / 4f;

        Table headerTable = new Table();
        headerTable.background(headerBox);
        headerTable.add(logo).width(logoWidth).
                height(logo.getHeight() / logo.getWidth() * logoWidth).expandX();
        headerTable.add(loginLabel).expandX();

        float buttonWidth = 3 * stage.getWidth() / 7f;
        float buttonHeight = 32;

        Table panelTable = new Table();
        panelTable.background(panelBackground);
        panelTable.row().expandY();
        panelTable.add(findGameButton).width(buttonWidth).expandX().height(buttonHeight);
        panelTable.add(highScoreButton).width(buttonWidth).expandX().height(buttonHeight);
        panelTable.row().expandY();
        panelTable.add(settingsButton).width(buttonWidth).expandX().height(buttonHeight);
        panelTable.add(logoutButton).width(buttonWidth).expandX().height(buttonHeight);

        rootTable.add(headerTable).fillX().height(2 * stage.getHeight() / 7f).top();
        rootTable.row().expandY();
        rootTable.add(panelTable).fillX().height(3 * stage.getHeight() / 7f).bottom();

        Group group = new Group();
        group.setSize(stage.getWidth(), stage.getHeight());
        background.setSize(stage.getWidth(), background.getHeight() / background.getWidth() * stage.getWidth());
        background.setPosition(0, stage.getHeight() / 2f - background.getHeight() / 2f);
        group.addActor(background);
        group.addActor(rootTable);

        stage.addActor(group);
        new MainMenuController(this.tankWarsGame, findGameButton, highScoreButton, settingsButton, logoutButton);
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
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
