package com.example.wgwg_auth.global.kafka;

import com.example.wgwg_auth.global.kafka.dto.KafkaOwnerDto;
import com.example.wgwg_auth.global.kafka.dto.KafkaStatus;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerProducer {
    private final KafkaTemplate<String, KafkaStatus<KafkaOwnerDto>> kafkaTemplate; // 타입 수정
    private final String topic = "owner-info-request-to-store-topic";

    @Bean
    public NewTopic newTopic() {
        return new NewTopic(topic, 1, (short) 1);
    }

    public void sendOwnerInfo(KafkaOwnerDto kafkaAccountDto, String status) {
        KafkaStatus<KafkaOwnerDto> kafkaStatus = new KafkaStatus<>(kafkaAccountDto, status);
        kafkaTemplate.send(topic, kafkaStatus);
    }
}
