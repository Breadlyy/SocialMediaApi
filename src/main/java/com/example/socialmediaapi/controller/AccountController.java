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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

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
    public String main() {
        return "redirect:/post_page";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails,
                          Model model) {
        Account account = accountService.findByUsername(userDetails.getUsername());
        if (account.getAvatar() != null) {
            model.addAttribute("avatar", Base64.getEncoder().encodeToString(account.getAvatar()));
        }
        model.addAttribute("account", account);
        return "profile";
    }

    @PostMapping("/change-avatar")
    public String changeAvatar(@RequestParam("newAvatar") MultipartFile newAvatar,
                               @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        Account account = accountService.findByUsername(userDetails.getUsername());
        account.setAvatar(newAvatar.getBytes());
        ArrayList<Integer> qwe = new ArrayList<>();
        accountService.update(account);
        return "redirect:/profile";
    }

    @PostMapping("/remove-avatar")
    public String removeAvatar(@AuthenticationPrincipal UserDetails userDetails) throws IOException {
        Account account = accountService.findByUsername(userDetails.getUsername());
        account.setAvatar(null);
        accountService.update(account);
        return "redirect:/profile";
    }
}
