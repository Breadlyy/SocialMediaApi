package com.example.socialmediaapi.response.error;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(String message)
    {
        super(message);
    }
}

