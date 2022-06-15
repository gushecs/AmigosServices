package com.amigos.customer;

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email) {
}
