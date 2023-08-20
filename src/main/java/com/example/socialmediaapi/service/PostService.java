package com.example.socialmediaapi.service;

import com.example.socialmediaapi.entity.Post;
import com.example.socialmediaapi.repo.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Service
public class PostService {
    private PostRepository postRepository;
    public void save(Post post, MultipartFile image) {
        postRepository.save(post);
    }
    public void savePost(Post post) {
        postRepository.save(post);
    }
}
