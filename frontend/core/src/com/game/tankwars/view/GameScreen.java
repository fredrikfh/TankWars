package com.game.tankwars.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.game.tankwars.TankWarsGame;
import com.game.tankwars.model.Box2dWorld;
import com.game.tankwars.model.Bullet;
import com.game.tankwars.model.Tank;

public class GameScreen implements Screen {
    final TankWarsGame tankWarsGame;
    public static int VIEWPORT_WIDTH = 80;
    public static int VIEWPORT_HEIGHT = 50;

    int horizontalScaling;
    int verticalScaling;
    private SpriteBatch batch;

    private Tank tank;
    private Box2dWorld model;
    private World world;

    private OrthographicCamera cam;
    private Box2DDebugRenderer debugRenderer;

    private Bullet bullet;

    public GameScreen(final TankWarsGame tankWarsGame){
        this.tankWarsGame = tankWarsGame;
        batch = new SpriteBatch();
        model = new Box2dWorld();
        world = Box2dWorld.getWorld();
        cam = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        cam.position.set(VIEWPORT_WIDTH/2, VIEWPORT_HEIGHT/2, 0);
        cam.update();
        debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);
        Vector2 tankPos = new Vector2(0, cam.position.y/2);
        tank = new Tank(tankPos, new Texture("tank-khaki.png"));
        horizontalScaling = Gdx.graphics.getWidth() / VIEWPORT_WIDTH;
        verticalScaling = Gdx.graphics.getHeight() / VIEWPORT_HEIGHT;
    }
    @Override
    public void render(float delta) {
        model.logicStep(Gdx.graphics.getDeltaTime());
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        debugRenderer.render(world, cam.combined);

        if(Gdx.input.isKeyPressed(Input.Keys.D) && !tank.detectCollisionRight()) {
            tank.moveRight();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.A) && !tank.detectCollisionLeft()) {
            tank.moveLeft();
        } else {
            tank.stop();
        }
        if(Gdx.input.justTouched()) {
            bullet = new Bullet(tank);
            System.out.println(tank.getPosition());
            bullet.shoot();
        }

        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);

        for (Body b : bodies) {
            Sprite s = (Sprite) b.getUserData();

            if (s != null) {
                s.setPosition(b.getPosition().x * (float) horizontalScaling, b.getPosition().y * (float) verticalScaling);
            }
        }

        batch.begin();
        tank.getSprite().draw(batch);
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

    }
}
