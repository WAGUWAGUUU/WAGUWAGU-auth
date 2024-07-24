package com.example.wgwg_auth.domain.repository;

import com.example.wgwg_auth.domain.entity.Rider;
import com.example.wgwg_auth.global.RiderTransportation;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RiderRepository extends ReactiveCrudRepository<Rider,Long> {
    @Query("INSERT INTO RIDERS (RIDER_ID, RIDER_NICKNAME, RIDER_EMAIL) " +
            "VALUES (:riderId, :riderNickname, :riderEmail) " +
            "ON DUPLICATE KEY UPDATE RIDER_EMAIL = RIDER_EMAIL ")
    Mono<Void> insertIfNotExistAndReturn(Long riderId, String riderNickname, String riderEmail);

    @Query("SELECT * FROM RIDERS WHERE RIDER_EMAIL = :riderEmail")
    Flux<Rider> findByRiderEmail(String riderEmail);

    @Query("UPDATE RIDERS SET RIDER_ACCOUNT = :riderAccount, " +
            "RIDER_NICKNAME = :riderNickname,  RIDER_PHONE = :riderPhone," +
            "RIDER_ACTIVATE = :riderActivate, RIDER_IS_DELETED = :riderIsDeleted, " +
            "RIDER_TRANSPORTATION = :riderTransportation WHERE RIDER_ID = :riderId")
    Mono<Rider> updateRiderInfo(Long riderId, String riderNickname, String riderAccount,
                                String riderPhone, boolean riderActivate,
                                String riderTransportation, boolean riderIsDeleted);
}
