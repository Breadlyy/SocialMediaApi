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
        if(!sender.getSubscriptions().contains(receiver)) sender.addSubscription(receiver);
        if(!receiver.getSubscribers().contains(sender))receiver.addSubscriber(sender);
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

    public void acceptFriendRequest(Account sender, Account receiver) {
        if (receiver.getFriendRequestsReceived().contains(sender)) {
            receiver.getFriendRequestsReceived().remove(sender);
            sender.getFriendRequestsSent().remove(receiver);
            receiver.getFriends().add(sender);
            sender.getFriends().add(receiver);
            sender.addSubscriber(receiver);
            receiver.addSubscription(sender);
            accountRepository.save(sender);
            accountRepository.save(receiver);
        }
    }

    public void rejectFriendRequest(Account user, Account sender) {
            user.getFriendRequestsReceived().remove(sender);
            sender.getFriendRequestsSent().remove(user);
            user.getFriendRequestsSent().remove(sender);
            accountRepository.save(user);
            accountRepository.save(sender);
    }
    public void removeFromFriend(Account user, Account friend)
    {
        if(user.getFriends().contains(friend))
        {
            user.getFriends().remove(friend);
            friend.getFriends().remove(user);
            accountRepository.save(user);
            accountRepository.save(friend);
        }
    }
}
