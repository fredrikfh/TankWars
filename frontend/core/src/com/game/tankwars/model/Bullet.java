package com.game.tankwars.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Bullet {

    BodyDef bodyDef = new BodyDef();
    FixtureDef fixtureDef = new FixtureDef();
    World world;
    Body body;
    Tank tank;
    private Vector2 position;

    public Bullet(Tank tank) {
        this.tank = tank;
        //this.position = new Vector2(tank.getPosition().add(0, 1f));
        world = Box2dWorld.getWorld();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(tank.getPosition().add(0, 1f)));
        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(0.1f);
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.restitution = 0.6f;
        fixtureDef.friction = 0.4f;
        body.createFixture(fixtureDef);

        shape.dispose();

    }

    public void shoot() {
        body.applyLinearImpulse(
                0.2f,
                0.3f,
                new Vector2(tank.getPosition()).x-1,
                new Vector2(tank.getPosition()).y-2,
                false);
    }

}
