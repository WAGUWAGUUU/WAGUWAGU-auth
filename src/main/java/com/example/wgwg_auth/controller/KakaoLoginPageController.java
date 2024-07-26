package com.example.wgwg_auth.controller;

import com.example.wgwg_auth.domain.dto.request.UserSignInRequest;
import com.example.wgwg_auth.domain.dto.response.UserSignInResponse;
import com.example.wgwg_auth.service.KakaoService;
import com.example.wgwg_auth.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class KakaoLoginPageController {

    @Value("${kakao.client_id}")
    private String client_id;
    @Value("${kakao.customer_redirect_uri}")
    private String customer_redirect_uri;
    @Value("${kakao.owner_redirect_uri}")
    private String owner_redirect_uri;
    @Value("${kakao.rider_redirect_uri}")
    private String rider_redirect_uri;

    private final KakaoService kakaoAuthService;
    private final OwnerService ownerService;

    @GetMapping("customers/page")
    public Mono<ResponseEntity<String>> customerLoginPage(Model model) {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + client_id + "&redirect_uri=" + customer_redirect_uri;
        model.addAttribute("location", location);
        return Mono.just(ResponseEntity.ok("고객 로그인"));
    }

    @GetMapping("owners/page")
    public Mono<ResponseEntity<String>> ownerLoginPage(Model model) {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + client_id + "&redirect_uri=" + owner_redirect_uri;
        model.addAttribute("location", location);
        return Mono.just(ResponseEntity.ok("점주 로그인"));
    }

    @GetMapping("riders/page")
    public Mono<ResponseEntity<String>> riderLoginPage(Model model) {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + client_id + "&redirect_uri=" + rider_redirect_uri;
        model.addAttribute("location", location);
        return Mono.just(ResponseEntity.ok("기사 로그인"));
    }

    @GetMapping("callback")
    public Mono<ResponseEntity<String>> handleCallback(@RequestParam String code) {
        return kakaoAuthService.getAccessTokenFromKakao(code)
                .flatMap(accessToken -> kakaoAuthService.getUserInfo(accessToken)
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
}
