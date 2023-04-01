package com.game.tankwars.model;

public class User {
    int games;
    float highscore;
    int losses;
    public int wins;
    public String username;
    public String id;

    public User() {
        // Initialize default values for the fields
        games = 0;
        highscore = 0.0f;
        losses = 0;
        wins = 0;
        username = null;
        id = null;
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
