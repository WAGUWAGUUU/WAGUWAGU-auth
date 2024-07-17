package com.example.wgwg_auth.global.kafka.dto;

public record KafkaStatus<T>(
        T data, String status
) {
}
