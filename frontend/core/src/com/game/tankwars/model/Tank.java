package com.game.tankwars.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.game.tankwars.TankWarsGame;

public class Tank {

    public static final int TANK_MOVESPEED = 10;
    public static final int TANK_WIDTH = 2;
    public static final int TANK_HEIGHT = 1;

    private Vector2 position;

    private Rectangle bounds;

    private Texture texture;
    private Sprite sprite;
    World world;
    Body body;
    BodyDef bodyDef = new BodyDef();
    FixtureDef fixtureDef = new FixtureDef();

    public Tank(Vector2 position, Texture texture) {
        this.position = position;
        this.bounds = new Rectangle(position.x, position.y, TANK_WIDTH, TANK_HEIGHT);
        this.texture = texture;
        sprite = new Sprite(texture);
        sprite.setPosition(TankWarsGame.VIEWPORT_WIDTH/2, TankWarsGame.VIEWPORT_HEIGHT/2 + TANK_HEIGHT);

        world = Box2dWorld.getWorld();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(sprite.getX(), sprite.getY());
        body = world.createBody(bodyDef);
        body.setUserData(sprite);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(TANK_WIDTH, TANK_HEIGHT);
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Texture getTexture() {
        return texture;
    }

    public Sprite getSprite() {return sprite;}

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }


    public void moveRight() {
        body.setLinearVelocity(TANK_MOVESPEED, 0);
        position = body.getPosition();
    }

    public void moveLeft() {
        body.setLinearVelocity(- TANK_MOVESPEED, 0);
        position = body.getPosition();
    }

    public void stop() {body.setLinearVelocity(0, 0);}

    public boolean detectCollisionLeft() {
        return position.x < 0;
    }

    public boolean detectCollisionRight() {
        return TankWarsGame.VIEWPORT_WIDTH - TANK_WIDTH < position.x;
    }


}
