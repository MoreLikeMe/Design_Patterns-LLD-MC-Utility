package com.api.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        client apiClient = new client();
        String url = "https://jsonplaceholder.typicode.com/todos/1";
        String response = apiClient.getData(url);

        ObjectMapper mapper = new ObjectMapper();
        try {
            Todo todo = mapper.readValue(response, Todo.class);
            System.out.println(todo);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String postString = "{\"userId\":1,\"le\":\"New Title\",\"body\":\"New Todo\"}";
        String postApiUrl = "https://jsonplaceholder.typicode.com/posts";
        String postResponse = apiClient.postData(postApiUrl, postString);
        System.out.println(postResponse);

        String getUrl = "https://jsonplaceholder.typicode.com/posts/1";
        String getResponse = apiClient.getData(getUrl);
        System.out.println(getResponse);
    }
}
