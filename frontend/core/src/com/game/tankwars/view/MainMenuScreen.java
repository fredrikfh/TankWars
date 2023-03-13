package com.game.tankwars.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.tankwars.TankWarsGame;
import com.game.tankwars.controller.MainMenuController;
import com.game.tankwars.model.MenuButton;

public class MainMenuScreen extends InputAdapter implements Screen {

    private final TankWarsGame tankWarsGame;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Viewport viewport;
    private final MainMenuController controller;

    private final Sprite logo;
    private final Texture welcomeBox;
    private final Texture background;
    private final Texture menuButtonTexture;
    private final GlyphLayout welcomeLayout, usernameLayout;

    private final Array<MenuButton> menuButtons;

    private final Vector3 touchPos;

    private float heightPercentile;

    /**
     * Main menu
     * Only supports portrait mode
     */
    public MainMenuScreen(final TankWarsGame tankWarsGame, final SpriteBatch batch, final BitmapFont font, final OrthographicCamera camera) {
        this.tankWarsGame = tankWarsGame;
        this.batch = batch;
        this.font = font;
        this.font.getData().setScale(0.55f);

        // TODO: Add landscape support, both on startup and on rotation
        viewport = new FitViewport(camera.viewportWidth, camera.viewportHeight, camera);
        heightPercentile = viewport.getWorldHeight() / 100;
        controller = new MainMenuController(this.tankWarsGame, this.font);

        logo = new Sprite(new Texture("tankwars-logo.png"));
        logo.setScale(1.3f);
        logo.setPosition(-logo.getWidth() / 2, 32 * heightPercentile);

        welcomeBox = new Texture("main-menu-welcome-box.png");
        welcomeLayout = new GlyphLayout(font, "Welcome to duty");
        usernameLayout = new GlyphLayout(font, controller.getUsername());

        background = new Texture("menu-background.png");

        menuButtonTexture = new Texture("menu-button-2.png");
        menuButtons = controller.setMenuButtons(menuButtonTexture, font);
        for (int i = 0; i < menuButtons.size; i++) {
            menuButtons.get(i).setPosition(0,  -(12 * i - 4) * heightPercentile);
        }

        touchPos = new Vector3();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        batch.draw(background, -background.getWidth() / 2f, -background.getHeight() / 2f);

        logo.draw(batch);

        batch.draw(welcomeBox, -welcomeBox.getWidth() / 2f, 12 * heightPercentile,
                viewport.getWorldWidth(), welcomeBox.getHeight());
        font.draw(batch, welcomeLayout, -welcomeLayout.width / 2,
                12 * heightPercentile + welcomeBox.getHeight() * 4f/5f);
        font.draw(batch, usernameLayout, -usernameLayout.width / 2,
                12 * heightPercentile + welcomeBox.getHeight() * 4f/5f - 2 * welcomeLayout.height);

        for (MenuButton menuButton : menuButtons) {
            batch.draw(menuButton.getTexture(), menuButton.getX(), menuButton.getY());
            font.draw(batch, menuButton.getContent(), menuButton.getContentX(), menuButton.getContentY());
        }
        batch.end();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPos.set(screenX, screenY, 0);
        viewport.unproject(touchPos);

        controller.handleInput(touchPos);
        return true;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        heightPercentile = viewport.getWorldHeight() / 100;
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
        logo.getTexture().dispose();
        welcomeBox.dispose();
        background.dispose();
        menuButtonTexture.dispose();
    }
}
