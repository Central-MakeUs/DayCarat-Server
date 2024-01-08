package com.example.daycarat.domain.user.api;

import com.example.daycarat.global.jwt.TokenResponse;
import com.example.daycarat.domain.user.dto.UserDto;
import com.example.daycarat.domain.user.service.UserService;
import com.example.daycarat.global.oauth.KakaoUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController @RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final KakaoUserService kakaoUserService;

    @Operation(summary = "Authorization Code로 토큰 발급하기")
    @GetMapping("/oauth/kakao")
    public ResponseEntity<TokenResponse> kakaoLogin(@Parameter(name = "accessToken", description = "카카오 인증서버에서 받은 토큰", required = true)
                                        @RequestParam String accessToken) throws JsonProcessingException {
        return ResponseEntity.ok()
                .body(kakaoUserService.kakaoLogin(accessToken));
    }

    @Operation(summary = "유저 정보 조회하기")
    @GetMapping("/userInfo")
    public ResponseEntity<UserDto> getUserInfo() {
        return ResponseEntity.ok()
                .body(userService.getUserInfo());
    }

}
