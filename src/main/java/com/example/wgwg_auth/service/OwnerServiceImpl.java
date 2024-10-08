package com.example.wgwg_auth.service;

import com.example.wgwg_auth.domain.dto.request.OwnerRequest;
import com.example.wgwg_auth.domain.dto.request.UserSignInRequest;
import com.example.wgwg_auth.domain.dto.response.UserSignInResponse;
import com.example.wgwg_auth.domain.entity.Owner;
import com.example.wgwg_auth.domain.repository.OwnerRepository;
import com.example.wgwg_auth.global.kafka.OwnerProducer;
import com.example.wgwg_auth.global.kafka.dto.KafkaOwnerDto;
import com.example.wgwg_auth.global.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {
    private final OwnerRepository ownerRepository;
    private final JwtUtil jwtUtil;
    private final OwnerProducer ownerProducer;

    @Override
    public Mono<UserSignInResponse> saveOwnerInfo(UserSignInRequest request) {
        return ownerRepository.findByOwnerEmail(request.toOwnerEntity().getOwnerEmail())
                .collectList()
                .flatMap(owners -> {
                    if (owners.size() > 1) {
                        log.error("Multiple customers found with the same email: " + request.toOwnerEntity().getOwnerEmail());
                        return Mono.error(new RuntimeException("Non unique result for email: " + request.toOwnerEntity().getOwnerEmail()));
                    } else if (owners.size() == 1) {
                        Owner existingOwner = owners.get(0);
                        log.info("Welcome back, " + existingOwner.getOwnerName() + "!");
                        return getOwnerInfo(existingOwner.getOwnerId())
                                .flatMap(owner -> {
                                    String token = jwtUtil.generateOwnerToken(owner);
                                    UserSignInResponse response = UserSignInResponse.from(token);
                                    log.info(response.token());
                                    return Mono.just(response);
                                });
                    } else {
                        return ownerRepository.insertIfNotExistAndReturn(
                                        request.userId(),
                                        request.UserNickname(),
                                        request.UserEmail()
                                ).then(ownerRepository.save(request.toOwnerEntity()))
                                .flatMap(owner -> {
                                    String token = jwtUtil.generateOwnerToken(owner);
                                    UserSignInResponse response = UserSignInResponse.from(token);
                                    log.info(response.token());
                                    // Kafka 메시지 전송
                                    KafkaOwnerDto dto = new KafkaOwnerDto(owner.getOwnerId(), owner.getOwnerEmail(),
                                            owner.getOwnerName(),owner.getOwnerBusinessNumber());
                                    ownerProducer.sendOwnerInfo(dto, "owner_info_to_store");

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
    public Mono<Owner> getOwnerInfo(Long ownerId) {
        return ownerRepository.findById(ownerId);
    }

    @Override
    public Mono<Owner> updateOwnerInfo(Long ownerId, OwnerRequest request) {
        return ownerRepository.updateOwnerInfo(
                ownerId, request.ownerName(),
                        request.ownerBusinessNumber())
                .then(ownerRepository.findById(ownerId))
                .doOnSuccess(owner -> {
                    log.info("Owner address updated successfully for ownerId: " + ownerId);
                    // Kafka 메시지 전송
                    KafkaOwnerDto dto = new KafkaOwnerDto(owner.getOwnerId(),
                            owner.getOwnerEmail(),
                            request.ownerName(),
                            request.ownerBusinessNumber());
                    ownerProducer.sendOwnerInfo(dto, "owner_info_to_store");
                })
                .doOnError(e -> log.error("Error updating owner address for ownerId: " + ownerId, e));
    }
}
