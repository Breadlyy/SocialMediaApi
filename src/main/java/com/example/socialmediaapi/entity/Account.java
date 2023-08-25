package com.example.socialmediaapi.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User's entity. The crucial params are id, username and password
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    private UserRole userRole = UserRole.USER_ROLE;

    @OneToMany(mappedBy = "account",
            cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Post> posts;

    /**
     * The Friends.
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "friendship",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    public List<Account> friends = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "subscriptions",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"))
    private List<Account> subscriptions = new ArrayList<>();
    @ManyToMany
    @JoinTable(name = "subscribers",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"))
    private List<Account> subscribers = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "friend_requests",
            joinColumns = @JoinColumn(name = "sender_id"),
            inverseJoinColumns = @JoinColumn(name = "receiver_id"))
    private List<Account> friendRequestsSent = new ArrayList<>();

    @ManyToMany(mappedBy = "friendRequestsSent")
    private List<Account> friendRequestsReceived = new ArrayList<>();

    /**
     * Send friend request.
     *
     * @param user the user
     */
    public void sendFriendRequest(Account user) {
        friendRequestsSent.add(user);
        user.getFriendRequestsReceived().add(this);
    }

    /**
     * Accept friend request.
     *
     * @param user the user
     */
    public void acceptFriendRequest(Account user) {
        if (friendRequestsReceived.contains(user)) {
            friendRequestsReceived.remove(user);
            user.getFriendRequestsSent().remove(this);
            friends.add(user);
            user.getFriends().add(this);
        }
    }

    /**
     * Reject friend request.
     *
     * @param user the user
     */
    public void rejectFriendRequest(Account user) {
        friendRequestsReceived.remove(user);
        user.getFriendRequestsSent().remove(this);
    }

    /**
     * Remove friend.
     *
     * @param user the user
     */
    public void removeFriend(Account user) {
        friends.remove(user);
        user.getFriends().remove(this);
    }

    /**
     * Add subscriber.
     *
     * @param account the account
     */
    public void addSubscriber(Account account)
    {
        this.subscribers.add(account);
    }

    /**
     * Remove subscriber.
     *
     * @param account the account
     */
    public void removeSubscriber(Account account)
    {
        this.subscribers.remove(account);
    }

    /**
     * Add subscription.
     *
     * @param account the account
     */
    public void addSubscription(Account account)
    {
        this.subscriptions.add(account);
    }

    /**
     * Remove subscription.
     *
     * @param account the account
     */
    public void removeSubscription(Account account)
    {
        this.subscriptions.remove(account);
    }

    /**
     * Instantiates a new Account.
     *
     * @param username the username
     * @param email    the email
     * @param password the password
     */
    public Account(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
