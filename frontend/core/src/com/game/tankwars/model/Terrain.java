package com.game.tankwars.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.game.tankwars.TankWarsGame;
import com.game.tankwars.controller.TerrainController;

import java.util.ArrayList;
import java.util.Arrays;

public class Terrain {
    World world;
    Body body;
    TerrainController terrainController;
    Vector2[] vertices;
    ArrayList<Integer> seedArray;

    public Terrain(ArrayList<Integer> seed) {
        this.seedArray = seed;

        float xStart = -5;
        float xEnd = 100;
        float yMin = 5f;
        float yMax = 15;
        int points = seedArray.size();
        int vertNumber = 1000;

        this.world = Box2dWorld.getWorld();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        vertices = new TerrainGenerator(xStart, xEnd, yMin, yMax, points, seedArray).generateVertices(vertNumber);

        ChainShape chainShape = new ChainShape();
        chainShape.createChain(vertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0;
        fixtureDef.shape = chainShape;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.getFixtureList().get(0).setUserData(new FixtureData("terrain"));

        chainShape.dispose();
    }

    public Vector2[] getVertices() {
        return this.vertices.clone();
    }

    public void draw(ShapeRenderer sp) {
        sp.setColor(Color.valueOf("#5f9a61"));

        for (int i = 0; i < vertices.length-1; i++) {
                sp.triangle(vertices[i].x, vertices[i].y,
                            vertices[i + 1].x, vertices[i + 1].y,
                            vertices[i].x, 0);

                sp.triangle(vertices[i + 1].x, vertices[i + 1].y,
                            vertices[i].x, 0,
                            vertices[i + 1].x, 0);
        }
    }
}


