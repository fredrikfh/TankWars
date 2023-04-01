package com.game.tankwars.view;

import static com.game.tankwars.model.CurrentUser.getCurrentUser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.game.tankwars.TankWarsGame;
import com.game.tankwars.controller.GameController;
import com.game.tankwars.model.Box2dWorld;
import com.game.tankwars.model.Bullet;
import com.game.tankwars.model.Tank;
import com.game.tankwars.model.Terrain;

public class GameScreen implements Screen {
    final TankWarsGame tankWarsGame;
    int VIEWPORT_WIDTH;
    int VIEWPORT_HEIGHT;
    int horizontalScaling;
    int verticalScaling;
    SpriteBatch batch;
    ShapeRenderer shapeRender;
    Tank myTank;
    Tank opponentTank;
    Box2dWorld model;
    World world;
    Terrain terrain;
    OrthographicCamera cam;
    Box2DDebugRenderer debugRenderer;
    Bullet bullet;
    GameController controller;
    Mesh groundMesh;

    public GameScreen(final TankWarsGame tankWarsGame){
        this.tankWarsGame = tankWarsGame;

        VIEWPORT_HEIGHT = tankWarsGame.getViewportHeight();
        VIEWPORT_WIDTH = tankWarsGame.getViewportWidth();

        batch = new SpriteBatch();
        shapeRender = new ShapeRenderer();

        model = new Box2dWorld();
        world = Box2dWorld.getWorld();
        cam = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        cam.position.set(VIEWPORT_WIDTH/2, VIEWPORT_HEIGHT/2, 0);
        cam.update();
        debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);

        terrain = new Terrain();

        int myPos = 50;
        int opponentPos = terrain.getVertices().length - 150;

        myTank = new Tank(myPos,
                new Texture("tank-khaki.png"),
                new Texture("cannon.png"),
                terrain,
                tankWarsGame, true);
        opponentTank = new Tank(opponentPos,
                new Texture("tank-khaki.png"),
                new Texture("cannon.png"),
                terrain,
                tankWarsGame, false);

        horizontalScaling = Gdx.graphics.getWidth() / VIEWPORT_WIDTH;
        verticalScaling = Gdx.graphics.getHeight() / VIEWPORT_HEIGHT;

        controller = new GameController(myTank, tankWarsGame);
    }
    @Override
    public void render(float delta) {
        model.logicStep(Gdx.graphics.getDeltaTime());
        Gdx.gl.glClearColor(0, 0, 100, 100);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        debugRenderer.render(world, cam.combined);
        shapeRender.setProjectionMatrix(cam.combined);

        controller.checkKeyInput(myTank);

        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);

        //Rendering Bullet bodies
        for (Body b : bodies) {
            Sprite s = (Sprite) b.getUserData();

            if (s != null) {
                s.setPosition(b.getPosition().x * (float) horizontalScaling - s.getWidth() / 2, (b.getPosition().y + 0.25f) * (float) verticalScaling);
                if (s.equals(myTank.getChassisSprite())) {
                    s.setRotation(myTank.getAngle());
                }
                if (s.equals(opponentTank.getChassisSprite())) {
                    s.setRotation(opponentTank.getAngle());
                }
                if (s.equals(myTank.getCannonSprite())) {
                    s.setOrigin(s.getWidth() / 2, 0);
                    s.setRotation(myTank.getCannonAngle());
                }
                if (s.equals(opponentTank.getCannonSprite())) {
                    s.setOrigin(s.getWidth() / s.getWidth(), 0);
                    s.setRotation(opponentTank.getCannonAngle());
                }
            }
        }

        shapeRender.begin(ShapeRenderer.ShapeType.Filled);
        terrain.draw(shapeRender);
        shapeRender.end();

        batch.begin();
        myTank.getChassisSprite().draw(batch);
        myTank.getCannonSprite().draw(batch);
        opponentTank.getChassisSprite().draw(batch);
        opponentTank.getCannonSprite().draw(batch);
        batch.end();
    }

    @Override
    public void show() {

    }
    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        myTank.getChassisTexture().dispose();
        myTank.getCannonTexture().dispose();
        batch.dispose();
        shapeRender.dispose();
    }
}
