package com.game.tankwars.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.tankwars.ResourceManager;

public class GameHud {

    private Stage stage;

    private Viewport viewport;

    private Table table;

    private Skin skin;

    private ProgressBar healthProgressBarPlayer;
    private ProgressBar healthProgressBarOpponent;

    private Label turnLabel;
    private Label turnInformationLabel;
    private Container turnContainer;
    private Container turnInformationContainer;
    private Table turnTable;

    private TextButton fireButton;

    private HorizontalGroup powerContainer;
    private Label powerLabel;
    private Slider powerSlider;

    private Button moveLeft;
    private Button moveRight;

    private Button aimUp;
    private Button aimDown;
    private HorizontalGroup movementContainer;
    private HorizontalGroup aimContainer;

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

        turnLabel = new Label("It's your turn!", skin.get("large-white", Label.LabelStyle.class));
        turnInformationLabel = new Label("Touch screen to start your turn.", skin.get("regular-white", Label.LabelStyle.class));

        table.add(healthProgressBarPlayer).expand().top().left().padTop(10).padLeft(10);
        table.add(HpOpponentWrapper).colspan(2).top().right().padTop(10).padRight(10);

        table.row();

        turnContainer = new Container(turnLabel);
        turnInformationContainer = new Container(turnInformationLabel);

        Pixmap bgPixmap = new Pixmap(1,1, Pixmap.Format.RGB565);
        bgPixmap.setColor(Color.GRAY);
        bgPixmap.fill();
        TextureRegionDrawable textureRegionDrawableBg = new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
        turnContainer.setBackground(textureRegionDrawableBg);

        turnTable = new Table();
        turnTable.add(turnContainer).prefWidth(1000).height(100);
        turnTable.row();
        turnTable.add(turnInformationContainer).padTop(20);

        table.add(turnTable).prefWidth(1000).height(300).colspan(3);

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

        movementContainer = new HorizontalGroup().space(10);



        aimUp = new Button(skin.get("move-right", Button.ButtonStyle.class));
        aimDown = new Button(skin.get("move-left", Button.ButtonStyle.class));
        aimUp.setTransform(true);
        aimDown.setTransform(true);
        aimUp.setOrigin(aimUp.getWidth() / 2, aimUp.getHeight() / 2);
        aimUp.setRotation(90);
        aimDown.setOrigin(aimDown.getWidth() / 2, aimDown.getHeight() / 2);
        aimDown.setRotation(90);

        movementContainer.addActor(moveLeft);
        movementContainer.addActor(moveRight);
        movementContainer.addActor(aimUp);
        movementContainer.addActor(aimDown);

        table.add(movementContainer).expand().bottom().right().padBottom(10).padRight(10);

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

    public Button getAimUp() { return aimUp; }

    public Button getAimDown() { return aimDown; }

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

    public boolean isTurnContainerVisible() {
        return turnContainer.isVisible();
    }

    public void removeTurnContainer() {
        if(turnContainer.isVisible()) {
            turnContainer.setVisible(false);
        }
    }

    public void showTurnContainer() {
        if (!turnContainer.isVisible()) {
            turnContainer.setVisible(true);
        }
    }

    public void removeTurnInformationContainer() {
        if(turnInformationContainer.isVisible()) {
            turnInformationContainer.setVisible(false);
        }
    }

    public void showTurnInformationContainer() {
        if (!turnInformationContainer.isVisible()) {
            turnInformationContainer.setVisible(true);
        }
    }

    public void showYourTurnLabel() {
        turnLabel.setText("It's your turn!");
        turnInformationLabel.setText("Touch screen to start your turn.");
        showTurnContainer();
        showTurnInformationContainer();
    }
    public void showOpponentTurnLabel() {
        turnLabel.setText("Waiting for opponent...");
        showTurnContainer();
        removeTurnInformationContainer();
    }

    public void showWinBanner() {
        turnLabel.setText("You won!");
        removeTurnInformationContainer();
    }

    public void showLoserBanner() {
        turnLabel.setText("Game Over!");
        removeTurnInformationContainer();
    }
}
