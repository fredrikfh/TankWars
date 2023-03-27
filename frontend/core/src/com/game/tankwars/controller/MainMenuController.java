package com.game.tankwars.controller;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.game.tankwars.TankWarsGame;
import com.game.tankwars.model.MenuButton;
import com.game.tankwars.view.GameScreen;
import com.game.tankwars.view.LeaderboardScreen;

public class MainMenuController {

    private final TankWarsGame tankWarsGame;
    private String username;
    private final Array<MenuButton> menuButtons;


    public MainMenuController(final TankWarsGame tankWarsGame, final BitmapFont font) {
        this.tankWarsGame = tankWarsGame;

        fetchUser();

        this.menuButtons = new Array<>();
    }

    public Array<MenuButton> setMenuButtons(Texture buttonTexture, BitmapFont font) {
        this.menuButtons.add(new MenuButton(buttonTexture, font, "Find Game"));
        this.menuButtons.add(new MenuButton(buttonTexture, font,  "Leaderboard"));
        this.menuButtons.add(new MenuButton(buttonTexture, font, "Settings"));
        this.menuButtons.add(new MenuButton(buttonTexture, font,  "Log out"));

        return menuButtons;
    }


    public void handleInput(Vector3 touchPos) {

        for (int i = 0; i < menuButtons.size; i++) {
            MenuButton menuButton = menuButtons.get(i);
            if (touchPos.x >= menuButton.getX() && touchPos.x <= menuButton.getX() + menuButton.getWidth() &&
                touchPos.y >= menuButton.getY() && touchPos.y <= menuButton.getY() + menuButton.getHeight()) {

                switch (i) {
                    case 0: tankWarsGame.setScreen(new GameScreen(tankWarsGame)); break;
                    case 1: tankWarsGame.setScreen(new LeaderboardScreen(tankWarsGame)); break;
                    case 2: System.out.println("Settings button: Not yet functional"); break;
                    case 3: System.out.println("Log out button: Not yet functional"); break;
                }
            }
        }
    }

    /**
     * TODO: Fetch user from backend. For now it only creates a dummy username to pass to the screen
     */
    private void fetchUser() {
        this.username = "Commander";
    }

    public String getUsername() {
        return username;
    }
}
