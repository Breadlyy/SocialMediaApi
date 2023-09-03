package com.example.socialmediaapi.service;

import com.example.socialmediaapi.entity.Post;
import com.example.socialmediaapi.repo.PostRepository;
import com.example.socialmediaapi.response.error.PostNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * PostService methods being used by PostController
 */
@AllArgsConstructor
@Service
public class PostService {
    private PostRepository postRepository;

    /**
     * Save.
     *
     * @param post  the post
     * @param image the image
     */
    public void save(Post post, MultipartFile image) {
        postRepository.save(post);
    }

    /**
     * Save post.
     *
     * @param post the post
     */
    public void savePost(Post post) {
        postRepository.save(post);
    }

    /**
     * find the post by id
     *
     * @param id the id
     * @return post post
     */
    public Post findById(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent())
            return postOptional.get();
        else throw new PostNotFoundException("post with id " + id + "not found");
    }

    /**
     * update the post's text
     *
     * @param postId the post id
     * @param title  the new title
     * @param text   the new text
     */
    public void updatePost(Long postId, String title, String text) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            if (title != null) post.setTitle(title);
            if (text != null) post.setText(text);
            postRepository.save(post);
        }
    }
}
