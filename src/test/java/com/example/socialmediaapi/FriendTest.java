package com.example.socialmediaapi;

import com.example.socialmediaapi.entity.Account;
import com.example.socialmediaapi.entity.UserRole;
import com.example.socialmediaapi.repo.AccountRepository;
import com.example.socialmediaapi.service.FriendService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FriendTest {
    @InjectMocks
    FriendService friendService;

    @Mock
    AccountRepository accountRepository;

    @Test
    public void isFriendRequestSent() {
        Account sender = Account.builder().
                id(1)
                .username("Jugger")
                .email("jugger@face.com")
                .password("123")
                .userRole(UserRole.USER_ROLE)
                .subscriptions(new ArrayList<>())
                .friendRequestsSent(new ArrayList<>())
                .build();
        Account receiver = Account.builder().
                id(2)
                .username("Kitty")
                .email("kat@kit.com")
                .password("321")
                .userRole(UserRole.USER_ROLE)
                .subscribers(new ArrayList<>())
                .friendRequestsReceived(new ArrayList<>())
                .build();
        when(accountRepository.findById(1)).thenReturn(Optional.of(sender));
        when(accountRepository.findById(2)).thenReturn(Optional.of(receiver));
        friendService.sendFriendRequest(1, 2);

        Assertions.assertEquals(sender.getFriendRequestsSent().get(0).getEmail(), "kat@kit.com");
    }

    @Test
    public void isFriendRequestAccepted() {
        Account sender = Account.builder().
                id(1)
                .username("Jugger")
                .email("jugger@face.com")
                .password("123")
                .userRole(UserRole.USER_ROLE)
                .subscriptions(new ArrayList<>())
                .friends(new ArrayList<>())
                .friendRequestsSent(new ArrayList<>())
                .build();
        Account receiver = Account.builder().
                id(2)
                .username("Kitty")
                .email("kat@kit.com")
                .password("321")
                .userRole(UserRole.USER_ROLE)
                .subscribers(new ArrayList<>())
                .friends(new ArrayList<>())
                .friendRequestsReceived(new ArrayList<>())
                .build();

        when(accountRepository.findById(1)).thenReturn(Optional.of(sender));
        when(accountRepository.findById(2)).thenReturn(Optional.of(receiver));
        friendService.sendFriendRequest(1, 2);
        friendService.acceptFriendRequest(sender, receiver);

        Assertions.assertEquals(sender.getFriends().get(0), receiver);
        Assertions.assertEquals(receiver.getFriends().get(0), sender);
    }
}
