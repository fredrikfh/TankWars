package com.game.tankwars.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.game.tankwars.TankWarsGame;
import com.game.tankwars.model.Bullet;
import com.game.tankwars.model.Tank;

import java.util.Arrays;

public class GameController {
    TankWarsGame tankWarsGame;
    Tank tank;
    Bullet bullet;
    public GameController(Tank tank, TankWarsGame tankWarsGame){
        this.tank = tank;
        this.tankWarsGame = tankWarsGame;
    }

    public void checkKeyInput(Tank tank){
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            tank.moveRight();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            tank.moveLeft();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
            tank.rotateCannonLeft();
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            tank.rotateCannonRight();
        }

        if(Gdx.input.justTouched()) {
            bullet = new Bullet(tank);
            bullet.shoot();
        }
    }
}
