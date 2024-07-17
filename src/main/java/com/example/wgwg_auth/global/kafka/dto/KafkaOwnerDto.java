package com.example.wgwg_auth.global.kafka.dto;

public record KafkaOwnerDto(
        Long ownerId,
        String ownerName,
        String ownerBusinessNumber
) {
}