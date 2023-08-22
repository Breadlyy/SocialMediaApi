package com.example.socialmediaapi.service;

import com.example.socialmediaapi.entity.Post;
import com.example.socialmediaapi.repo.PostRepository;
import com.example.socialmediaapi.response.error.PostNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

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
    public Post findById(Long id)
    {
        Optional<Post> postOptional = postRepository.findById(id);
        if(postOptional.isPresent())
            return postOptional.get();
        else throw new PostNotFoundException("post with id " + id + "not found");
    }

    public void updatePost(Long postId, String newContent) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            post.setText(newContent);
            postRepository.save(post);
        }
    }
}
