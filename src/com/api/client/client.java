package com.api.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class client {
    HttpClient httpClient;

    public client() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMinutes(1))
                .build();
    }

    public String getData(String url) {
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(
                HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build(),
                HttpResponse.BodyHandlers.ofString()
            );
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response.body();
    }

    public String postData(String url, String body) {
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(
                    HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .POST(HttpRequest.BodyPublishers.ofString(body))
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            );
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response.body();
    }
}
