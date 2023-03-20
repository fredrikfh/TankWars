package com.game.tankwars.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.game.tankwars.TankWarsGame;
import com.game.tankwars.model.Bullet;
import com.game.tankwars.model.Tank;

public class GameController {
    TankWarsGame tankWarsGame;
    Tank tank;
    Bullet bullet;
    public GameController(Tank tank, TankWarsGame tankWarsGame){
        this.tank = tank;
        this.tankWarsGame = tankWarsGame;
    }

    public void checkKeyInput(){
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            tank.moveRight();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            tank.moveLeft();
        }

        if(Gdx.input.justTouched()) {
            bullet = new Bullet(tank);
            System.out.println(tank.getPosition());
            bullet.shoot();
        }
    }
}
