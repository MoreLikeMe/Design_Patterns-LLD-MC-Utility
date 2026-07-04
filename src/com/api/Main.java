package com.api;

import com.api.client.ToDoClient;
import com.api.pojo.Todo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        ToDoClient toDoClient = new ToDoClient();
        String response = toDoClient.getData();

        ObjectMapper mapper = new ObjectMapper();
        Todo todo = mapper.readValue(response, Todo.class);
        System.out.println(todo);
    }
}
