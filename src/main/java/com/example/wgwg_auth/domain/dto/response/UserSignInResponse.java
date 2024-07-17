package com.example.wgwg_auth.domain.dto.response;

public record UserSignInResponse(
        String token,
        String tokenType
) {
    public static UserSignInResponse from(String token) {
        return new UserSignInResponse(token, "Bearer");
    }
}
