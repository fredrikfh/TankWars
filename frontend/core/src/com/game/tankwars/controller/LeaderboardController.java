package com.game.tankwars.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.game.tankwars.Callback;
import com.game.tankwars.ConfigReader;
import com.game.tankwars.ReceiverHandler;
import com.game.tankwars.model.User;

public class LeaderboardController {
    Array<User> leaderboardUsers;
    private int retries = 0;

    public void fetchLeaderboard() {
        // Create a new instance of the Callback interface to handle the response
        Callback callback = new Callback() {
            // Method called if the response is successful
            @Override
            public void onResult(String result) {
                // Create a new Json instance to parse the response body
                Json json = new Json();
                // Convert the response body to an Array of User objects using the Json instance
                leaderboardUsers = json.fromJson(Array.class, User.class, result);
            }
            // Method called if the response fails
            @Override
            public void onFailed(Throwable t){
                // Increment the number of retries
                retries += 1;
            }
        };
        // Define the URL for the HTTP request
        String url = ConfigReader.getProperty("backend.url") + "/highscores";

        // Create a new HttpRequest using the HttpRequestBuilder class
        Net.HttpRequest httpRequest = new HttpRequestBuilder()
                .newRequest()
                .method(Net.HttpMethods.GET)
                .url(url)
                .build();
        // Send the HttpRequest and pass in the Callback instance to handle the response
        Gdx.net.sendHttpRequest(httpRequest, new ReceiverHandler(callback));
    }

    public Array<User> getLeaderboard() {
        if (retries < 5 && leaderboardUsers == null) {
            fetchLeaderboard();
        }
        return leaderboardUsers;
    }
}
