package com.game.tankwars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpResponse;

/**
 * Handles request and response from the HTTP request using
 * a passed callback function that treats request success, failure and cancellation.
 * Implements the retry tactic by resending the request on failure
 * a few times with increasing backoff time between resends. Circuit pattern employed
 * to limit the amount of retries.
 */
public class HTTPRequestHandler implements Net.HttpResponseListener {
    private final HTTPRequestHandler instance = this;
    private final Callback callback;
    private final Net.HttpRequest request;
    private final int REQUEST_TIMEOUT = 6000;
    private final int MAX_ATTEMPTS = 5;
    private final int BACKOFF_TIME = 500;

    private int attempts = 0;

    public HTTPRequestHandler(Callback callback, Net.HttpRequest request) {
        this.callback = callback;
        this.request = request;
        this.request.setTimeOut(REQUEST_TIMEOUT);
    }

    /**
     * Send the HTTP request, passing the send call to the render thread
     */
    public void sendRequest() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Gdx.net.sendHttpRequest(request, instance);
            }
        });
    }

    /**
     * Retries sending the request as long as the attempts are not exhausted.
     * The back-off time between each request increases with the number of attempts.
     * @return {@code true} if a successful retry was made
     *         {@code false} otherwise
     */
    private boolean retry() {
        if (attempts < MAX_ATTEMPTS) {
            attempts++;

            try {
                Thread.sleep((long) Math.pow(attempts, 1.3) * BACKOFF_TIME);
                sendRequest();
                return true;
            } catch(InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }

        return false;
    }


    /**
     * Response received from server. Passing response to callback.
     * If callback returns {@code false}, request is retried.
     *
     * @param httpResponse The {@link HttpResponse} with the HTTP response values.
     */
    public void handleHttpResponse(HttpResponse httpResponse) {
        boolean succeeded = callback.onResult(httpResponse);

        if (!succeeded) failed(new Throwable("Response returned with status code " +
                    httpResponse.getStatus().getStatusCode()));
    }

    /**
     * Request failed. Request will be retried until the attempts
     * have been exhausted, at which point the callback is called.
     *
     * @param t If the HTTP request failed because an Exception, t encapsulates it to give more information.
     */
    public void failed(Throwable t) {
        boolean didRetry = retry();

        if (!didRetry) callback.onFailed(t);
    }

    /**
     * Request was cancelled. Nothing happens.
     */
    public void cancelled() {
        System.out.println("Request cancelled");
    }
}