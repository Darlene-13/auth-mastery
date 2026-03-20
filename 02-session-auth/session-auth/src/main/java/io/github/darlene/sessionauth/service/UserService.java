package io.github.darlene.sessionauth.service;

// Import own classes..
import io.github.darlene.sessionauth.dto.LoginRequest;
import io.github.darlene.sessionauth.dto.RegisterRequest;

import io.github.darlene.sessionauth.entity.User;
import io.github.darlene.sessionauth.entity.UserRole;
import io.github.darlene.sessionauth.repository.UserRepository;

// Authentication manager to verify credentias
import org.springframework.security.authentication.AuthenticationManager;

// Username Password authentication token wraps username + password for authenticationmanager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

// SecurityContextholder to store the auhtenticatied user for the current request
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// Bcrypt password encoding
import org.springframework.security.crypto.password.PasswordEncoder;

// Lonbok
import lombok.RequiredArgsConstructor;

// Spring stereotype
import org.springframework.stereotype.Service;

// Needed for the login method that allows us to create the http session.
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor

public class UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    // Method to register the user
    public String register(RegisterRequest request){

        // Check if the user exists in the database
        userRepository.findByUsername(request.getUsername()).ifPresent(u -> {throw new RuntimeException("Username already taken");
        }
        );

        // Build and save the user
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .userRole(UserRole.ROLE_USER)
                .build();

        // Save it to db
        userRepository.save(user);

        return request.getUsername() + " registered successfully!";
    }

    // Method to login
    public String login(LoginRequest request, HttpServletRequest httpRequest){
        // Verify the credentials
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )

        );

        // Store the authencited user in security context
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Create a http session and store the security context in it.
        // Spring session interceps this and persist it to the db automatically.
        HttpSession session = httpRequest.getSession(true);

        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        // Return sucess
        return request.getUsername() + " logged in successfully.";

    }
}