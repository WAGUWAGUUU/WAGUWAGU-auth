package com.example.wgwg_auth.global.utils;

import com.example.wgwg_auth.domain.entity.Customer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private final Long expiration;
    private final SecretKey secretKey;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") Long expiration
    ) {
        this.expiration = expiration;
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Customer customer) {
        return Jwts.builder()
                .claim("id", customer.getCustomerId())
                .claim("email", customer.getCustomerEmail())
                .claim("nickname", customer.getCustomerNickname())
                .claim("address", customer.getCustomerAddress())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Customer getCustomerFromToken(String token){
        Claims payload = (Claims) Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();


        Long customerId = payload.get("id", Long.class);
        String customerEmail = payload.get("email", String.class);
        String customerNickname = payload.get("nickname", String.class);
        String customerAddress = payload.get("address", String.class);
        return new Customer(customerId, customerNickname, customerEmail, customerAddress);
    }
}