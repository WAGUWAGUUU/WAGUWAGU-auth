package com.example.wgwg_auth.controller;

import com.example.wgwg_auth.domain.dto.request.OwnerRequest;
import com.example.wgwg_auth.domain.entity.Owner;
import com.example.wgwg_auth.global.utils.JwtUtil;
import com.example.wgwg_auth.service.OwnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth/owners")
public class OwnerController {
    private final OwnerService ownerService;
    private final JwtUtil jwtUtil;


    @GetMapping
    public Mono<Owner> getOwnerInfo(@RequestHeader("Authorization") String token) {
        String bearerToken = token.substring(7);
        Long ownerId = jwtUtil.getOwnerFromToken(bearerToken).getOwnerId();
        return ownerService.getOwnerInfo(ownerId);
    }

    @PutMapping
    public Mono<Owner> updateOwner
            (@RequestHeader("Authorization") String token,
             @RequestBody OwnerRequest ownerRequest) {
        String bearerToken = token.substring(7);
        Long ownerId = jwtUtil.getOwnerFromToken(bearerToken).getOwnerId();
        return ownerService.updateOwnerInfo(ownerId, ownerRequest);
    }
}
