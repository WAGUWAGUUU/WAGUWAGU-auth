package com.example.wgwg_auth.global.kafka.dto;

public record KafkaOwnerDto(
        Long ownerId,
        String ownerEmail,
        String ownerName,
        String ownerBusinessNumber
) {
}