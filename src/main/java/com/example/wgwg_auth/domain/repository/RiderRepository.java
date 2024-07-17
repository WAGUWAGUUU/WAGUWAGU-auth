package com.example.wgwg_auth.domain.repository;

import com.example.wgwg_auth.domain.entity.Rider;
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

    @Query("UPDATE RIDERS SET RIDER_ADDRESS = :riderAddress, " +
            "RIDER_NICKNAME = :riderNickname, RIDER_LATITUDE = :riderLatitude," +
            "RIDER_LONGITUDE = :riderLongitude, RIDER_PHONE = :riderPhone," +
            "RIDER_ACTIVITY_AREA = :riderActivityArea, RIDER_ACTIVATE = :riderActivate, " +
            "RIDER_TRANSPORTATION = :riderTransportation WHERE RIDER_ID = :riderId")
    Mono<Rider> updateRiderInfo(Long riderId, String riderNickname, String riderAddress,
                                      double riderLatitude, double riderLongitude,
                                   String riderPhone, String riderActivityArea,
                                   boolean riderActivate, String riderTransportation);

}
