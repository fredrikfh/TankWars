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
import com.game.tankwars.view.GameScreen;

public class Tank {
    TankWarsGame tankWarsGame;
    public static final int TANK_WIDTH = 2;
    public static final int TANK_HEIGHT = 1;
    int VIEWPORT_WIDTH;
    int VIEWPORT_HEIGHT;
    private Vector2 position;
    private Rectangle bounds;
    private Texture texture;
    private Sprite sprite;
    World world;
    Body body;
    BodyDef bodyDef = new BodyDef();
    FixtureDef fixtureDef = new FixtureDef();
    Terrain terrain;
    Vector2[] vertices;
    int posInVertArr;

    public Tank(int posInVertArr, Texture texture, Terrain terrain, TankWarsGame tankWarsGame) {
        VIEWPORT_HEIGHT = tankWarsGame.getViewportHeight();
        VIEWPORT_WIDTH = tankWarsGame.getViewportWidth();

        this.terrain = terrain;
        vertices = terrain.getVertices();

        this.posInVertArr = posInVertArr;
        position = vertices[posInVertArr];

        this.bounds = new Rectangle(position.x, position.y, TANK_WIDTH, TANK_HEIGHT);

        this.texture = texture;
        sprite = new Sprite(texture);
        sprite.setPosition(VIEWPORT_WIDTH/2, VIEWPORT_HEIGHT/2 + TANK_HEIGHT);
        sprite.setRotation(0);

        world = Box2dWorld.getWorld();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(position.x, position.y);
        body = world.createBody(bodyDef);
        body.setUserData(sprite);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(TANK_WIDTH, TANK_HEIGHT);
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void moveRight() {
        Vector2 curPos = vertices[posInVertArr];
        Vector2 newPos = vertices[posInVertArr + 1];
        float angle = new Vector2(newPos.x - curPos.x, newPos.y - curPos.y).angleRad();

        if (body.getPosition().x <= VIEWPORT_WIDTH - TANK_WIDTH){
            body.setTransform(newPos.x, newPos.y + 0.11f, angle);
            sprite.setRotation(angle);
            position.set(newPos);
            posInVertArr++;
        }
        else {
            body.setTransform(curPos.x, curPos.y + 0.11f, angle);
        }
    }

    public void moveLeft() {
        Vector2 curPos = vertices[posInVertArr];
        Vector2 newPos = vertices[posInVertArr - 1];
        float angle = new Vector2(newPos.x - curPos.x, newPos.y - curPos.y).angleRad();

        if (body.getPosition().x >= TANK_WIDTH){
            body.setTransform(newPos.x, newPos.y + 0.11f, angle);
            sprite.setRotation(angle);
            position.set(newPos);
            posInVertArr--;
        }
        else {
            body.setTransform(curPos.x, curPos.y + 0.11f, angle);
        }
    }

    public float getAngle(){
        Vector2 curPos = vertices[posInVertArr];
        Vector2 newPos = vertices[posInVertArr + 1];
        return new Vector2(newPos.x - curPos.x, newPos.y - curPos.y).angleDeg();
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
}
