package io.github.darlene.basicauth.controller;

import io.github.darlene.basicauth.service.UserService;
import io.github.darlene.basicauth.dto.RegisterRequest;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

/**
 * In this class we inject the service class.
 */

@RestController
@Slf4j
@Validated
@RequestMapping("/api")
@RequiredArgsConstructor
public class  HelloController{

    private final UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request){
        String message = userService.register(request);
        return ResponseEntity.ok(message);
    }
    @GetMapping("/hello")
    public ResponseEntity<String> hello(Principal principal){
        log.info("User {} accessed /hello", principal.getName());
        return ResponseEntity.ok("Hello, " + principal.getName() + "!");
    }

    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint(){
        log.info("Public endpoint accessed");
        return ResponseEntity.ok("This is public!");
    }

}