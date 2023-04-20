package com.game.tankwars.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    Tank tank1;
    Tank tank2;
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

        horizontalScaling = Gdx.graphics.getWidth() / TankWarsGame.GAMEPORT_WIDTH;
        verticalScaling = Gdx.graphics.getHeight() / TankWarsGame.GAMEPORT_HEIGHT;

        hud = new GameHud(new FitViewport(TankWarsGame.GAMEPORT_WIDTH, TankWarsGame.GAMEPORT_HEIGHT, hudCam), batch);
        controller = new GameController(tankWarsGame, hud, terrain);
        tank1 = controller.getTank1();
        tank2 = controller.getTank2();
    }
    @Override
    public void render(float delta) {
        model.logicStep(Gdx.graphics.getDeltaTime());
        Gdx.gl.glClearColor(83/255f, 109/255f, 175/255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRender.setProjectionMatrix(worldCam.combined);

        controller.checkKeyInput();
        controller.animateOpponentTank();

        //controller.handleTouchInput(worldCam);

        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);


        for (Body b : bodies) {
            Sprite s = (Sprite) b.getUserData();

            if (s != null) {
                s.setPosition(b.getPosition().x * (float) TankWarsGame.SCALE - s.getWidth() / 2,
                        (b.getPosition().y * (float) TankWarsGame.SCALE) - s.getHeight()/2);
                if (s.equals(tank1.getChassisSprite())) {
                    s.setOrigin(s.getWidth() / 2, s.getHeight());
                    s.setRotation(tank1.getTankAngle());
                }
                if (s.equals(tank2.getChassisSprite())) {
                    s.setOrigin(s.getWidth() / 2, s.getHeight());
                    s.setRotation(tank2.getTankAngle());
                }
                if (s.equals(tank1.getCannonSprite())) {
                    s.setOrigin(s.getWidth() / 2, s.getHeight());
                    s.setRotation(tank1.getCannonAngle());
                }
                if (s.equals(tank2.getCannonSprite())) {
                    s.setOrigin(s.getWidth() / 2 , s.getHeight());
                    s.setRotation(tank2.getCannonAngle());
                }
            }
        }
        shapeRender.begin(ShapeRenderer.ShapeType.Filled);
        terrain.draw(shapeRender);
        shapeRender.end();

        tank1.checkBeenHit();
        tank2.checkBeenHit();
        controller.handleBulletDisposal(model, tank1);
        controller.handleBulletDisposal(model, tank2);

        batch.begin();
        if (tank1.getBullet() != null) {
            tank1.getBullet().getBulletSprite().draw(batch);
        }
        if (tank2.getBullet() != null) {
            tank2.getBullet().getBulletSprite().draw(batch);
        }
        tank1.getChassisSprite().draw(batch);
        tank1.getCannonSprite().draw(batch);
        tank2.getChassisSprite().draw(batch);
        tank2.getCannonSprite().draw(batch);
        batch.end();

        controller.checkGameOver();

        batch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getHealthProgressBarOpponent().setValue(tank2.getHealth());
        hud.getHealthProgressBarPlayer().setValue(tank1.getHealth());
        hud.getStage().draw();

        model.destroyDeadBodies();
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(hud.getStage());
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
        Gdx.input.setInputProcessor(null);
        dispose();
    }

    @Override
    public void dispose() {
        tank1.getChassisSprite().getTexture().dispose();
        tank1.getCannonSprite().getTexture().dispose();
        tank2.getChassisSprite().getTexture().dispose();
        tank2.getCannonSprite().getTexture().dispose();
        hud.getStage().dispose();
        batch.dispose();
        shapeRender.dispose();
    }

    private float scale(float value) {
        return value / TankWarsGame.SCALE;
    }

}
