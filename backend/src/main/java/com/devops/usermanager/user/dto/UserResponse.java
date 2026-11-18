package com.devops.usermanager.user.dto;

import com.devops.usermanager.user.User;

/** Données renvoyées au frontend. */
public record UserResponse(Long id, String nom, String prenom, String email) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getNom(), user.getPrenom(), user.getEmail());
    }
}
