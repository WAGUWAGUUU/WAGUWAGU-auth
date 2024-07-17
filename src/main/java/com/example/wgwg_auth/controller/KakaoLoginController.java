package com.example.wgwg_auth.controller;

import com.example.wgwg_auth.domain.dto.request.UserSignInRequest;
import com.example.wgwg_auth.domain.dto.response.UserSignInResponse;
import com.example.wgwg_auth.service.CustomerService;
import com.example.wgwg_auth.service.KakaoService;
import com.example.wgwg_auth.service.OwnerService;
import com.example.wgwg_auth.service.RiderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoLoginController {
    private final KakaoService kakaoService;
    private final CustomerService customerService;
    private final OwnerService ownerService;
    private final RiderService riderService;

    @GetMapping("customers/callback")
    public Mono<ResponseEntity<?>> customerCallback(@RequestParam("code") String code) {
        return kakaoService.getAccessTokenFromKakao(code)
                .flatMap(accessToken -> kakaoService.getUserInfo(accessToken)
                        .flatMap(userInfo -> {
                            UserSignInRequest request = new UserSignInRequest(
                                    userInfo.getId(),
                                    userInfo.getKakaoAccount().getProfile().getNickName(),
                                    userInfo.getKakaoAccount().getEmail());
                            return customerService.saveCustomerInfo(request)
                                    .map(UserSignInResponse::token)
                                    .map(token -> ResponseEntity.ok(token));
                        })
                );
    }

    @GetMapping("owners/callback")
    public Mono<ResponseEntity<?>> ownerCallback(@RequestParam("code") String code) {
        return kakaoService.getAccessTokenFromKakao(code)
                .flatMap(accessToken -> kakaoService.getUserInfo(accessToken)
                        .flatMap(userInfo -> {
                            UserSignInRequest request = new UserSignInRequest(
                                    userInfo.getId(),
                                    userInfo.getKakaoAccount().getProfile().getNickName(),
                                    userInfo.getKakaoAccount().getEmail());
                            return ownerService.saveOwnerInfo(request)
                                    .map(UserSignInResponse::token)
                                    .map(token -> ResponseEntity.ok(token));
                        })
                );
    }

    @GetMapping("riders/callback")
    public Mono<ResponseEntity<?>> riderCallback(@RequestParam("code") String code) {
        return kakaoService.getAccessTokenFromKakao(code)
                .flatMap(accessToken -> kakaoService.getUserInfo(accessToken)
                        .flatMap(userInfo -> {
                            UserSignInRequest request = new UserSignInRequest(
                                    userInfo.getId(),
                                    userInfo.getKakaoAccount().getProfile().getNickName(),
                                    userInfo.getKakaoAccount().getEmail());
                            return riderService.saveRiderInfo(request)
                                    .map(UserSignInResponse::token)
                                    .map(token -> ResponseEntity.ok(token));
                        })
                );
    }
}
