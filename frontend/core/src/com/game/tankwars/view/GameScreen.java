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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.game.tankwars.CollisionDetection;
import com.game.tankwars.TankWarsGame;
import com.game.tankwars.controller.GameController;
import com.game.tankwars.controller.TerrainController;
import com.game.tankwars.model.Box2dWorld;
import com.game.tankwars.model.Bullet;
import com.game.tankwars.model.FixtureData;
import com.game.tankwars.model.Tank;
import com.game.tankwars.model.Terrain;

import java.util.ArrayList;
import java.util.Arrays;

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
    GameHud hud;
    Box2dWorld model;
    World world;
    Terrain terrain;
    OrthographicCamera worldCam;
    OrthographicCamera hudCam;
    Box2DDebugRenderer debugRenderer;
    GameController controller;
    CollisionDetection collisionDetection;
    TerrainController terrainController;

    private Bullet bullet;
    private boolean bulletToDestroy = false;

    public GameScreen(final TankWarsGame tankWarsGame){
        this.tankWarsGame = tankWarsGame;

        VIEWPORT_HEIGHT = tankWarsGame.getViewportHeight();
        VIEWPORT_WIDTH = tankWarsGame.getViewportWidth();

        batch = new SpriteBatch();
        shapeRender = new ShapeRenderer();

        model = new Box2dWorld();
        world = Box2dWorld.getWorld();

        collisionDetection = new CollisionDetection();
        world.setContactListener(collisionDetection);

        worldCam = new OrthographicCamera(scale(TankWarsGame.GAMEPORT_WIDTH), scale(TankWarsGame.GAMEPORT_HEIGHT));
        worldCam.position.set(scale(TankWarsGame.GAMEPORT_WIDTH)/2, scale(TankWarsGame.GAMEPORT_HEIGHT)/2, 0);

        hudCam = new OrthographicCamera(TankWarsGame.GAMEPORT_WIDTH, TankWarsGame.GAMEPORT_HEIGHT);
        hudCam.position.set(TankWarsGame.GAMEPORT_WIDTH/2, TankWarsGame.GAMEPORT_HEIGHT/2, 0);

        worldCam.update();
        hudCam.update();

        debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);

        terrainController = new TerrainController(this);

        int myPos = 50;
        int opponentPos = terrain.getVertices().length - 220;

        myTank = new Tank(myPos,
                new Texture("camo-tank-1.png"),
                new Texture("camo-tank-barrel.png"),
                terrain,
                tankWarsGame, true,
                120,
                "userTank");
        opponentTank = new Tank(opponentPos,
                new Texture("camo-tank-1.png"),
                new Texture("camo-tank-barrel.png"),
                terrain,
                tankWarsGame,
                false,
                225,
                "opponentTank");

        horizontalScaling = Gdx.graphics.getWidth() / TankWarsGame.GAMEPORT_WIDTH;
        verticalScaling = Gdx.graphics.getHeight() / TankWarsGame.GAMEPORT_HEIGHT;

        hud = new GameHud(new FitViewport(TankWarsGame.GAMEPORT_WIDTH, TankWarsGame.GAMEPORT_HEIGHT, hudCam), batch);
        controller = new GameController(myTank, tankWarsGame, hud);

        Gdx.input.setInputProcessor(hud.getStage());
        controller.handleHudEvents();
    }
    @Override
    public void render(float delta) {
        model.logicStep(Gdx.graphics.getDeltaTime());
        model.destroyDeadBodies();
        Gdx.gl.glClearColor(0, 0, 100, 100);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        debugRenderer.render(world, worldCam.combined);
        shapeRender.setProjectionMatrix(worldCam.combined);

        controller.checkKeyInput(myTank);

        //controller.handleTouchInput(worldCam);

        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);


        //Rendering Bullet bodies
        for (Body b : bodies) {
            Sprite s = (Sprite) b.getUserData();

            if (s != null) {
                s.setPosition(b.getPosition().x * (float) TankWarsGame.SCALE - s.getWidth() / 2,
                        (b.getPosition().y * (float) TankWarsGame.SCALE) - s.getHeight()/2);
                if (s.equals(myTank.getChassisSprite())) {
                    s.setOrigin(s.getWidth() / 2, s.getHeight());
                    s.setRotation(myTank.getAngle());
                }
                if (s.equals(opponentTank.getChassisSprite())) {
                    s.setOrigin(s.getWidth() / 2, s.getHeight());
                    s.setRotation(opponentTank.getAngle());
                }
                if (s.equals(myTank.getCannonSprite())) {
                    s.setOrigin(s.getWidth() / 2, s.getHeight());
                    s.setRotation(myTank.getCannonAngle());
                }
                if (s.equals(opponentTank.getCannonSprite())) {
                    s.setOrigin(s.getWidth() / 2 , s.getHeight());
                    s.setRotation(opponentTank.getCannonAngle());
                }
            }
        }
        shapeRender.begin(ShapeRenderer.ShapeType.Filled);
        terrain.draw(shapeRender);
        shapeRender.end();
        updateBullet();
        if (checkBulletCollision()) {
            Body body = this.bullet.getBody();
            bullet.getBulletSprite().getTexture().dispose();
            this.bullet = null;
            model.addDeadBody(body);
            System.out.println("Destroy body!");
        }
        opponentTank.hasBeenHit();

        batch.begin();
        if (this.bullet != (null)) {
            this.bullet.getBulletSprite().draw(batch);
        }
        myTank.getChassisSprite().draw(batch);
        myTank.getCannonSprite().draw(batch);
        opponentTank.getChassisSprite().draw(batch);
        opponentTank.getCannonSprite().draw(batch);
        batch.end();

        batch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getHealthProgressBarOpponent().setValue(opponentTank.getHealth());
        hud.getHealthProgressBarPlayer().setValue(myTank.getHealth());
        hud.getStage().draw();
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
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
        hud.getStage().dispose();
        batch.dispose();
        shapeRender.dispose();
    }

    private float scale(float value) {
        return value / TankWarsGame.SCALE;
    }

    private void updateBullet() {
        Bullet bullet = controller.getBullet();
        if (bullet != (null)) {
            this.bullet = bullet;
        }
    }

    private boolean checkBulletCollision() {
        if (this.bullet == null) {
            return false;
        }
        try {
            FixtureData data = (FixtureData) bullet.getBody().getFixtureList().get(0).getUserData();
            return data.isHit();
        } catch (IndexOutOfBoundsException e){
            return false;
        }

    }
}
