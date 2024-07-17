package com.example.wgwg_auth.domain.dto.request;

import com.example.wgwg_auth.domain.entity.Customer;
import com.example.wgwg_auth.domain.entity.Owner;
import com.example.wgwg_auth.domain.entity.Rider;

public record UserSignInRequest(
        Long userId,
       String UserNickname,
       String UserEmail
) {
    public Customer toCustomerEntity(){
        return Customer.builder()
                .customerId(userId)
                .customerNickname(UserNickname)
                .customerEmail(UserEmail)
                .build();
    }
    public Owner toOwnerEntity(){
        return Owner.builder()
                .ownerId(userId)
                .ownerName(UserNickname)
                .ownerEmail(UserEmail)
                .build();
    }
    public Rider toRiderEntity(){
        return Rider.builder()
                .riderId(userId)
                .riderNickname(UserNickname)
                .riderEmail(UserEmail)
                .build();
    }
}
