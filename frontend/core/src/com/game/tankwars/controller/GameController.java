package com.game.tankwars.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
    private Vector3 touchPos;

    private boolean moveRightTouched;
    private boolean moveLeftTouched;

    private boolean aimUpTouched;
    private boolean aimDownTouched;


    public GameController(Tank tank, TankWarsGame tankWarsGame, GameHud hud) {
        this.hud = hud;
        this.tank = tank;
        this.tankWarsGame = tankWarsGame;
        this.touchPos = new Vector3();

        hud.removeTurnContainer();
        hud.removeTurnInformationContainer();
    }

    public void checkKeyInput(Tank tank){
        if(aimUpTouched) {
            tank.rotateCannonLeft();
        }
        else if (aimDownTouched) {
            tank.rotateCannonRight();
        }

        if(moveRightTouched) {
            tank.moveRight();
        }
        else if(moveLeftTouched) {
            tank.moveLeft();
        }

        // TODO: remove turn container when it is your turn and have clicked screen
        /*if(Gdx.input.isTouched() && !hud.isTurnContainerVisible()) {
            hud.showTurnContainer();
        }

        if(Gdx.input.isTouched() && hud.isTurnContainerVisible()) {
            hud.removeTurnContainer();
        }*/
    }

    public void handleHudEvents() {
        hud.getFireButton().addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                bullet = new Bullet(tank);
                float power = hud.getPowerSlider().getValue();
                bullet.shoot(power);
                //actor.setTouchable(Touchable.disabled);
                // TODO: send turn  to server + enable touchable when it is players turn
                endPlayerTurn();
            }
        });

        hud.getPowerSlider().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                //System.out.println(hud.getPowerSlider().getValue());
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

        hud.getAimUp().addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Aim up start");
                aimUpTouched = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Aim up stop");
                aimUpTouched = false;
            }
        });

        hud.getAimDown().addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Aim down start");
                aimDownTouched = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Aim down stop");
                aimDownTouched = false;
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

    public boolean isGameOver() {
        if (tank.getHealth() <= 0) {
            return true;
        }
        else {
            return false;
        }
    }


    public boolean endPlayerTurn() {
        //System.out.println(tank.getPower());
        //System.out.println(tank.getPosition());
        // end turn for player and send data to server
        return true;
    }

    public Bullet getBullet() {
        if (bullet != null) {
            return bullet;
        }
        return null;
    }

}
