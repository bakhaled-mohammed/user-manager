package com.devops.usermanager.common;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("Utilisateur introuvable : " + id);
    }
}
