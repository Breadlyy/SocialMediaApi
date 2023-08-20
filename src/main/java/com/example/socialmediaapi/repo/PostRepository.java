package com.example.socialmediaapi.repo;

import com.example.socialmediaapi.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
