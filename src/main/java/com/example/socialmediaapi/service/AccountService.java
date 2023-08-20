package com.example.socialmediaapi.service;

import com.example.socialmediaapi.entity.Account;
import com.example.socialmediaapi.entity.UserRole;
import com.example.socialmediaapi.response.error.UserAlreadyExist;
import com.example.socialmediaapi.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class    AccountService {

    private AccountRepository accountRepository;
    private BCryptPasswordEncoder encoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account save(Account account)
    {
        Account user = accountRepository.findByEmail(account.getEmail());
        account.setPassword(encoder().encode(account.getPassword()));
        account.setUserRole(UserRole.USER_ROLE);
        if(user != null) throw new UserAlreadyExist("User with email: " + account.getEmail() + " already exist" );
        return accountRepository.save(account);

    }

    public Account login(String username, String password)
    {
        Account user = (Account) accountRepository.findByUsername(username).get();
        //Account user = accountRepository.findByEmail(username);
        System.out.println(user);
        if (encoder().matches(password, user.getPassword())) {
            return user;
        } else
        {
            throw new RuntimeException("incorrect password");
        }
    }


    public Account findByEmail(String email)
    {
        Account result = accountRepository.findByEmail(email);
        if(result != null)
        {
            return result;
        }
        else
        {
            throw new RuntimeException("Did not find account email - " + email);
        }
    }

    public boolean userExists(String email)
    {
        Account user = accountRepository.findByEmail(email);
        if(user != null) return true;
        return false;
    }
    public List<Account> findAll()
    {
        return accountRepository.findAll();
    }
}
