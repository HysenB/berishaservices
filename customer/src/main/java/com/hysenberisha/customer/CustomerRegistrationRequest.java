package com.hysenberisha.customer;

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email
) {
}
