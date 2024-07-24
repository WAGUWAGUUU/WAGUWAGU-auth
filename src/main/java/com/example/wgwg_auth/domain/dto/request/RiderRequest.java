package com.example.wgwg_auth.domain.dto.request;

import com.example.wgwg_auth.domain.entity.Rider;
import com.example.wgwg_auth.global.RiderTransportation;

public record RiderRequest(
       String riderNickname,
       String riderPhone,
       RiderTransportation riderTransportation,
       String riderAccount,
       boolean riderIsDeleted,
       boolean riderActivate
) {
    public Rider toEntity(){
        return Rider.builder()
                .riderNickname(riderNickname)
                .riderActivate(riderActivate)
                .riderPhone(riderPhone)
                .riderTransportation(riderTransportation)
                .riderAccount(riderAccount)
                .riderIsDeleted(riderIsDeleted)
                .build();
    }
}
