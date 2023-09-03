package com.example.socialmediaapi.controller;

import com.example.socialmediaapi.entity.Account;
import com.example.socialmediaapi.entity.Post;
import com.example.socialmediaapi.repo.PostRepository;
import com.example.socialmediaapi.service.AccountService;
import com.example.socialmediaapi.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * PostController is responsible for displaying the user's posts to
 * the user's friends and subscribers
 */
@AllArgsConstructor
@Controller
public class PostController {
    /**
     * The Post service.
     */
    PostService postService;
    /**
     * The Post repository.
     */
    PostRepository postRepository;
    /**
     * The Account service.
     */
    AccountService accountService;

    /**
     * show the user's posts to
     * the user's friends and subscribers
     *
     * @param model       the model
     * @param userDetails the user details
     * @return string string
     */
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

    /**
     * creating the post, allowed images below the 16Mb size
     *
     * @param model       the model
     * @param image       the image
     * @param title       the title
     * @param text        the text
     * @param userDetails the user details
     * @return string string
     * @throws IOException the io exception
     */
    @PostMapping("/create_post")
    public String createPost(Model model,
                             @RequestParam("image") MultipartFile image,
                             @RequestParam("title") String title,
                             @RequestParam("text") String text,
                             @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        Post post = new Post();
        post.setTitle(title);
        post.setText(text);
        Account account = accountService.findByUsername(userDetails.getUsername());
        post.setAccount(account);
            post.setImage(image.getBytes());

        postRepository.save(post);
        return "redirect:/post_page";
    }

    /**
     * make use of directly showing the post
     *
     * @param id the id
     * @return image image
     */
    @GetMapping("/posts/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(post.getImage());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * afford to update the post's text only
     *
     * @param postId      the post id
     * @param newText     the new text
     * @param userDetails the user details
     * @return string string
     */
    @PostMapping("/update-post")
    public String updatePost(@RequestParam("post_id") Long postId, @RequestParam("text") String newText,
                             @RequestParam("title") String title,
                             @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postService.findById(postId);
        String newTitle;
        if (title != null) newTitle = title;
        else newTitle = post.getTitle();
        // Устанавливаем код состояния 302 (Found) для перенаправления

        if (post != null && post.getAccount().getUsername().equals(userDetails.getUsername())) {
            postService.updatePost(postId, newText, newTitle);
        }
        return "redirect:/post_page";
    }
}
