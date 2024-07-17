package com.example.wgwg_auth.domain.dto.request;

import com.example.wgwg_auth.domain.entity.Rider;

public record RiderRequest(
       String riderNickname,
       String riderAddress,
       String riderActivateArea,
       String riderPhone,
       String riderTransportation,
       double riderLatitude,
       double riderLongitude,
       boolean riderActivate
) {
    public Rider toEntity(){
        return Rider.builder()
                .riderNickname(riderNickname)
                .riderActivate(riderActivate)
                .riderActivityArea(riderActivateArea)
                .riderPhone(riderPhone)
                .riderTransportation(riderTransportation)
                .riderAddress(riderAddress)
                .riderLatitude(riderLatitude)
                .riderLongitude(riderLongitude)
                .build();
    }
}
