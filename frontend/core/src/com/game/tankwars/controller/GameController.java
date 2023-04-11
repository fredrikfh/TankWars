package com.game.tankwars.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.game.tankwars.TankWarsGame;
import com.game.tankwars.model.Bullet;
import com.game.tankwars.model.Tank;
import com.game.tankwars.view.GameHud;

public class GameController {

    private TankWarsGame tankWarsGame;

    private GameHud hud;

    private Tank tank;

    private Bullet bullet;

    private boolean moveRightTouched;
    private boolean moveLeftTouched;

    public GameController(Tank tank, TankWarsGame tankWarsGame, GameHud hud) {
        this.hud = hud;
        this.tank = tank;
        this.tankWarsGame = tankWarsGame;
    }

    public void checkKeyInput(Tank tank){
        if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
            tank.rotateCannonLeft();
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            tank.rotateCannonRight();
        }

        if(moveRightTouched) {
            tank.moveRight();
        }
        else if(moveLeftTouched) {
            tank.moveLeft();
        }
    }

    public void handleHudEvents() {
        hud.getFireButton().addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                bullet = new Bullet(tank);
                bullet.shoot();
                //actor.setTouchable(Touchable.disabled);
                // TODO: send turn  to server + enable touchable when it is players turn
                endPlayerTurn();
            }
        });

        hud.getPowerSlider().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                System.out.println(hud.getPowerSlider().getValue());
                tank.setPower(Math.round(hud.getPowerSlider().getValue()));
            }
        });

        hud.getMoveLeft().addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                moveLeftTouched = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                moveLeftTouched = false;
            }
        });

        hud.getMoveRight().addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                moveRightTouched = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                moveRightTouched = false;
            }

        });


    }

    public boolean endPlayerTurn() {
        System.out.println(tank.getPower());
        System.out.println(tank.getPosition());
        // end turn for player and send data to server
        return true;
    }
}
