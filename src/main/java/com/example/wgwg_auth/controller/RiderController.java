package com.example.wgwg_auth.controller;

import com.example.wgwg_auth.domain.dto.request.CustomerRequest;
import com.example.wgwg_auth.domain.dto.request.RiderActivityRequest;
import com.example.wgwg_auth.domain.dto.request.RiderRequest;
import com.example.wgwg_auth.domain.dto.response.RiderResponse;
import com.example.wgwg_auth.domain.dto.response.RiderWithActivityAreas;
import com.example.wgwg_auth.domain.entity.Customer;
import com.example.wgwg_auth.domain.entity.Rider;
import com.example.wgwg_auth.domain.entity.RiderActivityArea;
import com.example.wgwg_auth.global.utils.JwtUtil;
import com.example.wgwg_auth.service.CustomerService;
import com.example.wgwg_auth.service.RiderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth/riders")
public class RiderController {
    private final RiderService riderService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public Mono<RiderWithActivityAreas> getRiderInfo(@RequestHeader("Authorization") String token) {
        String bearerToken = token.substring(7);
        Long riderId = jwtUtil.getRiderFromToken(bearerToken).getRider().getRiderId();
//        return riderService.getRiderInfo(riderId);
        return riderService.getRiderWithActivityAreas(riderId);
    }

    @PutMapping
    public Mono<Rider> updateRider
            (@RequestHeader("Authorization") String token,
             @RequestBody RiderRequest riderRequest) {
        String bearerToken = token.substring(7);
        Long riderId = jwtUtil.getRiderFromToken(bearerToken).getRider().getRiderId();
        return riderService.updateRiderInfo(riderId, riderRequest);
    }

    @PostMapping
    public Mono<RiderActivityArea> saveActivityArea(@RequestHeader("Authorization") String token,
                                                    @RequestParam String activityArea) {
        String bearerToken = token.substring(7);
        Long riderId = jwtUtil.getRiderFromToken(bearerToken).getRider().getRiderId();
        RiderActivityRequest req = new RiderActivityRequest(riderId, activityArea);
        return riderService.insertRiderActivityArea(req);
    }

    @DeleteMapping
    public Flux<RiderActivityArea> deleteActivityArea(@RequestHeader("Authorization") String token) {
        String bearerToken = token.substring(7);
        Long riderId = jwtUtil.getRiderFromToken(bearerToken).getRider().getRiderId();
        return riderService.deleteRiderActivityArea(riderId);
    }
}
