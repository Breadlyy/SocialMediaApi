package com.example.socialmediaapi.controller;

import com.example.socialmediaapi.entity.Account;
import com.example.socialmediaapi.service.AccountService;
import com.example.socialmediaapi.service.FriendService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class FriendController {

    AccountService accountService;
    FriendService friendService;
    @GetMapping("/users")
    public String users(Model model, @AuthenticationPrincipal UserDetails userDetails)
    {
        List<Account> users = accountService.findAll();
        users.remove(accountService.findByUsername(userDetails.getUsername()));
        model.addAttribute("users", users);
        return "all_users";
    }
    @GetMapping("/friends")
    public String friends(Model model, @AuthenticationPrincipal UserDetails userDetails)
    {
        Account account = accountService.findByUsername(userDetails.getUsername());

        model.addAttribute("friends", account.getFriends());
        return "friends";
    }
    @GetMapping("/friend-request")
    public String showFriendRequests(Model model, @AuthenticationPrincipal UserDetails userDetails)
    {
        Account account= accountService.findByUsername(userDetails.getUsername());
        model.addAttribute("friendRequest", account.getFriendRequestsReceived());
        return "friend_requests_page";
    }
    @PostMapping("/accept-friend-request")
    public String accept_friend_request(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestParam("senderId") Integer senderId)
    {
        Account receiver = accountService.findByUsername(userDetails.getUsername());
        Account sender = accountService.findById(senderId);
        friendService.acceptFriendRequest(sender, receiver);
        return "redirect:/friends";
    }

    @PostMapping("/reject-friend-request")
    public String reject_friend_request(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestParam("senderId") Integer senderId)
    {
        Account receiver = accountService.findByUsername(userDetails.getUsername());
        Account sender = accountService.findById(senderId);
        friendService.rejectFriendRequest(sender, receiver);
        return "redirect:/friends";
    }
    @PostMapping("/add-friend")
    public String add_friend( @AuthenticationPrincipal UserDetails userDetails,
                              @RequestParam("username") String username)
    {
        Account sender = accountService.findByUsername(userDetails.getUsername());
        Account receiver = accountService.findByUsername(username);
        friendService.sendFriendRequest(sender.getId(), receiver.getId());
//        sender.sendFriendRequest(receiver);
//        receiver.acceptFriendRequest(sender);
//        receiver.getSubscribers().add(sender);
//        Friendship friendship = Friendship.builder()
//                .sender(sender)
//                .createdAt(LocalDateTime.now())
//                .status(FriendshipStatus.PENDING)
//                .receiver(receiver)
//                .build();
        return "redirect:/friends";
    }
    @PostMapping("/send-friend-request")
    public ResponseEntity<String> sendFriendRequest(@RequestParam Integer senderId, @RequestParam Integer receiverId) {
        Account sender = accountService.findById(senderId);
        Account receiver = accountService.findById(receiverId);

        if (sender == null || receiver == null) {
            return ResponseEntity.badRequest().body("Sender or receiver not found.");
        }

      //  friendService.sendFriendRequest(sender, receiver);
        return ResponseEntity.ok().body("Friend request sent successfully.");
    }
}
