package com.example.wgwg_auth.domain.dto.request;

import com.example.wgwg_auth.domain.entity.Rider;
import com.example.wgwg_auth.global.RiderTransportation;

public record RiderRequest(
       String riderNickname,
       String riderAddress,
       String riderPhone,
       RiderTransportation riderTransportation,
       double riderLatitude,
       double riderLongitude,
       boolean riderActivate
) {
    public Rider toEntity(){
        return Rider.builder()
                .riderNickname(riderNickname)
                .riderActivate(riderActivate)
                .riderPhone(riderPhone)
                .riderTransportation(riderTransportation)
                .riderAddress(riderAddress)
                .riderLatitude(riderLatitude)
                .riderLongitude(riderLongitude)
                .build();
    }
}
