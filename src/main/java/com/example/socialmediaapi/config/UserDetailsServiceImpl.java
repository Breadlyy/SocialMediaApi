package com.example.socialmediaapi.config;


import com.example.socialmediaapi.entity.Account;
import com.example.socialmediaapi.response.error.UserNotFoundException;
import com.example.socialmediaapi.repo.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    AccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> optionalUser = accountRepository.findByUsername(username);

        Account user = optionalUser.orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));

        return UserDetailsImpl.build(user);
    }
}
