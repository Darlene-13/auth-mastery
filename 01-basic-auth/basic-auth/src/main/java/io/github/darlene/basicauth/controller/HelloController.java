package io.github.darlene.basicauth.controller;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


import lombok.extern.slf4j.Slf4j;

import java.security.Principal;



@RestController
@Slf4j
@Validated
@RequestMapping("/api")
public class  HelloController{

    @GetMapping("/hello")
    public ResponseEntity<String> hello(Principal principal){
        log.info("User {} accessed /hello", principal.getName());
        return ResponseEntity.ok("Hello, " + principal.getName() + "!");
    }

    @GetMapping
    public ResponseEntity<String> publicEndpoint(){
        log.info("Public endpoint accessed");
        return ResponseEntity.ok("This is public!");
    }
}