package com.game.tankwars.model;

import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    String gameId;

    Boolean gameStatus;
    Integer currentTurn;
    Array<UserTank> users;

    public GameState() {

    }

    public GameState(String gameId, boolean gameStatus, int currentTurn) {
        this.gameId = gameId;
        this.gameStatus = gameStatus;
        this.currentTurn = currentTurn;
        this.users = new Array<>();
    }

    public void setUsers(Array<User> users, Array<Tank> tanks) {
        for (int i = 0; i < users.size; i++) {

            Tank tank = tanks.get(i);
            GameStateTank gameStateTank = new GameStateTank(
                    tank.getPosition().x,
                    tank.getCannonAngle(),
                    tank.getHealth(),
                    0,
                    0,
                    true,
                    "direction",
                    "tankType"
            );
            User user = users.get(i);
            this.users.add(new UserTank(user, gameStateTank));
        }
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Boolean getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(Boolean gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Integer getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(Integer currentTurn) {
        this.currentTurn = currentTurn;
    }

    public Array<UserTank> getUsers() {
        return users;
    }

    public void setUsers(Array<UserTank> users) {
        this.users = users;
    }
}
