package com.api.client;

import com.api.retry.Retry;
import com.api.exception.NonRetryableException;
import com.api.exception.RetryableException;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public abstract class Client<T> {
    private final HttpClient httpClient;
    private final Retry retry;

    public Client() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMinutes(1))
                .build();
        this.retry = new Retry();
    }

    public abstract String getUrl();
    public abstract HttpRequest prepareHttpRequest(String url);
    public abstract HttpResponse.BodyHandler<T> getHttpResponseBodyHandler();

    public T getData() {
        HttpRequest request = prepareHttpRequest(getUrl());
        HttpResponse.BodyHandler<T> bodyHandler = getHttpResponseBodyHandler();
        try {
            return retry.executeWithRetry(() -> invokeEndPoint(request, bodyHandler));
        } catch (Exception e) {
            throw new RuntimeException("Failed to get data from " + getUrl(), e);
        }
    }

    public T invokeEndPoint(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler) {
        HttpResponse<T> response = null;
        try {
            response = httpClient.send(request, bodyHandler);
        } catch (IOException e) {
            throw new RetryableException("Error while invoking endpoint from " + request.uri(), e);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            throw new NonRetryableException("Request interrupted while invoking endpoint from " + request.uri(), e);
        }

        if(response.statusCode() == 200){
            return response.body();
        }
        else if(response.statusCode() >= 500 && response.statusCode() < 600){
            throw new RetryableException("Server error while invoking endpoint from " + request.uri() + ". Status code: " + response.statusCode());
        }
        else if(response.statusCode() >=400 && response.statusCode() < 500){
            throw new NonRetryableException("Client error while invoking endpoint from " + request.uri() + ". Status code: " + response.statusCode());
        }
        throw new NonRetryableException("Failed to invoke endpoint from " + request.uri() + ". Status code: " + response.statusCode());
    }

//    public String postData(String url, String body) {
//        HttpResponse<String> response = null;
//        try {
//            response = httpClient.send(
//                    HttpRequest.newBuilder()
//                            .uri(URI.create(url))
//                            .POST(HttpRequest.BodyPublishers.ofString(body))
//                            .build(),
//                    HttpResponse.BodyHandlers.ofString()
//            );
//        } catch (IOException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        if(response.statusCode() == 200){
//            return response.body();
//        }
//        throw new RuntimeException("Failed to post data to " + url + ". Status code: " + response.statusCode());
//    }
}
