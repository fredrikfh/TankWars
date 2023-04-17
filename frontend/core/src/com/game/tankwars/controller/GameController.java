package com.game.tankwars.controller;

import static com.game.tankwars.model.CurrentUser.getCurrentUser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.SerializationException;
import com.game.tankwars.Callback;
import com.game.tankwars.ConfigReader;
import com.game.tankwars.HTTPRequestHandler;
import com.game.tankwars.TankWarsGame;
import com.game.tankwars.model.Bullet;
import com.game.tankwars.model.CurrentUser;
import com.game.tankwars.model.GameState;
import com.game.tankwars.model.Tank;
import com.game.tankwars.model.User;
import com.game.tankwars.view.GameHud;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class GameController {

    private TankWarsGame tankWarsGame;

    private GameHud hud;
    private GameState gameState;

    private Tank tank;

    private Bullet bullet;
    private Vector3 touchPos;

    private boolean moveRightTouched;
    private boolean moveLeftTouched;
    private boolean aimUpTouched;
    private boolean aimDownTouched;

    private String gameId;
    private boolean gameStatus = false;
    private int currentTurn;

    private User currentUser, opponent;
    private Tank opponentTank;

    public GameController(Tank tank, TankWarsGame tankWarsGame, GameHud hud) {
        this.currentUser = CurrentUser.getCurrentUser().getUser();
        this.hud = hud;
        this.tank = tank;
        this.tankWarsGame = tankWarsGame;
        this.touchPos = new Vector3();

        this.gameId = getCurrentUser().getGameId();

        fetchGameState();
        this.opponentTank = this.tank;
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

        User currentUser = getCurrentUser().getUser();

        // Convert the JSON object to a string
        GameState gameState = new GameState(gameId, gameStatus, currentTurn);
        Array<User> userArray = new Array<>();
        Array<Tank> tankArray = new Array<>();



        if (CurrentUser.getCurrentUser().getTurnIndex() == 0) {
            userArray.add(currentUser);
            userArray.add(opponent);
            tankArray.add(tank);
            tankArray.add(opponentTank);
        } else {
            userArray.add(opponent);
            userArray.add(currentUser);
            tankArray.add(opponentTank);
            tankArray.add(tank);
        }

        gameState.setUsers(userArray, tankArray);

        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        String content = json.toJson(gameState);

        System.out.println(content);

        new HTTPRequestHandler(new Callback() {
            @Override
            public boolean onResult(Net.HttpResponse response) {
                if (response.getStatus().getStatusCode() == -1) return false;
                System.out.println(response.getStatus().getStatusCode());
                System.out.println(response.getResultAsString());


                fetchGameState();
                return true;
            }

            @Override
            public void onFailed(Throwable t) {
                System.err.println(t);
            }
        }, new HttpRequestBuilder()
                .newRequest()
                .url(ConfigReader.getProperty("backend.url") + "/game/" + gameId + "/move")
                .method(Net.HttpMethods.POST)
                .header("Content-Type", "application/json")
                .content(content)
                .build()
        ).sendRequest();
        return true;
    }

    public Bullet getBullet() {
        if (bullet != null) {
            return bullet;
        }
        return null;
    }

    private void fetchGameState() {
        System.out.println("Fetch");
        new HTTPRequestHandler(new Callback() {
            @Override
            public boolean onResult(Net.HttpResponse response) {
                HttpStatus status = response.getStatus();
                String responseString = response.getResultAsString();

                if (status.getStatusCode() == 200) {
                    System.out.println("RESPONSE 200: " + responseString);

                    try {
                        GameState gs = new Json().fromJson(GameState.class, responseString);

                        gameState = gs;
                        System.out.println("Current turn: " + gameState.getCurrentTurn());
                        currentTurn = gameState.getCurrentTurn();
                        // TODO: Figure out of opponent is index 0 or 1
                        User user1 = gameState.getUsers().get(0).getUser();
                        User user2 = gameState.getUsers().get(1).getUser();

                        if (user1.getUsername().equals(currentUser.getUsername())) {
                            CurrentUser.getCurrentUser().setTurnIndex(0);
                            opponent = user2;
                        } else {
                            CurrentUser.getCurrentUser().setTurnIndex(1);
                            opponent = user1;
                        }

                        System.out.println("Opponent: " + opponent.getUsername());
                    } catch (SerializationException e) {
                        System.out.println(responseString);
                        try {
                            Thread.sleep(1000);
                            fetchGameState();
                        } catch (InterruptedException interruptException) {
                            System.err.println(e.getMessage());
                        }
                    }
                    return true;
                } else if (status.getStatusCode() == 404) {
                    System.out.println("RESPONSE 404: " + responseString);
                }

                return false;
            }

            @Override
            public void onFailed(Throwable t) {
                System.err.println(t);
            }
        }, new HttpRequestBuilder()
                .newRequest()
                .url(ConfigReader.getProperty("backend.url") + "/game/" + gameId + "/gameState")
                .method(Net.HttpMethods.POST)
                .header("Content-Type", "application/json")
                .content(String.format("{\"username\": \"%s\"}", getCurrentUser().getUser().username))
                .build()
        ).sendRequest();
    }
}
