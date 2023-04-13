package com.game.tankwars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpResponse;

/**
 * Handles request and response from the HTTP request using
 * a passed callback function that treats request success, failure and cancellation.
 * Implements the retry tactic by resending the request on failure
 * a few times with increasing backoff time between resends.
 */
public class HTTPRequestHandler implements Net.HttpResponseListener {
    private final Callback callback;
    private final Net.HttpRequest request;
    private int attempts = 0;
    private final int MAX_ATTEMPTS = 3;
    private final int BACKOFF_TIME = 300;

    public HTTPRequestHandler(Callback callback, Net.HttpRequest request) {
        this.callback = callback;
        this.request = request;
    }

    /**
     * Send the HTTP request
     */
    public void sendRequest() {
        Gdx.net.sendHttpRequest(request, this);
    }


    /**
     * Request was successful and response received. Passes response body
     * to callback.
     *
     * @param httpResponse The {@link HttpResponse} with the HTTP response values.
     */
    public void handleHttpResponse(HttpResponse httpResponse) {
        if (!callback.onResult(httpResponse))
            failed(new Throwable("Response returned with status code " +
                    httpResponse.getStatus().getStatusCode()));
    }

    /**
     * Request failed. Request will be retried until the attempts
     * have been exhausted with an increasing backoff time between each retry.
     *
     * @param t If the HTTP request failed because an Exception, t encapsulates it to give more information.
     */
    public void failed(Throwable t) {
        if (attempts < MAX_ATTEMPTS) {
            attempts++;

            try {
                Thread.sleep((long) attempts * BACKOFF_TIME);
                sendRequest();
                return;
            } catch(InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }

        callback.onFailed(t);
    }

    /**
     * Request was cancelled
     */
    public void cancelled() {
        System.out.println("Request cancelled");
    }
}