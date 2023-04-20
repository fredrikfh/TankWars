package com.game.tankwars.controller;

import static com.game.tankwars.model.CurrentUser.getCurrentUser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.SerializationException;
import com.game.tankwars.Callback;
import com.game.tankwars.ConfigReader;
import com.game.tankwars.HTTPRequestHandler;
import com.game.tankwars.ResourceManager;
import com.game.tankwars.TankWarsGame;
import com.game.tankwars.model.Box2dWorld;
import com.game.tankwars.model.Bullet;
import com.game.tankwars.model.CurrentUser;
import com.game.tankwars.model.FixtureData;
import com.game.tankwars.model.GameState;
import com.game.tankwars.model.Tank;
import com.game.tankwars.model.Terrain;
import com.game.tankwars.model.User;
import com.game.tankwars.view.GameHud;
import com.game.tankwars.view.MainMenuScreen;

public class GameController {

    private final TankWarsGame tankWarsGame;

    private final GameHud hud;
    private GameState gameState;

    private boolean moveRightTouched;
    private boolean moveLeftTouched;
    private boolean aimUpTouched;
    private boolean aimDownTouched;

    private final String gameId;
    private boolean isFinished = false, hasWon = false, gameEnded = false, gameStarted = false, activeAnimation = false;
    private int currentTurn, turnIndex;
    private final User currentUser;
    private User opponent;

    private final Tank tank1, tank2;
    private ChangeListener fireChangeListener, powerSliderChangeListener;
    private InputListener leaveInputListener, moveRightInputListener, moveLeftInputListener, aimUpInputListener, aimDownInputListener;

    public GameController(TankWarsGame tankWarsGame, GameHud hud, Terrain terrain) {
        this.tankWarsGame = tankWarsGame;
        this.hud = hud;

        currentUser = CurrentUser.getCurrentUser().getUser();
        gameId = getCurrentUser().getGameId();
        currentTurn = -1;

        tank1 = new Tank(50,
                new Texture("camo-tank-1.png"),
                new Texture("camo-tank-barrel.png"),
                terrain,
                false,
                120,
                "tank1");
        tank2 = new Tank(terrain.getVertices().length - 220,
                new Texture("camo-tank-1.png"),
                new Texture("camo-tank-barrel.png"),
                terrain,
                true,
                225,
                "tank2");

        hud.showTurnInformationContainer();
        hud.showOpponentTurnLabel();

        fetchGameState();

        defineEventListeners();
        setLeaveInputListener();

        ResourceManager.getInstance().loadAndGetGameStartSound().play();
    }

