package com.game.tankwars.model;

public class CurrentUser {

    private User user;
    private static CurrentUser INSTANCE;
    private String gameId;
    private int turnIndex;

    private CurrentUser() {
    }

    public static CurrentUser getCurrentUser(){
        if (INSTANCE == null) {
            INSTANCE = new CurrentUser();
        }
        return INSTANCE;
    }
    public User getUser() {
        return user;
    }

    public int getTurnIndex() {
        return turnIndex;
    }

    public void setTurnIndex(int turnIndex) {
        this.turnIndex = turnIndex;
    }

    public String getGameId() { return gameId; }
    public void setUser(User user) {
        this.user = user;
    }

    public void setGameId(String gameId) { this.gameId = gameId; }

    @Override
    public String toString() {
        return "CurrentUser is: " + user ;
    }
}
