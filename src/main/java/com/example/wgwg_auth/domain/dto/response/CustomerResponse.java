package com.example.wgwg_auth.domain.dto.response;

import com.example.wgwg_auth.domain.entity.Customer;

public record CustomerResponse(
        Long customerId,
        String customerNickname,
        String customerEmail,
        String customerAddress,
        double customerLatitude,
        double customerLongitude,
        String token
) {
    public static CustomerResponse from(Customer customer, String token) {
        return new CustomerResponse(
                customer.getCustomerId(),
                customer.getCustomerNickname(),
                customer.getCustomerEmail(),
                customer.getCustomerAddress(),
                customer.getCustomerLatitude(),
                customer.getCustomerLongitude(),
                token
        );
    }
}
