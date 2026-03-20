package io.github.darlene.sessionauth.service;

// Import the model/entity files
import io.github.darlene.sessionauth.entity.User;
import io.github.darlene.sessionauth.entity.UserRole;
import io.github.darlene.sessionauth.repository.UserRepository;

// Lombok imports @RequiredArgsConstructor for constructor dependency injection
import lombok.RequiredArgsConstructor;


// Import user details from springframework core security module
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service


@Service
@RequiredArgsConstructor;

public class CustomUserDetailsService implements UserDetailsService{

    // Since we are doing DP injection we need the fields
    private final UserRepository userRepository;

    @Override
    public UserDetails loadByUsername(String username)
        throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found: " + username));

        return org.springframework.security.core.UserDetails.User
                .withUsername(user.getUsername())
                .authorities(user.getUserRole().name())
                .password(user.getPassword())
                .build();

    }
}