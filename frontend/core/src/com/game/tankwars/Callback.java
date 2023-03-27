package com.game.tankwars;

public interface Callback {
    // This interface defines two methods to handle the different responses from an HTTP request

    // Method to handle a successful response
    // Takes a String parameter containing the response body
    void onResult(String result);

    // Method to handle a failed response
    // Takes a Throwable parameter containing the exception that caused the failure
    void onFailed(Throwable t);
}