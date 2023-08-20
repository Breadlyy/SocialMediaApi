package com.example.socialmediaapi.response.error;

public class UserAlreadyExist extends RuntimeException{
    public UserAlreadyExist(String message)
    {
        super(message);
    }
}
