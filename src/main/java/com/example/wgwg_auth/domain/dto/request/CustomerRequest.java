package com.example.wgwg_auth.domain.dto.request;

import com.example.wgwg_auth.domain.entity.Customer;

public record CustomerRequest(
       String customerNickname,
       String customerAddress,
       double customerLatitude,
       double customerLongitude,
       String customerPhone
) {
//    public Customer toEntity(){
//        return Customer.builder()
//                .customerNickname(customerNickname)
//                .customerAddress(customerAddress)
//                .customerLatitude(customerLatitude)
//                .customerLongitude(customerLongitude)
//                .build();
//    }
}
