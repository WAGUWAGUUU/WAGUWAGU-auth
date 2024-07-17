package com.example.wgwg_auth.service;

import com.example.wgwg_auth.domain.dto.request.OwnerRequest;
import com.example.wgwg_auth.domain.dto.request.RiderRequest;
import com.example.wgwg_auth.domain.dto.request.UserSignInRequest;
import com.example.wgwg_auth.domain.dto.response.UserSignInResponse;
import com.example.wgwg_auth.domain.entity.Rider;
import reactor.core.publisher.Mono;

public interface RiderService {
    Mono<UserSignInResponse> saveRiderInfo(UserSignInRequest request);
    Mono<Rider> getRiderInfo(Long riderId);
    Mono<Rider> updateRiderInfo(Long riderId, RiderRequest req);
}
