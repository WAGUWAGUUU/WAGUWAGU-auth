package com.example.wgwg_auth.service;

import com.example.wgwg_auth.domain.dto.request.RiderRequest;
import com.example.wgwg_auth.domain.dto.request.UserSignInRequest;
import com.example.wgwg_auth.domain.dto.response.UserSignInResponse;
import com.example.wgwg_auth.domain.entity.Rider;
import com.example.wgwg_auth.domain.repository.RiderRepository;
import com.example.wgwg_auth.global.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {
    private final RiderRepository riderRepository;
    private final JwtUtil jwtUtil;

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
                                .flatMap(rider -> {
                                    String token = jwtUtil.generateRiderToken(rider);
                                    UserSignInResponse response = UserSignInResponse.from(token);
                                    log.info(response.token());
                                    return Mono.just(response);
                                });
                    } else {
                        return riderRepository.insertIfNotExistAndReturn(
                                        request.userId(),
                                        request.UserNickname(),
                                        request.UserEmail()
                                ).then(riderRepository.save(request.toRiderEntity()))
                                .flatMap(rider -> {
                                    String token = jwtUtil.generateRiderToken(rider);
                                    UserSignInResponse response = UserSignInResponse.from(token);
                                    log.info(response.token());
                                    return Mono.just(response);
                                });
                    }
                })
                .onErrorResume(e -> {
                    log.error("Error occurred while saving owner info: " + e.getMessage());
                    return Mono.error(e);
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
                request.toEntity().getRiderActivityArea(), request.toEntity().getRiderActivate(),
                request.toEntity().getRiderTransportation()
                )
                .then(riderRepository.findById(riderId))
                .doOnSuccess(customer -> log.info("Rider address updated successfully for ownerId: " + riderId))
                .doOnError(e -> log.error("Error updating owner address for riderId: " + riderId, e));
    }
}
