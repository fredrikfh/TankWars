package com.game.tankwars;

import com.badlogic.gdx.Net;

public interface Callback {
    // This interface defines two methods to handle the different responses from an HTTP request

    // Method to handle a successful response
    // Takes a String parameter containing the response body
    void onResult(Net.HttpResponse result);

    // Method to handle a failed response
    // Takes a Throwable parameter containing the exception that caused the failure
    void onFailed(Throwable t);
}