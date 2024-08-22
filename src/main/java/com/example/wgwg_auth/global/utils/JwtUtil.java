package com.example.wgwg_auth.global.utils;

import com.example.wgwg_auth.domain.dto.response.RiderWithActivityAreas;
import com.example.wgwg_auth.domain.entity.Customer;
import com.example.wgwg_auth.domain.entity.Owner;
import com.example.wgwg_auth.domain.entity.Rider;
import com.example.wgwg_auth.domain.entity.RiderActivityArea;
import com.example.wgwg_auth.global.RiderTransportation;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
                .claim("phone", customer.getCustomerPhone())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    public String generateOwnerToken(Owner owner) {
        return Jwts.builder()
                .claim("id", owner.getOwnerId())
                .claim("email", owner.getOwnerEmail())
                .claim("name", owner.getOwnerName())
                .claim("ownerBusinessNumber",owner.getOwnerBusinessNumber())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    public String generateRiderToken(Rider rider,
                                     List<RiderActivityArea> activityAreas) {
        return Jwts.builder()
                .claim("id", rider.getRiderId())
                .claim("email", rider.getRiderEmail())
                .claim("nickname", rider.getRiderNickname())
                .claim("phone", rider.getRiderPhone())
                .claim("activityAreas", activityAreas.stream()
                        .map(RiderActivityArea::getRiderActivityArea)
                        .collect(Collectors.toList()))
                .claim("activate", rider.getRiderActivate())
                .claim("transportation", rider.getRiderTransportation())
                .claim("account", rider.getRiderAccount())
                .claim("isDeleted", rider.getRiderIsDeleted())
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
        String phone = payload.get("phone", String.class);
        return new Customer(customerId, customerNickname, customerEmail,
                customerAddress, latitude, longitude, phone);
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
        String ownerBusinessNumber = payload.get("ownerBusinessNumber", String.class);
        return new Owner(ownerId, ownerName, ownerEmail, ownerBusinessNumber);
    }

    public RiderWithActivityAreas getRiderFromToken(String token) {
        Claims payload = (Claims) Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long riderId = payload.get("id", Long.class);
        String riderEmail = payload.get("email", String.class);
        String riderNickname = payload.get("nickname", String.class);
        String riderPhone = payload.get("phone", String.class);
        Boolean riderActivate = payload.get("activate", Boolean.class);
        String riderTransportationString = payload.get("transportation", String.class);
        String riderAccount = payload.get("account", String.class);
        Boolean riderIsDeleted = (payload.get("isDeleted", Boolean.class));

        RiderTransportation riderTransportation = RiderTransportation.valueOf(riderTransportationString);

        Rider rider = new Rider(riderId, riderNickname, riderEmail, riderPhone,
                riderActivate, riderTransportation, riderAccount, riderIsDeleted);

        List<String> activityAreasStr = payload.get("activityAreas", List.class);
        List<RiderActivityArea> activityAreas = activityAreasStr.stream()
                .map(area -> new RiderActivityArea(null, riderId, area))
                .collect(Collectors.toList());

        return new RiderWithActivityAreas(rider, activityAreas);
    }
}