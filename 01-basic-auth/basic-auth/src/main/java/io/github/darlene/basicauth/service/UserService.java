package io.github.darlene.basicauth.service;

import io.github.darlene.basicauth.dto.RegisterRequest;
import io.github.darlene.basicauth.model.User;
import io.github.darlene.basicauth.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


/**
 * @RequiredArgsConstructor generates constructor dependecy injection, if we wanted field injection we would have used Autowired.
 * Here we are injectiong the repository and the password encoder
 */
@Service
@RequiredArgsConstructor

public class UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String register(RegisterRequest request){
        // Check if the user exists in the database
        userRepository.findByUsername(request.getUsername())
                .ifPresent(u -> { throw new RuntimeException("Username already taken!"); });

        // Build a new user.
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        // Save to db.. we will use the repository cause it connects to the db as well..we do save the user instance not the class.
        userRepository.save(user);

        return request.getUsername() + " registered successfully!";

    }

}