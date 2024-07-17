package com.example.wgwg_auth.global.kafka;

import com.example.wgwg_auth.global.kafka.dto.KafkaCustomerDto;
import com.example.wgwg_auth.global.kafka.dto.KafkaStatus;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerProducer {
    private final KafkaTemplate<String, KafkaStatus<KafkaCustomerDto>> kafkaTemplate; // 타입 수정
    private final String topic = "customer-info-request-to-store-topic";

    @Bean
    public NewTopic newTopic() {
        return new NewTopic(topic, 1, (short) 1);
    }

    public void sendCustomerInfo(KafkaCustomerDto kafkaAccountDto, String status) {
        KafkaStatus<KafkaCustomerDto> kafkaStatus = new KafkaStatus<>(kafkaAccountDto, status);
        kafkaTemplate.send(topic, kafkaStatus);
    }
}
