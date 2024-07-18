package com.example.wgwg_auth.global.kafka.dto;

import com.example.wgwg_auth.global.RiderTransportation;

import java.util.List;

public record KafkaRiderDto(
        Long riderId,
        String riderEmail,
        String riderNickname,
        String riderPhoneNumber,
        List<String> riderActivityArea,
        RiderTransportation riderTransportation,
        String riderAccount, // 계좌
        boolean riderIsDeleted
) {
}
