package com.example.wgwg_auth.service;

import com.example.wgwg_auth.domain.dto.response.KakaoUserInfoResponseDto;
import reactor.core.publisher.Mono;

public interface KakaoService {
    Mono<String> getAccessTokenFromKakao(String code);
    Mono<KakaoUserInfoResponseDto> getUserInfo(String accessToken);
}
