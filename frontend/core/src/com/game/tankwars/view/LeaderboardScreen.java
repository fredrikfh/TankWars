package com.game.tankwars.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.game.tankwars.ResourceManager;
import com.game.tankwars.TankWarsGame;
import com.game.tankwars.controller.LeaderboardController;
import com.game.tankwars.model.User;

public class LeaderboardScreen implements Screen {

    private final TankWarsGame tankWarsGame;
    private Stage stage;

    private Skin skin;
    private Table boardTable;
    LeaderboardController leaderboardController;



    public LeaderboardScreen (final TankWarsGame tankWarsGame) {
        this.tankWarsGame = tankWarsGame;
    }

    @Override
    public void show() {
        stage = new Stage(new ExtendViewport(tankWarsGame.getViewportWidth(),
                tankWarsGame.getViewportHeight()), new SpriteBatch());
        Gdx.input.setInputProcessor(stage);

        skin = ResourceManager.getInstance().loadAndGetMenuAssets();

        Image background = new Image(skin.getDrawable("camo-background-portrait-blurred"));
        Image logo = new Image(skin.getDrawable("logo"));
        Drawable headerBox = skin.getDrawable("dark-menu-header");
        Drawable boardBackground = skin.getDrawable("transparent-white-box");

        Label leaderboardLabel = new Label("Leaderboard", skin.get("header", Label.LabelStyle.class));
        Label userLabel = new Label("User", skin.get("header", Label.LabelStyle.class));
        Label pointsLabel = new Label("Wins", skin.get("header", Label.LabelStyle.class));

        Button backButton = new Button(skin.get("default", Button.ButtonStyle.class));

        //--- Layout
        float logoWidth = stage.getWidth() / 4f;
        float headerPadding = backButton.getWidth() + 16;

        Table headerTable = new Table();
        headerTable.background(headerBox);
        headerTable.add(backButton).padLeft(headerPadding - backButton.getWidth());
        headerTable.add(logo).width(logoWidth).
                height(logo.getHeight() / logo.getWidth() * logoWidth).expandX().padRight(headerPadding);
        headerTable.add(leaderboardLabel).expandX().padRight(headerPadding);

        boardTable = new Table();
        boardTable.background(boardBackground);
        boardTable.align(Align.top);
        boardTable.row().expandX().padTop(4);
        boardTable.add(userLabel);
        boardTable.add(pointsLabel);
        ScrollPane boardScrollPane = new ScrollPane(boardTable);

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.add(headerTable).fillX().height(2 * stage.getHeight() / 7f).top();
        rootTable.row().expand();
        rootTable.add(boardScrollPane).width(2 * stage.getWidth() / 3f).fillY().padTop(16);

        Group group = new Group();
        group.setSize(stage.getWidth(), stage.getHeight());
        background.setSize(stage.getWidth(), background.getHeight() / background.getWidth() * stage.getWidth());
        background.setPosition(0, stage.getHeight() / 2f - background.getHeight() / 2f);
        group.addActor(background);
        group.addActor(rootTable);

        stage.addActor(group);

        leaderboardController = new LeaderboardController(tankWarsGame, backButton, this);
    }

    /**
     * Adds users to leaderboard with username and number of wins.
     * Displays "No connection" or "No users" in case there is no connection
     * to the server or the array is empty.
     *
     * @param users Array of users to be included in the leaderboard
     */
    public void setLeaderBoard(Array<User> users) {
        if (users == null) {
            boardTable.row().expand().colspan(2);
            Label noConnection = new Label("No connection", skin.get("default", Label.LabelStyle.class));
            boardTable.add(noConnection);
        } else if (users.size == 0) {
            boardTable.row().expand().colspan(2);
            Label noUsers = new Label("No users", skin.get("default", Label.LabelStyle.class));
            boardTable.add(noUsers);
        } else {
            for (User user : users) {
                boardTable.row().expandX();
                Label username = new Label(user.username,  skin.get("default", Label.LabelStyle.class));
                boardTable.add(username);
                Label points = new Label(String.valueOf(user.wins),  skin.get("default", Label.LabelStyle.class));
                boardTable.add(points);
            }
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
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
        dispose();
    }

}
