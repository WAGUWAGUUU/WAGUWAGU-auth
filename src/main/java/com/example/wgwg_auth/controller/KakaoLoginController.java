package com.example.wgwg_auth.controller;

import com.example.wgwg_auth.domain.dto.request.CustomerSignInRequest;
import com.example.wgwg_auth.domain.dto.response.CustomerSignInResponse;
import com.example.wgwg_auth.service.CustomerService;
import com.example.wgwg_auth.service.KakaoService;
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

    @GetMapping("/callback")
    public Mono<ResponseEntity<?>> callback(@RequestParam("code") String code) {
        return kakaoService.getAccessTokenFromKakao(code)
                .flatMap(accessToken -> kakaoService.getUserInfo(accessToken)
                        .flatMap(userInfo -> {
                            CustomerSignInRequest request = new CustomerSignInRequest(
                                    userInfo.getId(),
                                    userInfo.getKakaoAccount().getProfile().getNickName(),
                                    userInfo.getKakaoAccount().getEmail());
                            return customerService.saveCustomerInfo(request)
                                    .map(CustomerSignInResponse::token)
                                    .map(token -> ResponseEntity.ok(token));
                        })
                );
    }
}
