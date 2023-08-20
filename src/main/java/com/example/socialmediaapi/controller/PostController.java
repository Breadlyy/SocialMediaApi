package com.example.socialmediaapi.controller;

import com.example.socialmediaapi.entity.Account;
import com.example.socialmediaapi.entity.ImageEntity;
import com.example.socialmediaapi.entity.Post;
import com.example.socialmediaapi.repo.AccountRepository;
import com.example.socialmediaapi.repo.PostRepository;
import com.example.socialmediaapi.service.PostService;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class PostController {
    PostService postService;
    PostRepository postRepository;
    AccountRepository accountRepository;

    @GetMapping("/post_page")
    public String PostCreation(Model model)
    {
        List<Post> posts = postRepository.findAll();
        model.addAttribute("post", new Post());
        model.addAttribute("posts", posts);
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
                             @AuthenticationPrincipal UserDetails userDetails)
    {
        Post post = new Post();
        post.setTitle(title);
        post.setText(text);
        Optional<Account> acc = accountRepository.findByUsername(userDetails.getUsername());
        Account account = acc.get();
        post.setAccount(account);
        try
        {
            post.setImage(image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        postRepository.save(post);
        return "redirect:/post_page";
    }
}
