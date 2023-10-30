package com.example.socialmediaapi.repo;

import com.example.socialmediaapi.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * jpa repository contains the basic methods interacting with db like save, find by id etc.
 */
public interface AccountRepository extends JpaRepository<Account, Integer> {

    /**
     * Find by email account.
     *
     * @param email the email
     * @return the account
     */
    Account findByEmail(String email);

    /**
     * Find by username optional.
     *
     * @param username the username
     * @return the optional
     */
    Optional findByUsername(String username);

}
