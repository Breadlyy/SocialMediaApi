package com.example.socialmediaapi.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * configuration class with SecurityFilterChain allow unauthorized users
 * have access only to registration and login pages.
 * Also set the user details
 */
@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * The User details service.
     */
    UserDetailsServiceImpl userDetailsService;

    private BCryptPasswordEncoder encoder()
    {
        return new BCryptPasswordEncoder();
    }

    /**
     * method configures and returns a DaoAuthenticationProvider instance,
     * setting its user details service and password encoder for authentication
     *
     * @return dao authentication provider
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider()
    {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(encoder());
        return auth;
    }

    /**
     * This method configures security rules for an HTTP request using Spring Security.
     * It allows unauthenticated access to "/registration", requires authentication for all other requests,
     * sets up a custom login page at "/login", and permits logout for all users.
     *
     * @param httpSecurity the http security
     * @return security filter chain
     * @throws Exception the exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests((request) -> request
                .requestMatchers("/registration").permitAll()
                .anyRequest().authenticated()).
                formLogin((form) -> form.loginPage("/login").permitAll())
                .logout((logout)->logout.permitAll());
        return httpSecurity.build();
    }

}
