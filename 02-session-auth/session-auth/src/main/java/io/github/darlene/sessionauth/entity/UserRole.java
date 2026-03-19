package io.github.darlene.sessionauth.entity;

import lombok.Getter;

@Getter
public enum UserRole{

    ROLE_ADMIN("admin"),
    ROLE_USER("normal");

    private String description;

    UserRole(String description){
        this.description = description;
    }
}