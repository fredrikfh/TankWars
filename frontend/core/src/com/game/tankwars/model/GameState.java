package com.game.tankwars.model;

import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    String gameId;
    boolean gameStatus;
    Integer currentTurn;
    Array<UserTank> users;

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

    public boolean isGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(boolean gameStatus) {
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

class UserTank {
    User user;
    GameStateTank stats;

    public UserTank(User user, GameStateTank tank) {
        this.user = user;
        this.stats = tank;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GameStateTank getStats() {
        return stats;
    }

    public void setStats(GameStateTank stats) {
        this.stats = stats;
    }
}

class GameStateTank {
    float position;
    float turretAngle;
    Integer health;
    Integer ammunition;
    Integer score;
    boolean isMirrored;
    String tankDirection;
    String tankType;

    public GameStateTank(float position, float turretAngle, int health, int ammunition, int score, boolean isMirrored, String tankDirection, String tankType) {
        this.position = position;
        this.turretAngle = turretAngle;
        this.health = health;
        this.ammunition = ammunition;
        this.score = score;
        this.isMirrored = isMirrored;
        this.tankDirection = tankDirection;
        this.tankType = tankType;
    }

    public float getPosition() {
        return position;
    }

    public void setPosition(float position) {
        this.position = position;
    }

    public float getTurretAngle() {
        return turretAngle;
    }

    public void setTurretAngle(float turretAngle) {
        this.turretAngle = turretAngle;
    }

    public Integer getHealth() {
        return health;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public Integer getAmmunition() {
        return ammunition;
    }

    public void setAmmunition(Integer ammunition) {
        this.ammunition = ammunition;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public boolean isMirrored() {
        return isMirrored;
    }

    public void setMirrored(boolean mirrored) {
        isMirrored = mirrored;
    }

    public String getTankDirection() {
        return tankDirection;
    }

    public void setTankDirection(String tankDirection) {
        this.tankDirection = tankDirection;
    }

    public String getTankType() {
        return tankType;
    }

    public void setTankType(String tankType) {
        this.tankType = tankType;
    }
}
