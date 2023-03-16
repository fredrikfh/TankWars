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
    Vector2 position;

    public Bullet(Tank tank) {
        this.position = tank.getPosition();
        position.y += 0.5;
        this.tank = tank;
        world = Box2dWorld.getWorld();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(this.position);
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
        body.applyLinearImpulse(0.4f,  0.3f, position.x-1, position.y-2, true);
        System.out.println(this.position);
    }

}
