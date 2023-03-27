package com.game.tankwars;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpResponse;
import com.game.tankwars.controller.LeaderboardController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ReceiverHandler implements Net.HttpResponseListener {
    // This class handles the response from the HTTP request
    // It uses the callback interface to handle the different responses

    // Declare a Callback instance to handle the different responses
    private final Callback callback;

    // Constructor to initialize the Callback instance
    public ReceiverHandler(Callback callback) {
        this.callback = callback;
    }

    // Method called if the request was cancelled
    public void cancelled() {
        System.out.println("cancelled");
    }

    // Method called if the request failed
    public void failed(Throwable t) {
        // Call the onFailed() method of the Callback instance
        callback.onFailed(t);
        // Print the error to the console
        System.err.println(t);
    }

    // Method called if the request was successful and a response was received
    public void handleHttpResponse(HttpResponse httpResponse) {
        // Call the onResult() method of the Callback instance
        // Pass in the response body as a String
        callback.onResult(httpResponse.getResultAsString());
    }
}