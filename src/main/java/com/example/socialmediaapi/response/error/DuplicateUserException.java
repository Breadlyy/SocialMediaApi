package com.example.socialmediaapi.response.error;

/**
 * The type Duplicate user exception.
 */
public class DuplicateUserException extends RuntimeException{
    /**
     * Instantiates a new Duplicate user exception.
     *
     * @param message the message
     */
    public DuplicateUserException(String message)
    {
        super(message);
    }
}
