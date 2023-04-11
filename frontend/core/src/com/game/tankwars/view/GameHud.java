package com.game.tankwars.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.tankwars.ResourceManager;
import com.ray3k.stripe.FreeTypeSkin;

public class GameHud {

    private Stage stage;

    private Viewport viewport;

    private Table table;

    private Skin skin;

    private ProgressBar healthProgressBarPlayer;
    private ProgressBar healthProgressBarOpponent;


    private TextButton fireButton;

    private HorizontalGroup powerContainer;
    private Label powerLabel;
    private Slider powerSlider;

    private Button moveLeft;
    private Button moveRight;
    private HorizontalGroup moveContainer;

    public GameHud(Viewport viewport, SpriteBatch batch) {
        this.viewport = viewport;
        stage = new Stage(viewport, batch);

        table = new Table();
        table.setFillParent(true);
        table.setDebug(false);

        stage.addActor(table);

        skin = ResourceManager.getInstance().loadAndGetGameplayHudAssets();

        healthProgressBarPlayer = new ProgressBar(0, 100, 1, false, skin);
        healthProgressBarPlayer.setValue(100);
        healthProgressBarOpponent = new ProgressBar(0, 100, 1, false, skin);
        healthProgressBarOpponent.setValue(100);

        Container HpOpponentWrapper = new Container(healthProgressBarOpponent);
        HpOpponentWrapper.setTransform(true);
        HpOpponentWrapper.setOrigin(HpOpponentWrapper.getPrefWidth() / 2, HpOpponentWrapper.getPrefHeight() / 2);
        HpOpponentWrapper.setRotation(180);

        table.add(healthProgressBarPlayer).expand().top().left().padTop(10).padLeft(10);
        table.add(HpOpponentWrapper).colspan(2).top().right().padTop(10).padRight(10);

        table.row();

        fireButton = new TextButton("Fire!", skin);
        table.add(fireButton).expand().bottom().left().padLeft(10).padBottom(10);

        powerLabel = new Label("Power", skin.get("large-white", Label.LabelStyle.class));
        powerSlider = new Slider(0, 100, 1, false, skin);

        powerContainer = new HorizontalGroup().space(10);
        powerContainer.addActor(powerLabel);
        powerContainer.addActor(powerSlider);

        table.add(powerContainer).expand().bottom().padBottom(10);

        moveLeft = new Button(skin.get("move-left", Button.ButtonStyle.class));
        moveRight = new Button(skin.get("move-right", Button.ButtonStyle.class));

        moveContainer = new HorizontalGroup().space(10);
        moveContainer.addActor(moveLeft);
        moveContainer.addActor(moveRight);

        table.add(moveContainer).expand().bottom().right().padBottom(10).padRight(10);

    }

    public Stage getStage() {
        return stage;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public Skin getSkin() {
        return skin;
    }

    public TextButton getFireButton() {
        return fireButton;
    }

    public Slider getPowerSlider() {
        return powerSlider;
    }

    public Button getMoveLeft() {
        return moveLeft;
    }

    public Button getMoveRight() {
        return moveRight;
    }

    /**
     * Set visible health of player
     * @param health new health of player
     */
    public void setPlayerHealth(int health) {
        healthProgressBarPlayer.setValue(health);
    }

    /**
     * Set visible health of opponent
     * @param health new health of opponent
     */
    public void setOpponentHealth(int health) {
        healthProgressBarOpponent.setValue(health);
    }
}
