package com.game.tankwars.model;

public class CurrentUser {

    private User user;
    private static CurrentUser INSTANCE;
    private String gameId;
    private String lobbyId;
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

    public String getGameId() { return gameId; }
    public void setUser(User user) {
        this.user = user;
    }

    public void setGameId(String gameId) { this.gameId = gameId; }

    public void setLobbyId(String lobbyId) {
        this.lobbyId = lobbyId;
    }

    public String getLobbyId() {
        return lobbyId;
    }

    @Override
    public String toString() {
        return "CurrentUser is: " + user ;
    }
}
