package com.game.tankwars.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.game.tankwars.TankWarsGame;

public class Bullet {
    private static final Vector2 IMPULSE = new Vector2(0.7f, 0.8f);
    private BodyDef bodyDef = new BodyDef();
    private FixtureDef fixtureDef = new FixtureDef();
    private World world;
    private Body body;
    private Tank tank;
    private Vector2 position;

    private Sprite bulletSprite;

    private float angle;
    private boolean collision = false;

    public Bullet(Tank tank, String id) {
        this.tank = tank;
        this.angle = tank.getCannonAngle();
        world = Box2dWorld.getWorld();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(tank.getCannonPosition().add(Tank.CANNON_WIDTH + 1, Tank.CANNON_HEIGHT + 1)));
        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(0.1f);
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.restitution = 0.0f;
        fixtureDef.friction = 1f;
        body.createFixture(fixtureDef);
        body.getFixtureList().get(0).setUserData(new FixtureData(id));


        shape.dispose();
        bulletSprite = new Sprite(new Texture("bullet.png"));
        bulletSprite.setScale(0.5f);
        body.setUserData(bulletSprite);

        position = new Vector2(body.getPosition());

    }

    public void shoot(float power) {
        Vector2 impulsePower = calculatePower(power);
        body.applyLinearImpulse(
                impulsePower.x,
                impulsePower.y,
                position.x,
                position.y,
                false);
    }

    private Vector2 calculatePower(float power) {

        float scale = power / 100;
        double radAngle = (angle * MathUtils.degRad) - (Math.PI / 2);
        float impulseX = IMPULSE.x * (float) Math.cos(radAngle);
        float impulseY = IMPULSE.y * (float) Math.sin(radAngle);

        Vector2 impulse = new Vector2(impulseX, impulseY).scl(scale);
        return impulse;
    }

    public Sprite getBulletSprite() { return bulletSprite; }


    public void removeBullet() {
        world.destroyBody(body);
        bulletSprite.getTexture().dispose();
    }

    public Body getBody() { return body; }

    public boolean collision() {
        return collision;
    }

    public void setCollision() {
        collision = true;
    }

}
