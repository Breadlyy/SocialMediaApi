package com.example.socialmediaapi.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * The post entity
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Lob
    @Column(length = 16777215)
    private byte[] image;

    @ManyToOne(cascade = CascadeType.ALL,
    fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;
}
