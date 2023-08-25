package com.example.socialmediaapi.repo;

import com.example.socialmediaapi.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * jpa repository contains the basic methods interacting with db like save, find by id etc.
 */
public interface PostRepository extends JpaRepository<Post, Long> {
}
