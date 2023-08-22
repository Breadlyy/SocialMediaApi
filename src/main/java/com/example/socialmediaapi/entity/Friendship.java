package com.example.socialmediaapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id")
    private Account sender;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_id")
    private Account receiver;


    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime declinedAt;
}
