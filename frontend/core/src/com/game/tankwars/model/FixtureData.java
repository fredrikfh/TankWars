package com.game.tankwars.model;

public class FixtureData {

    String id;

    Boolean collision;

    public FixtureData(String id) {
        this.id = id;
        this.collision = false;
    }

    public void hasCollided() {
        this.collision = true;
    }

    public boolean isHit() {
        return collision;
    }

    public String getId() {
        return id;
    }

    public void resetHit() {
        this.collision = false;
    }

}
