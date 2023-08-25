package com.example.socialmediaapi.controller;

import com.example.socialmediaapi.config.UserDetailsImpl;
import com.example.socialmediaapi.entity.Account;
import com.example.socialmediaapi.entity.FriendshipStatus;
import com.example.socialmediaapi.response.error.DuplicateUserException;
import com.example.socialmediaapi.service.AccountService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Controller
//@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;


    @GetMapping("/registration")
    public String registration(Model model)
    {
        model.addAttribute("account", new Account());
        return "signup";
    }
    @PostMapping("/registration")
    public String createUser(@ModelAttribute("account") Account account, Model model)
    {
//        if (accountService.userExists(account.getEmail())) {
//            throw new DuplicateUserException("Пользователь с таким именем уже существует");
//        }
        accountService.save(account);
        return"redirect:/post_page";
    }
    @GetMapping("/login")
    public String login(Model model)
    {
        model.addAttribute("account", new Account());
        return "login";
    }
//    @PostMapping("/login")
//    public String log_in(@ModelAttribute("account") Account account)
//    {
//        accountService.login(account.getUsername(), account.getPassword());
//        return"redirect:/post_page";
//    }
    @GetMapping("/greetings")
    public String greetings(Principal principal)
    {
        return "post_page";
    }

    @GetMapping("/user")
    public ResponseEntity<String> user(@AuthenticationPrincipal UserDetails userDetails)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        return ResponseEntity.ok().body(userDetails.getUsername());
    }

    @GetMapping("/")
    public String main()
    {
        return"redirect:/post_page";
    }
}
