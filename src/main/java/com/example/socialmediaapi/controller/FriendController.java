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

/**
 * FriendController serves for ensure the relations between users
 */
@Controller
@AllArgsConstructor
public class FriendController {

    /**
     * The Account service.
     */
    AccountService accountService;
    /**
     * The Friend service.
     */
    FriendService friendService;

    /**
     * return the template with the list of all users for sending the friend request
     *
     * @param model       the model
     * @param userDetails the user details
     * @return string string
     */
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

    /**
     * display the friend's list
     *
     * @param model       the model
     * @param userDetails the user details
     * @return string string
     */
    @GetMapping("/friends")
    public String friends(Model model, @AuthenticationPrincipal UserDetails userDetails)
    {
        Account account = accountService.findByUsername(userDetails.getUsername());

        model.addAttribute("friends", account.getFriends());
        return "friends";
    }

    /**
     * display incoming friend request and add the sender as a subscriber to the receiver
     *
     * @param model       the model
     * @param userDetails the user details
     * @return string string
     */
    @GetMapping("/friend-request")
    public String showFriendRequests(Model model, @AuthenticationPrincipal UserDetails userDetails)
    {
        Account account= accountService.findByUsername(userDetails.getUsername());
        model.addAttribute("friendRequest", account.getFriendRequestsReceived());
        return "friend/friend_requests_page";
    }

    /**
     * show user's subscribers
     *
     * @param model       the model
     * @param userDetails the user details
     * @return string string
     */
    @GetMapping("/subscribes")
    public String subscribes(Model model, @AuthenticationPrincipal UserDetails userDetails)
    {
        Account account = accountService.findByUsername(userDetails.getUsername());
        model.addAttribute("subscribes", account.getSubscribers());
        return "friend/subscribes";
    }

    /**
     * show user's subscriptions
     *
     * @param model       the model
     * @param userDetails the user details
     * @return string string
     */
    @GetMapping("/subscriptions")
    public String subscriptions(Model model, @AuthenticationPrincipal UserDetails userDetails)
    {
        Account account = accountService.findByUsername(userDetails.getUsername());
        model.addAttribute("subscriptions", account.getSubscriptions());
        return "friend/subscriptions";
    }

    /**
     * delete the subscription
     *
     * @param subId       the sub id
     * @param userDetails the user details
     * @return string string
     */
    @PostMapping("/unsubscribe")
    public String unsubscribe(@RequestParam("subId") Integer subId,
                              @AuthenticationPrincipal UserDetails userDetails)
    {
        Account account = accountService.findByUsername(userDetails.getUsername());
        Account subscription = accountService.findById(subId);
        accountService.removeSubscription(account, subscription);
        return "friend/subscribes";
    }

    /**
     * accept the received friend request
     *
     * @param userDetails the user details
     * @param senderId    the sender id
     * @return string string
     */
    @PostMapping("/accept-friend-request")
    public String accept_friend_request(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestParam("senderId") Integer senderId)
    {
        Account receiver = accountService.findByUsername(userDetails.getUsername());
        Account sender = accountService.findById(senderId);
        friendService.acceptFriendRequest(sender, receiver);
        return "redirect:/friends";
    }

    /**
     * reject the received friend request
     *
     * @param userDetails the user details
     * @param senderId    the sender id
     * @return string string
     */
    @PostMapping("/reject-friend-request")
    public String reject_friend_request(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestParam("senderId") Integer senderId)
    {
        Account receiver = accountService.findByUsername(userDetails.getUsername());
        Account sender = accountService.findById(senderId);
        friendService.rejectFriendRequest(sender, receiver);
        return "redirect:/friends";
    }

    /**
     * add user to the friend list and the receiver become the sender's subscriber
     *
     * @param userDetails the user details
     * @param username    the username
     * @return string string
     */
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

    /**
     * delete the user from friend list and also abandon from subscription on friend
     *
     * @param userDetails the user details
     * @param friendId    the friend id
     * @return string string
     */
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
