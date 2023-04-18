package com.game.tankwars.model;

import com.badlogic.gdx.utils.Array;

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

    public void setUsers(User[] users, Tank[] tanks) {
        for (int i = 0; i < users.length; i++) {

            Tank tank = tanks[i];
            Stats stats = new Stats(
                    tank.getPosInVertArr(),
                    tank.getCannonAngle(),
                    tank.getPower(),
                    tank.getHealth(),
                    0,
                    0,
                    true,
                    tank.directionLeft ? "left" : "right",
                    "tankType"
            );
            User user = users[i];
            this.users.add(new UserTank(user, stats));
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
