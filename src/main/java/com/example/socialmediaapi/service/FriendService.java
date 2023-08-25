package com.example.socialmediaapi.service;

import com.example.socialmediaapi.entity.Account;
import com.example.socialmediaapi.repo.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * FriendService methods being used by FriendController
 */
@Service
public class FriendService {
    /**
     * The Account repository.
     */
    AccountRepository accountRepository;

    /**
     * Instantiates a new Friend service.
     *
     * @param accountRepository the account repository
     */
    @Autowired
    public FriendService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * sending the friend request and adding the sender to the receiver's subscribers list.
     * Then the sender is available to see the receiver's posts
     *
     * @param senderId   the sender id
     * @param receiverId the receiver id
     */
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

    /**
     * when the receiver accept friend request he will become the sender's subscriber
     *
     * @param sender   the sender
     * @param receiver the receiver
     */
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

    /**
     * when the receiver reject friend request, the sender still will be
     * a receiver's subscriber
     *
     * @param user   the user
     * @param sender the sender
     */
    public void rejectFriendRequest(Account user, Account sender) {
            user.getFriendRequestsReceived().remove(sender);
            sender.getFriendRequestsSent().remove(user);
            user.getFriendRequestsSent().remove(sender);
            accountRepository.save(user);
            accountRepository.save(sender);
    }

    /**
     * when the user remove somebody from friend list, the friend, in turn,
     * still will be the user's subscriber until he abandons the subscription
     *
     * @param user   the user
     * @param friend the friend
     */
    public void removeFromFriend(Account user, Account friend)
    {
        if(user.getFriends().contains(friend))
        {
            user.getFriends().remove(friend);
            friend.getFriends().remove(user);
            user.removeSubscription(friend);
            accountRepository.save(user);
            accountRepository.save(friend);
        }
    }
}
