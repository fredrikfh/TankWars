package com.game.tankwars;

import com.badlogic.gdx.Net;

/**
 * This interface defines two methods to handle the different responses from an HTTP request
 */
public interface Callback {

    /**
     * Method to handle a successful response
     * @param result the response body
     * @return true if response was accepted
     *         false if the request should be resent
     */
    boolean onResult(Net.HttpResponse result);

    /**
     * Method to handle a failed response
     * @param t Throwable containing the exception that caused the failure
     */
    void onFailed(Throwable t);
}