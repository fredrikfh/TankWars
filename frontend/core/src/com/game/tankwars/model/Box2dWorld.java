package com.game.tankwars.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Box2dWorld {

    private static final World world = new World(new Vector2(0, -10), true);

    public void logicStep(float dt) {
        world.step(dt, 3, 3);
    }

    public static World getWorld() {
        return world;
    }


}
