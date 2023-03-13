package com.game.tankwars.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuButton {

    /* TODO: Find better way to handle textures */
    private final Texture buttonTexture;

    private final BitmapFont font;
    private float x, y, contentX, contentY;
    private final float width, height;
    private String content;

    private final GlyphLayout contentLayout;

    public MenuButton(Texture buttonTexture, BitmapFont font, String content) {
        this.buttonTexture = buttonTexture;
        this.font = font;
        this.content = content;

        this.width = buttonTexture.getWidth();
        this.height = buttonTexture.getHeight();

        this.contentLayout = new GlyphLayout(font, content);
    }

    public Texture getTexture() {
        return buttonTexture;
    }

    public float getX() { return x; }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getContentX() {
        return contentX;
    }

    public float getContentY() {
        return contentY;
    }

    public float getContentWidth() {
        return contentLayout.width;
    }

    public float getContentHeight() {
        return contentLayout.height;
    }

    public String getContent() {
        return content;
    }

    /**
     * Sets x- and y-coordinates of the button and its content relative to
     * the button's centre. Content is also centred.
     *
     * @param x x-coordinate for the centre of the button
     * @param y y-coordinate for the centre of the button
     */
    public void setPosition(float x, float y) {
        this.x = x - width / 2;
        this.y = y - height / 2;
        this.contentX = x - getContentWidth() / 2;
        this.contentY = y + getContentHeight() / 2;
    }

    /**
     * Sets the textual content and the font.
     * Must also be called whenever the font size changes.
     *
     * @param font The font to use for the content
     * @param content The button's textual content
     */
    public void setContent(BitmapFont font, String content) {
        this.content = content;
        contentLayout.setText(font, content);
    }

    public void setContent(String content) {
        setContent(font, content);
    }
}
