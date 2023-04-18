package com.game.tankwars.model;

public class User {
    public Integer games;
    public Float highscore;
    public Integer losses;
    public Integer wins;
    public String username;
    private String id;

    public User() {
        // Initialize default values for the fields
    }

    public Integer getGames() {
        return games;
    }

    public void setGames(Integer games) {
        this.games = games;
    }

    public Float getHighscore() {
        return highscore;
    }

    public void setHighscore(Float highscore) {
        this.highscore = highscore;
    }

    public Integer getLosses() {
        return losses;
    }

    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void incrementGames() {
        games += 1;
    }

    public void incrementWins() {
        wins += 1;
    }

    public void incrementLosses() {
        losses += 1;
    }

    public void increaseHighscore(float highscore) {
        this.highscore += highscore;
    }

    @Override
    public String toString() {
        return "User {" +
                "games=" + games +
                ", highscore=" + highscore +
                ", losses=" + losses +
                ", wins=" + wins +
                ", username='" + username + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
