package com.example.socialmediaapi.response.error;

public class DuplicateUserException extends RuntimeException{
    public DuplicateUserException(String message)
    {
        super(message);
    }
}
