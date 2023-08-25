package com.example.socialmediaapi.response.error;

/**
 * The type User already exist.
 */
public class UserAlreadyExist extends RuntimeException{
    /**
     * Instantiates a new User already exist.
     *
     * @param message the message
     */
    public UserAlreadyExist(String message)
    {
        super(message);
    }
}
