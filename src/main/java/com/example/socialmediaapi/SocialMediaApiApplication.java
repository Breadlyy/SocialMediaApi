package com.example.socialmediaapi;

import com.example.socialmediaapi.entity.Account;
import com.example.socialmediaapi.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SocialMediaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialMediaApiApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(AccountService accountService) {
        return runner->
        {
            System.out.println("base");

        };
    }
}
