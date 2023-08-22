package com.example.socialmediaapi.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(name = "friendship",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    public List<Account> friends = new ArrayList<>();

    @ManyToMany(mappedBy = "friends")
    private List<Account> subscribers = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "friend_requests",
            joinColumns = @JoinColumn(name = "sender_id"),
            inverseJoinColumns = @JoinColumn(name = "receiver_id"))
    private List<Account> friendRequestsSent = new ArrayList<>();

    @ManyToMany(mappedBy = "friendRequestsSent")
    private List<Account> friendRequestsReceived = new ArrayList<>();

    public void sendFriendRequest(Account user) {
        friendRequestsSent.add(user);
        user.getFriendRequestsReceived().add(this);
    }

    public void acceptFriendRequest(Account user) {
        if (friendRequestsReceived.contains(user)) {
            friendRequestsReceived.remove(user);
            user.getFriendRequestsSent().remove(this);
            friends.add(user);
            user.getFriends().add(this);
        }
    }

    public void rejectFriendRequest(Account user) {
        friendRequestsReceived.remove(user);
        user.getFriendRequestsSent().remove(this);
    }

    public void removeFriend(Account user) {
        friends.remove(user);
        user.getFriends().remove(this);
    }
    public void addSubscriber(Account account)
    {
        this.subscribers.add(account);
    }
    public void removeSubscriber(Account account)
    {
        this.subscribers.remove(account);
    }
//    @OneToMany(mappedBy = "sender",
//            cascade = CascadeType.ALL, fetch = FetchType.EAGER )
//    private List<Friendship> sentFriendships;

//    @OneToMany(mappedBy = "receiver",
//            cascade = {
//            CascadeType.DETACH,
//            CascadeType.MERGE,
//            CascadeType.PERSIST,
//            CascadeType.REFRESH}, fetch = FetchType.EAGER )
//    private List<Friendship> receivedFriendships;

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
//    public void addSentFriendships(Friendship friendship)
//    {
//        this.sentFriendships.add(friendship);
//    }
//    public void removeSentFriendships(Friendship friendship)
//    {
//        this.sentFriendships.remove(friendship);
//    }
//    public void addReceivedFriendships(Friendship friendship)
//    {
//        this.receivedFriendships.add(friendship);
//    }
//    public void removeReceivedFriendships(Friendship friendship)
//    {
//        this.receivedFriendships.remove(friendship);
//    }
}
