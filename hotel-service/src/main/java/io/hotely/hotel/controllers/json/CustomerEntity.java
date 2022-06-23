package io.hotely.hotel.controllers.json;

import java.util.UUID;

import io.hotely.hotel.entities.UserRole;
import io.hotely.hotel.entities.UserStatus;

public record CustomerEntity (
    UUID id, String email,
    String password, UserRole role,
    UserStatus userstatus
    ) {}
