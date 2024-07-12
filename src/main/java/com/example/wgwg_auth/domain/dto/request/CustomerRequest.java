package com.example.wgwg_auth.domain.dto.request;

import com.example.wgwg_auth.domain.entity.Customer;

public record CustomerRequest(
        Long customerId,
       String customerNickname,
       String customerEmail
) {
    public Customer toEntity(){
        return Customer.builder()
                .customerId(customerId)
                .customerNickname(customerNickname)
                .customerEmail(customerEmail)
                .build();
    }
}
