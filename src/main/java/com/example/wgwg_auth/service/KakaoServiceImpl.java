package com.example.wgwg_auth.service;

import com.example.wgwg_auth.domain.dto.response.KakaoTokenResponseDto;
import com.example.wgwg_auth.domain.dto.response.KakaoUserInfoResponseDto;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoServiceImpl implements KakaoService {

    @Value("${kakao.client_id}")
    private String clientId;
    @Value("${kakao.client_secret}")
    private String clientSecret;
    @Value("${kakao.KAUTH_TOKEN_URL_HOST}")
    private String KAUTH_TOKEN_URL_HOST;
    @Value("${kakao.KAUTH_USER_URL_HOST}")
    private String KAUTH_USER_URL_HOST;

    @Override
    public Mono<String> getAccessTokenFromKakao(String code) {
        String url = KAUTH_TOKEN_URL_HOST + "/oauth/token";

        return WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
                        .queryParam("client_secret", clientSecret)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.error("카카오에서 액세스 토큰을 요청할 때 4xx 오류 발생: {}", clientResponse.statusCode());
                    return Mono.error(new RuntimeException("Invalid Parameter"));
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    log.error("카카오에서 액세스 토큰을 요청할 때 5xx 오류 발생: {}", clientResponse.statusCode());
                    return Mono.error(new RuntimeException("Internal Server Error"));
                })
                .bodyToMono(KakaoTokenResponseDto.class)
                .doOnNext(kakaoTokenResponseDto -> {
                    log.info(" [Kakao Service] Access Token ------> {}", kakaoTokenResponseDto.getAccessToken());
                    log.info(" [Kakao Service] Refresh Token ------> {}", kakaoTokenResponseDto.getRefreshToken());
                    log.info(" [Kakao Service] Id Token ------> {}", kakaoTokenResponseDto.getIdToken());
                    log.info(" [Kakao Service] Scope ------> {}", kakaoTokenResponseDto.getScope());
                })
                .map(KakaoTokenResponseDto::getAccessToken);
    }

    @Override
    public Mono<KakaoUserInfoResponseDto> getUserInfo(String accessToken) {
        return WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.error("카카오에서 사용자 정보를 요청할 때 4xx 오류 발생: {}", clientResponse.statusCode());
                    return Mono.error(new RuntimeException("Invalid Parameter"));
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    log.error("카카오에서 사용자 정보를 요청할 때 5xx 오류 발생: {}", clientResponse.statusCode());
                    return Mono.error(new RuntimeException("Internal Server Error"));
                })
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .doOnNext(userInfo -> {
                    log.info("[ Kakao Service ] Auth ID ---> {} ", userInfo.getId());
                    log.info("[ Kakao Service ] NickName ---> {} ", userInfo.getKakaoAccount().getProfile().getNickName());
                    log.info("[ Kakao Service ] ProfileImageUrl ---> {} ", userInfo.getKakaoAccount().getProfile().getProfileImageUrl());
                });
    }
}
