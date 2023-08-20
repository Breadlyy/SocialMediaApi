package com.example.socialmediaapi.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @OneToMany(mappedBy = "sender",
            cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<Friendship> sentFriendships;

    @OneToMany(mappedBy = "receiver",
            cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH}, fetch = FetchType.EAGER )
    private List<Friendship> receivedFriendships;

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
