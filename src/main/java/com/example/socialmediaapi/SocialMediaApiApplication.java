package com.example.socialmediaapi;

import com.example.socialmediaapi.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * The type Social media api application.
 */
@SpringBootApplication
public class SocialMediaApiApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(SocialMediaApiApplication.class, args);
    }

    /**
     * Run command line runner.
     *
     * @param accountService the account service
     * @return the command line runner
     */
    @Bean
    public CommandLineRunner run(AccountService accountService) {
        return runner->
        {
            System.out.println("base");

        };
    }
}
