package com.game.tankwars.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Tank {

    public static final int TANK_MOVESPEED = 2;
    public static final int TANK_WIDTH = 50;
    public static final int TANK_HEIGHT = 50;

    private Vector2 position;

    private Rectangle bounds;

    private Texture texture;

    public Tank(Vector2 position, Texture texture) {
        this.position = position;
        this.bounds = new Rectangle(position.x, position.y, TANK_WIDTH, TANK_HEIGHT);
        this.texture = texture;
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
        position.x += TANK_MOVESPEED;
    }

    public void moveLeft() {
        position.x -= TANK_MOVESPEED;
    }


}
