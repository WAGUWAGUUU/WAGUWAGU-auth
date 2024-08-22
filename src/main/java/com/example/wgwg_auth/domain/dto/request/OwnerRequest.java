package com.example.wgwg_auth.domain.dto.request;

import com.example.wgwg_auth.domain.entity.Customer;
import com.example.wgwg_auth.domain.entity.Owner;

public record OwnerRequest(
       String ownerName,
       String ownerBusinessNumber
) {
//    public Owner toEntity(){
//        return Owner.builder()
//                .ownerName(ownerName)
//                .ownerBusinessNumber(ownerBusinessNumber)
//                .build();
//    }
}
