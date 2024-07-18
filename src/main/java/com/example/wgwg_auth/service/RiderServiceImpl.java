package com.example.wgwg_auth.service;

import com.example.wgwg_auth.domain.dto.request.RiderActivityRequest;
import com.example.wgwg_auth.domain.dto.request.RiderRequest;
import com.example.wgwg_auth.domain.dto.request.UserSignInRequest;
import com.example.wgwg_auth.domain.dto.response.UserSignInResponse;
import com.example.wgwg_auth.domain.entity.Rider;
import com.example.wgwg_auth.domain.entity.RiderActivityArea;
import com.example.wgwg_auth.domain.repository.RiderActivityRepository;
import com.example.wgwg_auth.domain.repository.RiderRepository;
import com.example.wgwg_auth.global.kafka.RiderProducer;
import com.example.wgwg_auth.global.kafka.dto.KafkaRiderDto;
import com.example.wgwg_auth.global.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {
    private final RiderRepository riderRepository;
    private final RiderActivityRepository riderActivityRepository;
    private final JwtUtil jwtUtil;
    private final RiderProducer riderProducer;

    @Override
    public Mono<UserSignInResponse> saveRiderInfo(UserSignInRequest request) {
        return riderRepository.findByRiderEmail(request.toRiderEntity().getRiderEmail())
                .collectList()
                .flatMap(riders -> {
                    if (riders.size() > 1) {
                        log.error("Multiple customers found with the same email: " + request.toRiderEntity().getRiderEmail());
                        return Mono.error(new RuntimeException("Non unique result for email: " + request.toRiderEntity().getRiderEmail()));
                    } else if (riders.size() == 1) {
                        Rider existingOwner = riders.get(0);
                        log.info("Welcome back, " + existingOwner.getRiderNickname() + "!");
                        return getRiderInfo(existingOwner.getRiderId())
                                .flatMap(rider -> generateTokenWithActivityAreas(rider))
                                .map(token -> {
                                    UserSignInResponse response = UserSignInResponse.from(token);
                                    log.info(response.token());
                                    return response;
                                });
                    } else {
                        return riderRepository.insertIfNotExistAndReturn(
                                        request.userId(),
                                        request.UserNickname(),
                                        request.UserEmail()
                                ).then(riderRepository.save(request.toRiderEntity()))
                                .flatMap(rider -> generateTokenWithActivityAreas(rider))
                                .map(token -> {
                                    UserSignInResponse response = UserSignInResponse.from(token);
                                    log.info(response.token());
                                    return response;
                                });
                    }
                })
                .doOnSuccess(response -> sendRiderToKafka(request.toRiderEntity()))
                .onErrorResume(e -> {
                    log.error("Error occurred while saving owner info: " + e.getMessage());
                    return Mono.error(e);
                });
    }

    private Mono<String> generateTokenWithActivityAreas(Rider rider) {
        return riderActivityRepository.findAllByRiderId(rider.getRiderId())
                .collectList()
                .map(activityAreas -> jwtUtil.generateRiderToken(rider, activityAreas));
    }

    private void sendRiderToKafka(Rider rider) {
        riderActivityRepository.findAllByRiderId(rider.getRiderId())
                .collectList()
                .subscribe(activityAreas -> {
                    KafkaRiderDto dto = new KafkaRiderDto(
                            rider.getRiderId(),
                            rider.getRiderEmail(),
                            rider.getRiderNickname(),
                            rider.getRiderPhone(),
                            activityAreas.stream()
                                    .map(RiderActivityArea::getRiderActivityArea)
                                    .collect(Collectors.toList()),
                            rider.getRiderTransportation(),
                            "rider.getRiderAccount()",
                            false
                    );
                    riderProducer.sendRiderInfo(dto, "insert");
                });
    }

    @Override
    public Mono<Rider> getRiderInfo(Long riderId) {
        return riderRepository.findById(riderId);
    }

    @Override
    public Mono<Rider> updateRiderInfo(Long riderId, RiderRequest request) {
        return riderRepository.updateRiderInfo(
                        riderId, request.toEntity().getRiderNickname(),
                        request.toEntity().getRiderAddress(), request.toEntity().getRiderLatitude(),
                        request.toEntity().getRiderLongitude(), request.toEntity().getRiderPhone(),
                        request.toEntity().getRiderActivate(),
                        request.toEntity().getRiderTransportation().toString()
                )
                .then(riderRepository.findById(riderId))
                .doOnSuccess(response -> sendRiderToKafka(request.toEntity()))
                .doOnSuccess(customer -> log.info("Rider address updated successfully for ownerId: " + riderId))
                .doOnError(e -> log.error("Error updating owner address for riderId: " + riderId, e));
    }

    @Override
    public Mono<RiderActivityArea> insertRiderActivityArea(RiderActivityRequest req) {
        return riderActivityRepository.save(req.toEntity());
    }
}
