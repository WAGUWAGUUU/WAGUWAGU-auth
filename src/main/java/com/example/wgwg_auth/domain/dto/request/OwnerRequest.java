package com.example.wgwg_auth.domain.dto.request;

import com.example.wgwg_auth.domain.entity.Customer;
import com.example.wgwg_auth.domain.entity.Owner;

public record OwnerRequest(
       String ownerName,
       String ownerAddress,
       double ownerLatitude,
       double ownerLongitude
) {
    public Owner toEntity(){
        return Owner.builder()
                .ownerName(ownerName)
                .ownerAddress(ownerAddress)
                .ownerLatitude(ownerLatitude)
                .ownerLongitude(ownerLongitude)
                .build();
    }
}
