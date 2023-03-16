package com.game.tankwars.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Arrays;

public class Terrain {
    World world;
    Body body;
    final Vector2[] vertices;
    Vector2 position;

    public Terrain(){

        float xStart = -5;
        float xEnd = 100;
        float yMin = 1f;
        float yMax = 10;
        int points = 10;
        int vertNumber = 1000;

        this.world = Box2dWorld.getWorld();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0,0);

        vertices = new TerrainGenerator(xStart, xEnd, yMin, yMax, points).generateVertices(vertNumber);

        ChainShape chainShape = new ChainShape();
        chainShape.createChain(vertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0;
        fixtureDef.shape = chainShape;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);

        chainShape.dispose();
    }
}
