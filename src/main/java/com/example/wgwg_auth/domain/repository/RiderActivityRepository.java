package com.example.wgwg_auth.domain.repository;

import com.example.wgwg_auth.domain.entity.Rider;
import com.example.wgwg_auth.domain.entity.RiderActivityArea;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RiderActivityRepository extends ReactiveCrudRepository<RiderActivityArea,Long> {
    @Query("SELECT * FROM RIDER_ACTIVITY_AREAS WHERE RIDER_ID = :riderId")
    Flux<RiderActivityArea> findAllByRiderId(Long riderId);
}
