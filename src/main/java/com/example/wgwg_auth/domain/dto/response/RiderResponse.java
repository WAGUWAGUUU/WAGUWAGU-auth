package com.example.wgwg_auth.domain.dto.response;

import com.example.wgwg_auth.domain.entity.Rider;
import com.example.wgwg_auth.domain.entity.RiderActivityArea;
import com.example.wgwg_auth.global.RiderTransportation;

import java.util.List;

public record RiderResponse(
        Long riderId,
        String riderEmail,
        String riderNickname,
        String riderPhone,
        Boolean riderActivate,
        List<RiderActivityArea> riderActivityAreas, // 수정된 부분
        RiderTransportation riderTransportation,
        String riderAccount,
        boolean riderIsDeleted
) {
    public static RiderResponse from(Rider rider, List<RiderActivityArea> areas) {
        return new RiderResponse(
                rider.getRiderId(),
                rider.getRiderEmail(),
                rider.getRiderNickname(),
                rider.getRiderPhone(),
                rider.getRiderActivate(),
                areas.stream().toList(),
                rider.getRiderTransportation(),
                rider.getRiderAccount(),
                rider.getRiderIsDeleted()
        );
    }
}