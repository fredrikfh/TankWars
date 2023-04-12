package com.game.tankwars.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.game.tankwars.ResourceManager;
import com.game.tankwars.TankWarsGame;
import com.game.tankwars.controller.FindGameController;

/**
 * Screen where the user can choose between creating a new game lobby
 * and join an existing lobby by entering a game pin.
 */
public class FindGameScreen implements Screen {

    private final TankWarsGame tankWarsGame;
    private Stage stage;
    private Group layoutGroup;
    private Table windowTable;

    public FindGameScreen(final TankWarsGame tankWarsGame) {
        this.tankWarsGame = tankWarsGame;
    }

    @Override
    public void show() {
        stage = new Stage(new ExtendViewport(tankWarsGame.getViewportWidth(),
                tankWarsGame.getViewportHeight()), new SpriteBatch());
        Gdx.input.setInputProcessor(stage);

        Skin skin = ResourceManager.getInstance().loadAndGetMenuAssets();

        Image backgroundPortrait = new Image(skin.getDrawable("camo-background-portrait"));
        Drawable backgroundBlurred = skin.getDrawable("camo-background-portrait-blurred");
        Image logo = new Image(skin.getDrawable("logo"));
        Drawable headerBox = skin.getDrawable("dark-menu-header");

        Label findGameLabel = new Label("Find Game", skin.get("header", Label.LabelStyle.class));
        Label gamePinLabel = new Label("Game pin", skin.get("default", Label.LabelStyle.class));

        TextField gamePinField = new TextField("",
                skin.get("default", TextField.TextFieldStyle.class));
        gamePinField.setAlignment(Align.center);

        TextButton joinLobbyButton = new TextButton("Join lobby",
                skin.get("default", TextButton.TextButtonStyle.class));
        joinLobbyButton.setDisabled(true);
        Button backButton = new Button(skin.get("default", Button.ButtonStyle.class));

        //--- Layout
        float lw = 2 * stage.getWidth() / 6f;
        float rw = stage.getWidth() - lw;

        Table rootTable = new Table();
        rootTable.setBounds(0, 0, stage.getWidth(), stage.getHeight());

        Group leftGroup = new Group();
        leftGroup.setSize(lw, stage.getHeight());
        backgroundPortrait.setSize(lw, backgroundPortrait.getHeight() / backgroundPortrait.getWidth() * lw);
        backgroundPortrait.setPosition(0, stage.getHeight() / 2f - backgroundPortrait.getHeight() / 2f);
        leftGroup.addActor(backgroundPortrait);
        backButton.setPosition(14, stage.getHeight() - backButton.getHeight() - 14);
        leftGroup.addActor(backButton);

        Table rightTable = new Table();
        rightTable.background(backgroundBlurred);

        Table headerTable = new Table();
        headerTable.background(headerBox);
        headerTable.add(logo).expandX().width(3 * rw / 7f).
                height(logo.getHeight() / logo.getWidth() * 3 * rw / 7f);
        headerTable.add(findGameLabel).expandX();

        rightTable.add(headerTable).fillX().height(stage.getHeight() / 4f);
        rightTable.row().expand(1, 1);
        rightTable.add(gamePinLabel).width(2 * rw / 3f).bottom().padLeft(10);
        rightTable.row().expand(1, 0);
        rightTable.add(gamePinField).width(2 * rw / 3f).height(42).top();
        rightTable.row().expand(1, 4);
        rightTable.add(joinLobbyButton).width(2 * rw / 3f).height(28);

        rootTable.add(leftGroup).width(lw).height(stage.getHeight());
        rootTable.add(rightTable).expandX().height(stage.getHeight());

        //--- Awaiting opponent window
        windowTable = new Table();
        float ww = 3 * stage.getWidth() / 5f;
        float wh = 3 * stage.getHeight() / 5f;

        windowTable.setBounds(stage.getWidth() / 2f - ww / 2f, stage.getHeight() / 2f - wh / 2f, ww, wh);

        Drawable windowBackground = skin.getDrawable("semi-transparent-white-box");
        Label waitingLabel = new Label("Awaiting opponent...", skin.get("default", Label.LabelStyle.class));
        Label gamePinWaitingLabel = new Label("Game pin: ---", skin.get("default", Label.LabelStyle.class));
        TextButton cancelButton = new TextButton("Cancel", skin.get("default", TextButton.TextButtonStyle.class));

        windowTable.background(windowBackground);
        windowTable.row().expand();
        windowTable.add(waitingLabel);
        windowTable.row().expand();
        windowTable.add(gamePinWaitingLabel);
        windowTable.row().expand();
        windowTable.add(cancelButton).width(2 * ww / 3f).height(30);


        layoutGroup = new Group();
        layoutGroup.addActor(rootTable);

        stage.addActor(layoutGroup);

        new FindGameController(tankWarsGame, this, gamePinField,
                joinLobbyButton, backButton, cancelButton, gamePinWaitingLabel, stage);
    }

    public void showWaitingWindow() { layoutGroup.addActor(windowTable); }

    public void hideWaitingWindow() {
        layoutGroup.removeActor(windowTable);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1f);

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
