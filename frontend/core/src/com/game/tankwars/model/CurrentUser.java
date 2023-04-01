package com.game.tankwars.model;

public class CurrentUser {

    User user;
    private static CurrentUser INSTANCE;

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
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "CurrentUser is: " + user ;
    }
}
