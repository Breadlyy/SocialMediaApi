package com.example.socialmediaapi.controller;

import com.example.socialmediaapi.entity.Account;
import com.example.socialmediaapi.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * controller for registration and login
 */
@AllArgsConstructor
@Controller
//@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    /**
     * return sign up template
     *
     * @param model the model
     * @return string string
     */
    @GetMapping("/registration")
    public String registration(Model model)
    {
        model.addAttribute("account", new Account());
        return "signup";
    }

    /**
     * save the user after registration
     *
     * @param account the account
     * @param model   the model
     * @return string string
     */
    @PostMapping("/registration")
    public String createUser(@ModelAttribute("account") Account account, Model model)
    {
//        if (accountService.userExists(account.getEmail())) {
//            throw new DuplicateUserException("Пользователь с таким именем уже существует");
//        }
        accountService.save(account);
        return"redirect:/post_page";
    }

    /**
     * return log in template
     *
     * @param model the model
     * @return string string
     */
    @GetMapping("/login")
    public String login(Model model)
    {
        model.addAttribute("account", new Account());
        return "login";
    }

    /**
     * User response entity.
     *
     * @param userDetails the user details
     * @return the response entity
     */
    @GetMapping("/user")
    public ResponseEntity<String> user(@AuthenticationPrincipal UserDetails userDetails)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        return ResponseEntity.ok().body(userDetails.getUsername());
    }

    /**
     * redirect to the post page after successful log in or registration
     *
     * @return string string
     */
    @GetMapping("/")
    public String main()
    {
        return"redirect:/post_page";
    }
}
