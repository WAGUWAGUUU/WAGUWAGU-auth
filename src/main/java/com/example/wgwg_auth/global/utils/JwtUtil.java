package com.example.wgwg_auth.global.utils;

import com.example.wgwg_auth.domain.entity.Customer;
import com.example.wgwg_auth.domain.entity.Owner;
import com.example.wgwg_auth.domain.entity.Rider;
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

    public String generateCustomerToken(Customer customer) {
        return Jwts.builder()
                .claim("id", customer.getCustomerId())
                .claim("email", customer.getCustomerEmail())
                .claim("nickname", customer.getCustomerNickname())
                .claim("address", customer.getCustomerAddress())
                .claim("latitude", customer.getCustomerLatitude())
                .claim("longitude", customer.getCustomerLongitude())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    public String generateOwnerToken(Owner owner) {
        return Jwts.builder()
                .claim("id", owner.getOwnerId())
                .claim("email", owner.getOwnerEmail())
                .claim("name", owner.getOwnerName())
                .claim("address", owner.getOwnerAddress())
                .claim("latitude", owner.getOwnerLatitude())
                .claim("longitude", owner.getOwnerLongitude())
                .claim("ownerBusinessNumber",owner.getOwnerBusinessNumber())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    public String generateRiderToken(Rider rider) {
        return Jwts.builder()
                .claim("id", rider.getRiderId())
                .claim("email", rider.getRiderEmail())
                .claim("nickname", rider.getRiderNickname())
                .claim("address", rider.getRiderAddress())
                .claim("latitude", rider.getRiderLatitude())
                .claim("longitude", rider.getRiderLongitude())
                .claim("phone", rider.getRiderPhone())
                .claim("activity_area", rider.getRiderActivityArea())
                .claim("activate", rider.getRiderActivate())
                .claim("transportation", rider.getRiderTransportation())
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
        double latitude = payload.get("latitude", Double.class);
        double longitude = payload.get("longitude", Double.class);
        return new Customer(customerId, customerNickname, customerEmail, customerAddress, latitude, longitude);
    }

    public Owner getOwnerFromToken(String token){
        Claims payload = (Claims) Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long ownerId = payload.get("id", Long.class);
        String ownerEmail = payload.get("email", String.class);
        String ownerName = payload.get("name", String.class);
        String ownerAddress = payload.get("address", String.class);
        double latitude = payload.get("latitude", Double.class);
        double longitude = payload.get("longitude", Double.class);
        String ownerBusinessNumber = payload.get("ownerBusinessNumber", String.class);
        return new Owner(ownerId, ownerName, ownerEmail, ownerAddress, latitude, longitude, ownerBusinessNumber);
    }

    public Rider getRiderFromToken(String token){
        Claims payload = (Claims) Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long riderId = payload.get("id", Long.class);
        String riderEmail = payload.get("email", String.class);
        String riderNickname = payload.get("nickname", String.class);
        String riderAddress = payload.get("address", String.class);
        double latitude = payload.get("latitude", Double.class);
        double longitude = payload.get("longitude", Double.class);
        String riderPhone = payload.get("phone", String.class);
        String riderActivityArea = payload.get("activity_area", String.class);
        Boolean riderActivate = payload.get("activate", Boolean.class);
        String riderTransportation = payload.get("transportation", String.class);
        return new Rider(riderId, riderNickname, riderEmail, riderPhone, riderActivityArea,
                riderActivate, riderTransportation,riderAddress, latitude, longitude);
    }
}