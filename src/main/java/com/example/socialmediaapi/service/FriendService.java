package com.example.socialmediaapi.service;

import com.example.socialmediaapi.entity.Account;
import com.example.socialmediaapi.repo.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendService {
    AccountRepository accountRepository;
    @Autowired
    public FriendService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void sendFriendRequest(Integer senderId, Integer receiverId) {
        Account sender = accountRepository.findById(senderId).orElse(null);
        Account receiver = accountRepository.findById(receiverId).orElse(null);

        if (sender == receiver) {
            // Пользователь не может отправить заявку сам себе
            return;
        }
        if (sender != null && receiver != null) {
            sender.sendFriendRequest(receiver);
            accountRepository.save(sender);
        }
//        if (!receiver.getFriendRequestsReceived().contains(sender)) {
//            // Проверка, что заявка еще не отправлена
//            sender.addSubscriber(receiver);
//            receiver.getFriendRequestsReceived().add(sender);
//
//        }
    }

    public void acceptFriendRequest(Account user, Account friend) {
        if (user.getFriendRequestsReceived().contains(friend)) {
            user.getFriendRequestsReceived().remove(friend);
            friend.getFriendRequestsSent().remove(user);
            user.getFriends().add(friend);
            friend.getFriends().add(user);
            accountRepository.save(user);
            accountRepository.save(friend);
        }
    }

    public void rejectFriendRequest(Account user, Account sender) {
        if (user.getFriendRequestsReceived().contains(sender)) {
            user.getFriendRequestsReceived().remove(sender);
            sender.getFriendRequestsSent().remove(user);
            accountRepository.save(user);
            accountRepository.save(sender);
        }
    }
}
