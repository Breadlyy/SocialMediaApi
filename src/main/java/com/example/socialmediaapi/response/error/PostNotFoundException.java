package com.example.socialmediaapi.response.error;

/**
 * The type Post not found exception.
 */
public class PostNotFoundException extends RuntimeException{
    /**
     * Instantiates a new Post not found exception.
     *
     * @param message the message
     */
    public PostNotFoundException(String message)
    {
        super(message);
    }
}

