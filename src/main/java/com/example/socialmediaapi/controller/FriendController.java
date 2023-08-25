package com.example.socialmediaapi.controller;

import com.example.socialmediaapi.entity.Account;
import com.example.socialmediaapi.service.AccountService;
import com.example.socialmediaapi.service.FriendService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class FriendController {

    AccountService accountService;
    FriendService friendService;
    @GetMapping("/users")
    public String users(Model model, @AuthenticationPrincipal UserDetails userDetails)
    {
        List<Account> allUsers = accountService.findAll();
        Account account = accountService.findByUsername(userDetails.getUsername());
        List<Account> users = allUsers.stream()
                .filter(user -> !account.getFriends().contains(user)) // Not friends
                .filter(user -> !user.getFriendRequestsReceived().contains(account)) // No incoming friend requests
                .filter(user -> !account.getFriendRequestsReceived().contains(user)) // No outgoing friend requests
                .filter(user -> !user.equals(account)) // Exclude the current user
                .collect(Collectors.toList());
        users.remove(account);
        model.addAttribute("account", account);
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
    @GetMapping("/subscribes")
    public String subscribes(Model model, @AuthenticationPrincipal UserDetails userDetails)
    {
        Account account = accountService.findByUsername(userDetails.getUsername());
        model.addAttribute("subscribes", account.getSubscribers());
        return "friend/subscribes";
    }
    @GetMapping("/subscriptions")
    public String subscriptions(Model model, @AuthenticationPrincipal UserDetails userDetails)
    {
        Account account = accountService.findByUsername(userDetails.getUsername());
        model.addAttribute("subscriptions", account.getSubscriptions());
        return "friend/subscriptions";
    }
    @PostMapping("/unsubscribe")
    public String unsubscribe(@RequestParam("subId") Integer subId,
                              @AuthenticationPrincipal UserDetails userDetails)
    {
        Account account = accountService.findByUsername(userDetails.getUsername());
        Account subscription = accountService.findById(subId);
        accountService.removeSubscription(account, subscription);
        return "friend/subscribes";
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
    @PostMapping("/delete-friend")
    public String delete_friend(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam("friendId") Integer friendId)
    {
        Account user = accountService.findByUsername(userDetails.getUsername());
        Account friend = accountService.findById(friendId);
        friendService.removeFromFriend(user, friend);
        return "redirect:/friends";
    }

}