    private void defineEventListeners() {
        leaveInputListener = new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("leave");
                leaveLobby();
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        tankWarsGame.setScreen(new MainMenuScreen(tankWarsGame));
                    }
                });
                return true;
            }
        };

        /*
         * Fire a bullet according to tank position and turret angle, and then end turn
         */
        fireChangeListener = new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                ResourceManager.getInstance().loadAndGetCannonShotSound();
                getCurrentTank().fireBullet();

                endPlayerTurn();
            }
        };

        /*
         * Set the power with which the bullet will be fired by using the HUD slider
         */
        powerSliderChangeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getCurrentTank().setPower(Math.round(hud.getPowerSlider().getValue()));
            }
        };

        moveLeftInputListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ResourceManager.getInstance().loadAndGetDrivingTankSound().play();
                moveLeftTouched = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                moveLeftTouched = false;
            }
        };

        moveRightInputListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ResourceManager.getInstance().loadAndGetDrivingTankSound().play();
                moveRightTouched = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                moveRightTouched = false;
            }
        };

        aimUpInputListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ResourceManager.getInstance().loadAndGetRotateTurretSound().play();
                aimUpTouched = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                aimUpTouched = false;
            }
        };

        aimDownInputListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ResourceManager.getInstance().loadAndGetRotateTurretSound().play();
                aimDownTouched = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                aimDownTouched = false;
            }
        };
    }

    /**
     * Handle input requiring polling, such as aiming, moving the tank,
     * and checking for removal of on-screen banners.
     */
    public void checkKeyInput() {
        if (aimUpTouched)
            getCurrentTank().rotateCannonCounterClockwise();
        else if (aimDownTouched)
            getCurrentTank().rotateCannonClockwise();

        if (moveRightTouched) {
            getCurrentTank().moveRight();
            hud.getFuelBar().setValue(getCurrentTank().getFuel());
        }
        else if (moveLeftTouched) {
            getCurrentTank().moveLeft();
            hud.getFuelBar().setValue(getCurrentTank().getFuel());
        }

        if (Gdx.input.justTouched()) {
            if (isFinished && !gameEnded) {
                gameEnded = true;
                endGame();
            } else if (!isFinished && isCurrentTurn() && !activeAnimation) {
                hud.removeTurnInformationContainer();
                hud.removeTurnContainer();
            }
        }
    }

    public void setTurnListeners() {
        setLeaveInputListener();

        hud.getFireButton().addListener(fireChangeListener);
        hud.getPowerSlider().addListener(powerSliderChangeListener);

        hud.getMoveLeft().addListener(moveLeftInputListener);
        hud.getMoveRight().addListener(moveRightInputListener);

        hud.getAimUp().addListener(aimUpInputListener);
        hud.getAimDown().addListener(aimDownInputListener);
    }

    public void setLeaveInputListener() {
        hud.getLeaveLabel().addListener(leaveInputListener);
    }

    public void removeTurnListeners() {
        hud.getFireButton().removeListener(fireChangeListener);
        hud.getPowerSlider().removeListener(powerSliderChangeListener);

        hud.getMoveLeft().removeListener(moveLeftInputListener);
        hud.getMoveRight().removeListener(moveRightInputListener);

        hud.getAimUp().removeListener(aimUpInputListener);
        hud.getAimDown().removeListener(aimDownInputListener);
    }

    public void checkGameOver() {
        if (!isFinished && getCurrentTank().getHealth() <= 0) {
            isFinished = true;
            endPlayerTurn();
            hud.removeTurnInformationContainer();
            hud.showLoserBanner();
        } else if (!isFinished && getOpponentTank().getHealth() <= 0) {
            hasWon = true;
            isFinished = true;
            endPlayerTurn();
            hud.removeTurnInformationContainer();
            hud.showWinBanner();
        }
    }

    private void startNewTurn() {
        if (isFinished) return;
        setTurnListeners();
        hud.showTurnInformationContainer();
        hud.showYourTurnLabel();
        getCurrentTank().resetFuel();
        hud.getFuelBar().setValue(getCurrentTank().getFuel());
    }

    public void animateOpponentTank() {
        if (activeAnimation) {
            if (getOpponentTank().autoMove() || getOpponentTank().autoRotateCannon())
                return;
            if (getOpponentTank().autoFireBullet())
                return;

            activeAnimation = false;
            startNewTurn();
        }
    }

    public void handleBulletDisposal(Box2dWorld model, Tank tank) {
        if (tank.getBullet() != null) {
            if (((FixtureData) tank.getBullet().getBody().getFixtureList().get(0).getUserData()).isHit() ||
                    tank.getBullet().getBody().getPosition().x < 0 ||
                    tank.getBullet().getBody().getPosition().x > TankWarsGame.VIEWPORT_WIDTH) {
                tank.getBullet().getBulletSprite().getTexture().dispose();
                model.addDeadBody(tank.getBullet().getBody());
                tank.setBullet(null);
            }
        }
    }

    private void initializeGameState() {
        gameState = new GameState(gameId, isFinished, currentTurn);
        Tank[] tankArray = new Tank[]{tank1, tank2};

        User[] userArray = new User[2];
        userArray[turnIndex] = currentUser;
        userArray[(turnIndex + 1) % 2] = opponent;

        gameState.setUsers(userArray, tankArray);
    }

    private void fetchGameState() {
        new HTTPRequestHandler(new Callback() {
            @Override
            public boolean onResult(Net.HttpResponse response) {
                HttpStatus status = response.getStatus();
                String responseString = response.getResultAsString();
                if (isFinished) return true;

                if (status.getStatusCode() == 200) {
                    System.out.println("RESPONSE 200: " + responseString);

                    try {
                        GameState newGameState = new Json().fromJson(GameState.class, responseString);

                        User user1 = newGameState.getUsers().get(0).getUser();
                        User user2 = newGameState.getUsers().get(1).getUser();

                        hud.setPlayerName(user1.getUsername());
                        hud.setOpponentName(user2.getUsername());

                        if (user1.getUsername().equals(currentUser.getUsername())) {
                            turnIndex = 0;
                            opponent = user2;
                        } else {
                            turnIndex = 1;
                            opponent = user1;
                        }

                        int oldCurrentTurn = currentTurn;
                        currentTurn = newGameState.getCurrentTurn();

                        if (oldCurrentTurn != -1) {
                            if (!newGameState.getIsFinished()) activeAnimation = isCurrentTurn();
                            getOpponentTank().update(newGameState.getUsers()
                                    .get(turnIndex == 0 ? 1 : 0).getStats());
                        } else {
                            initializeGameState();
                            if (gameStarted) {
                                activeAnimation = isCurrentTurn();
                                getOpponentTank().update(newGameState.getUsers()
                                        .get(turnIndex == 0 ? 1 : 0).getStats());
                            } else {
                                gameStarted = true;
                                startNewTurn();
                            }
                        }
                    } catch (SerializationException e) {
                        try {
                            Thread.sleep(2500);
                        } catch (InterruptedException interruptException) {
                            System.err.println(interruptException.getMessage());
                        }

                        if (currentTurn == -1) gameStarted = true;
                        if (!gameEnded) fetchGameState();
                    }

                    return true;
                } else if (status.getStatusCode() == 404) {
                    hud.showSomethingWentWrong();
                    System.out.println("RESPONSE 404: " + responseString);
                    return true;
                } else if (status.getStatusCode() == 206) {
                    gameEnded = true;
                    hud.showOpponentLeftBanner();
                    return true;
                }
                System.out.println("Something went wrong");
                hud.showSomethingWentWrong();
                return false;
            }

            @Override
            public void onFailed(Throwable t) {
                System.err.println(t);
                hud.showSomethingWentWrong();
            }
        }, new HttpRequestBuilder()
                .newRequest()
                .url(ConfigReader.getProperty("backend.url") + "/game/" + gameId + "/gameState")
                .method(Net.HttpMethods.POST)
                .header("Content-Type", "application/json")
                .content(String.format("{\"username\": \"%s\"}", getCurrentUser().getUser().username))
                .build()).sendRequest();
    }

    public void endPlayerTurn() {
        if (gameState == null) { fetchGameState(); return; }

        gameState.setCurrentTurn(currentTurn);
        gameState.getUsers().get(turnIndex).getStats().update(getCurrentTank());
        gameState.getUsers().get((turnIndex + 1) % 2).getStats().update(getOpponentTank());
        String content = new Json(JsonWriter.OutputType.json).toJson(gameState);

        currentTurn = currentTurn == 0 ? 1 : 0;
        hud.showTurnInformationContainer();
        hud.showOpponentTurnLabel();
        removeTurnListeners();

        new HTTPRequestHandler(new Callback() {
            @Override
            public boolean onResult(Net.HttpResponse response) {
                HttpStatus status = response.getStatus();
                String responseString = response.getResultAsString();

                if (status.getStatusCode() == 200) {
                    System.out.println(responseString); // TODO: Remove sysout
                    if (!isFinished) fetchGameState();
                    return true;
                } else if (status.getStatusCode() == 400 || status.getStatusCode() == 404) {
                    System.err.println(responseString);
                    return true;
                }
                return false;
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
                .build()).sendRequest();
    }

    public void endGame() {
        System.out.println("End game");

        currentUser.incrementGames();
        if (hasWon) {
            ResourceManager.getInstance().loadAndGetVictorySound().play();
            currentUser.incrementWins();
            currentUser.increaseHighscore((float) getCurrentTank().getHealth());
        } else{
            ResourceManager.getInstance().loadAndGetLossSound().play();
            currentUser.incrementLosses();
        }

        String content = new Json(JsonWriter.OutputType.json).toJson(currentUser, User.class);

        new HTTPRequestHandler(new Callback() {
            @Override
            public boolean onResult(Net.HttpResponse response) {
                HttpStatus status = response.getStatus();
                String responseString = response.getResultAsString();

                if (status.getStatusCode() == 200) {
                    System.out.println(responseString);
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            tankWarsGame.setScreen(new MainMenuScreen(tankWarsGame));
                        }
                    });

                    return true;
                } else if (status.getStatusCode() == 400 && status.getStatusCode() == 404) {
                    System.err.println(responseString);
                    return true;
                }
                return false;
            }

            @Override
            public void onFailed(Throwable t) {
                System.err.println(t);
            }
        }, new HttpRequestBuilder()
                .newRequest()
                .url(ConfigReader.getProperty("backend.url") + "/user/" + currentUser.getUsername() + "/highscore")
                .method(Net.HttpMethods.PUT)
                .header("Content-Type", "application/json")
                .content(content)
                .build()).sendRequest();
    }

    public void leaveLobby() {
        new HTTPRequestHandler(new Callback() {
            @Override
            public boolean onResult(Net.HttpResponse result) {
                System.out.println(String.format("RESPONSE %s: %s",result.getStatus().getStatusCode(), result.getResultAsString()));
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        tankWarsGame.setScreen(new MainMenuScreen(tankWarsGame));
                    }
                });
                return true;
            }

            @Override
            public void onFailed(Throwable t) {
                System.err.println(t);
            }
        },
                new HttpRequestBuilder()
                        .newRequest()
                        .url(ConfigReader.getProperty("backend.url") + "/lobby/" + getCurrentUser().getLobbyId() + "/leave")
                        .method(Net.HttpMethods.POST)
                        .header("Content-Type", "application/json")
                        .content(String.format("{\"username\": \"%s\"}", getCurrentUser().getUser().username))
                        .build()
        ).sendRequest();

    }

    public Tank getTank1() {
        return tank1;
    }

    public Tank getTank2() {
        return tank2;
    }

    public Tank getCurrentTank() {
        return turnIndex == 0 ? tank1 : tank2;
    }

    public Tank getOpponentTank() {
        return turnIndex == 0 ? tank2 : tank1;
    }

    public boolean isCurrentTurn() {
        return currentTurn == turnIndex;
    }
}
