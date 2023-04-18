package com.game.tankwars.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Box2dWorld {

    private Array<Body> deadBodies = new Array<>();

    private static final World world = new World(new Vector2(0, -10), true);

    public void logicStep(float dt) {
        world.step(dt, 3, 3);
    }

    public static World getWorld() {
        return world;
    }

    public void addDeadBody(Body body) {
        deadBodies.add(body);
    }

    public void destroyDeadBodies() {
        if (deadBodies.size > 0) {
            for (Body b : deadBodies) {
                if (b.isActive()) world.destroyBody(b);
            }
            deadBodies.clear();
        }
    }


}
