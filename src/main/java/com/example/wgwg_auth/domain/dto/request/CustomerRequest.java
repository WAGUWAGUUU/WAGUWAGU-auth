package com.example.wgwg_auth.domain.dto.request;

import com.example.wgwg_auth.domain.entity.Customer;

public record CustomerRequest(
       String customerNickname,
       String customerEmail,
       String customerAddress
) {
    public Customer toEntity(){
        return Customer.builder()
                .customerNickname(customerNickname)
                .customerEmail(customerEmail)
                .customerAddress(customerAddress)
                .build();
    }
}
