package com.api.client;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * ToDoClient is a concrete implementation of the Client class that fetches data from a specific URL.
 * It overrides the abstract methods to provide the URL, prepare the HTTP request, and specify the response body handler.
 *
 * TODO: This class should return Todo object instead of String. The current implementation returns a String response, which is not ideal for type safety and data handling.
 * TODO: The getData() method in the Client class should be modified to return a Generic object,
 */

public class ToDoClient extends Client<String> {

    @Override
    public String getUrl() {
        //return "https://jsonplaceholder.typicode.com/todos/1";
        return "https://jsonplaceholder.typicode.com/todo/1";
    }

    @Override
    public HttpRequest prepareHttpRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
    }

    @Override
    public HttpResponse.BodyHandler<String> getHttpResponseBodyHandler() {
        return HttpResponse.BodyHandlers.ofString();
    }
}
