package com.example.wgwg_auth.global.kafka.dto;

public record KafkaCustomerDto(
        Long customerId,
        double longitude,
        double latitude
)
{
}
