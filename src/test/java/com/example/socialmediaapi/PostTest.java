package com.example.socialmediaapi;

import com.example.socialmediaapi.entity.Post;
import com.example.socialmediaapi.repo.PostRepository;
import com.example.socialmediaapi.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostTest {

    @InjectMocks
    PostService postService;

    @Mock
    PostRepository postRepository;

    @Test
    public void checkIfPostWasUpdated() {
        Post post1 = Post.builder()
                .id(1L)
                .title("Super post")
                .text("Pure of the heart and awakened by fury")
                .build();
        Post post2 = Post.builder()
                .id(2L)
                .title("faceless post")
                .text("does it matter if i put it in my jug or not? " +
                        "Get it? Juggernaut")
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(post1));
        when(postRepository.findById(2L)).thenReturn(Optional.of(post2));
        postService.updatePost(1L, "Ultra Post", "It's not love. " +
                "It's Fury");
        postService.updatePost(2L, "Faceless Post", null);

        Assertions.assertEquals(post1.getTitle(), "Ultra Post");
        Assertions.assertEquals(post1.getText(), "It's not love. It's Fury");
        Assertions.assertEquals(post2.getTitle(), "Faceless Post");
        Assertions.assertEquals(post2.getText(), "does it matter if i put it in my jug or not? Get it? Juggernaut");
    }
}
