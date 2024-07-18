package com.example.wgwg_auth.domain.dto.request;

import com.example.wgwg_auth.domain.entity.Rider;
import com.example.wgwg_auth.domain.entity.RiderActivityArea;

public record RiderActivityRequest(
       Long riderId,
       String riderActivateArea
) {
    public RiderActivityArea toEntity(){
        return RiderActivityArea.builder()
                .riderId(riderId)
                .riderActivityArea(riderActivateArea)
                .build();
    }
}
