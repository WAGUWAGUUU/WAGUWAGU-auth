package com.example.wgwg_auth.domain.repository;

import com.example.wgwg_auth.domain.entity.Owner;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OwnerRepository  extends ReactiveCrudRepository<Owner,Long> {
    @Query("INSERT INTO OWNERS (OWNER_ID, OWNER_NAME, OWNER_EMAIL) " +
            "VALUES (:ownerId, :ownerName, :ownerEmail) " +
            "ON DUPLICATE KEY UPDATE OWNER_EMAIL = OWNER_EMAIL ")
    Mono<Void> insertIfNotExistAndReturn(Long ownerId, String ownerName, String ownerEmail);

    @Query("SELECT * FROM OWNERS WHERE OWNER_EMAIL = :ownerEmail")
    Flux<Owner> findByOwnerEmail(String ownerEmail);

    @Query("UPDATE OWNERS SET OWNER_ADDRESS = :ownerAddress, " +
            "OWNER_NAME = :ownerName, OWNER_LATITUDE = :ownerLatitude," +
            "OWNER_LONGITUDE = :ownerLongitude, OWNER_BUSINESS_NUMBER = :ownerBusinessNumber" +
            " WHERE OWNER_ID = :ownerId")
    Mono<Owner> updateOwnerInfo(Long ownerId, String ownerName, String ownerAddress,
                                      double ownerLatitude, double ownerLongitude,
                                String ownerBusinessNumber);

}
