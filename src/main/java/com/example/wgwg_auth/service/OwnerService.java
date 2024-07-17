package com.example.wgwg_auth.service;

import com.example.wgwg_auth.domain.dto.request.CustomerRequest;
import com.example.wgwg_auth.domain.dto.request.OwnerRequest;
import com.example.wgwg_auth.domain.dto.request.UserSignInRequest;
import com.example.wgwg_auth.domain.dto.response.UserSignInResponse;
import com.example.wgwg_auth.domain.entity.Customer;
import com.example.wgwg_auth.domain.entity.Owner;
import reactor.core.publisher.Mono;

public interface OwnerService {
    Mono<UserSignInResponse> saveOwnerInfo(UserSignInRequest request);
    Mono<Owner> getOwnerInfo(Long ownerId);
    Mono<Owner> updateOwnerInfo(Long ownerId, OwnerRequest req);
}
