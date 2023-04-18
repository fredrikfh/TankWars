package com.game.tankwars.model;

public class UserTank {
    User user;
    Stats stats;

    public UserTank() {

    }

    public UserTank(User user, Stats stats) {
        this.user = user;
        this.stats = stats;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }
}
