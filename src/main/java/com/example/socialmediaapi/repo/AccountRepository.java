package com.example.socialmediaapi.repo;

import com.example.socialmediaapi.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findByEmail(String email);
    Optional findByUsername(String username);

}
