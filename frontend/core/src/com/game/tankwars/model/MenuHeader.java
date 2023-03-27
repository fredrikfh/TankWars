package com.game.tankwars.model;

import com.badlogic.gdx.graphics.Texture;

public class MenuHeader {

    private final Texture texture;

    public MenuHeader() {
        texture = new Texture("main-menu-welcome-box.png");
    }

    public Texture getTexture() {
        return  texture;
    }

}
