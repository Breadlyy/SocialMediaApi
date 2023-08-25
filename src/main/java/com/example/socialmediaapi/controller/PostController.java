package com.example.socialmediaapi.controller;

import com.example.socialmediaapi.entity.Account;
import com.example.socialmediaapi.entity.ImageEntity;
import com.example.socialmediaapi.entity.Post;
import com.example.socialmediaapi.repo.AccountRepository;
import com.example.socialmediaapi.repo.PostRepository;
import com.example.socialmediaapi.service.AccountService;
import com.example.socialmediaapi.service.PostService;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class PostController {
    PostService postService;
    PostRepository postRepository;
    AccountService accountService;

    @GetMapping("/post_page")
    public String PostCreation(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Post> posts = new ArrayList<>();
        Account account = accountService.findByUsername(userDetails.getUsername());
        List<Account> subscriptions = account.getSubscriptions();
        posts.addAll(account.getPosts());
        for (Account subscription : subscriptions)
        {
            posts.addAll(subscription.getPosts());
        }
        model.addAttribute("post", new Post());
        model.addAttribute("posts", posts);
        model.addAttribute("username", userDetails.getUsername());

        return "post_page";
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // или другой тип в зависимости от формата
                    .body(post.getImage());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create_post")
    public String createPost(Model model,
                             @RequestParam("image") MultipartFile image,
                             @RequestParam("title") String title,
                             @RequestParam("text") String text,
                             @AuthenticationPrincipal UserDetails userDetails) {
        Post post = new Post();
        post.setTitle(title);
        post.setText(text);
        Account account = accountService.findByUsername(userDetails.getUsername());
        post.setAccount(account);
        try {
            post.setImage(image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        postRepository.save(post);
        return "redirect:/post_page";
    }

    @PostMapping("/update-post")
    public String updatePost(@RequestParam("post_id") Long postId, @RequestParam("text") String newText,
                             @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postService.findById(postId);
        // Устанавливаем код состояния 302 (Found) для перенаправления

        if (post != null && post.getAccount().getUsername().equals(userDetails.getUsername())) {
            postService.updatePost(postId, newText);
        }
        return "redirect:/post_page";
    }
}
