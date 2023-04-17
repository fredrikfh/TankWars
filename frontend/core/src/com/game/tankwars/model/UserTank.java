package com.game.tankwars.model;

public class UserTank {
    User user;
    GameStateTank stats;

    public UserTank() {

    }

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
