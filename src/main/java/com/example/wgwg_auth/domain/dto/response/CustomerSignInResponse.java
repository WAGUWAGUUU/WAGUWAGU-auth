package com.example.wgwg_auth.domain.dto.response;

public record CustomerSignInResponse(
        String token,
        String tokenType
) {
    public static CustomerSignInResponse from(String token) {
        return new CustomerSignInResponse(token, "Bearer");
    }
}
