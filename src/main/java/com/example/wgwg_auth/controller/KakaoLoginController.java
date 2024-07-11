package com.example.wgwg_auth.controller;

import com.example.wgwg_auth.service.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoLoginController {
    private final KakaoService kakaoService;

    @GetMapping("/callback")
    public Mono<ResponseEntity<?>> callback(@RequestParam("code") String code) {
        return kakaoService.getAccessTokenFromKakao(code)
                .flatMap(accessToken -> kakaoService.getUserInfo(accessToken)
                        .map(userInfo -> {
                            // 사용자 정보를 처리하고 세션에 저장하거나 데이터베이스에 저장하는 로직 추가
                            // 예를 들어, userInfo를 데이터베이스에 저장하고 필요한 추가 작업 수행
                            return ResponseEntity.ok(userInfo);
                        })
                );
    }
}
