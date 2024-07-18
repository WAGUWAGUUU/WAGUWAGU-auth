package com.example.wgwg_auth.service;

import com.example.wgwg_auth.domain.dto.request.UserSignInRequest;
import com.example.wgwg_auth.domain.dto.response.UserSignInResponse;
import com.example.wgwg_auth.domain.entity.Rider;
import com.example.wgwg_auth.domain.repository.RiderRepository;
import com.example.wgwg_auth.global.kafka.RiderProducer;
import com.example.wgwg_auth.global.kafka.dto.KafkaRiderDto;
import com.example.wgwg_auth.global.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.Mockito.*;

class RiderServiceImplTest {

    @Mock
    private RiderRepository riderRepository;

    @Mock
    private RiderProducer riderProducer;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private RiderServiceImpl riderService;

    private Rider[] ridersResponses;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        // Initialize mock responses
        ridersResponses = new Rider[]{
                Rider.builder()
                        .riderId(1L)
                        .riderNickname("qnwk963")
                        .riderEmail("qnwk963@naver.com")
                        .build()
        };
    }

    @Test
    void saveRiderInfo() {
        // Mock repository response
        when(riderRepository.findByRiderEmail("qnwk963@naver.com"))
                .thenReturn(Flux.fromArray(ridersResponses));

        // Mock jwtUtil
        when(jwtUtil.generateRiderToken(any(Rider.class), anyList()))
                .thenReturn("mocked-token");

        // Mock riderRepository.insertIfNotExistAndReturn
        when(riderRepository.insertIfNotExistAndReturn(anyLong(), anyString(), anyString()))
                .thenReturn(Mono.empty());

        // Mock riderRepository.save
        when(riderRepository.save(any(Rider.class)))
                .thenReturn(Mono.just(ridersResponses[0]));

        // Mock riderProducer
        doNothing().when(riderProducer).sendRiderInfo(any(KafkaRiderDto.class), anyString());

        // Create request object
        UserSignInRequest request = new UserSignInRequest(1L, "qnwk963", "qnwk963@naver.com");

        // Call the method under test
        Mono<UserSignInResponse> result = riderService.saveRiderInfo(request);

        // Verify the result using StepVerifier
        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.token().equals("mocked-token"))
                .verifyComplete();
    }
}
