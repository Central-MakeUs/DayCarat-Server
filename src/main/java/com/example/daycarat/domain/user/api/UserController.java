package com.example.daycarat.domain.user.api;

import com.example.daycarat.domain.user.dto.GetUserInfo;
import com.example.daycarat.domain.user.dto.PatchUserInfo;
import com.example.daycarat.domain.user.service.UserService;
import com.example.daycarat.global.jwt.TokenResponse;
import com.example.daycarat.global.oauth.KakaoUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController @RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final KakaoUserService kakaoUserService;

    @Operation(summary = "카카오 액세스 토큰으로 내부 토큰 발급하기")
    @GetMapping("/oauth/kakao")
    public ResponseEntity<TokenResponse> kakaoLogin(@Parameter(name = "accessToken", description = "카카오 인증서버에서 받은 토큰", required = true)
                                        @RequestParam String accessToken) throws JsonProcessingException {
        return ResponseEntity.ok()
                .body(kakaoUserService.kakaoLogin(accessToken));
    }

    @Operation(summary = "유저 정보 조회하기")
    @GetMapping("/userInfo")
    public ResponseEntity<GetUserInfo> getUserInfo() {
        return ResponseEntity.ok()
                .body(userService.getUserInfo());
    }

    @Operation(summary = "유저 정보 수정하기", description = "보내지 않은 데이터에 대해서는 기존 값을 유지합니다.")
    @PatchMapping("/userInfo")
    public ResponseEntity<Boolean> patchUserInfo(@RequestBody PatchUserInfo patchUserInfo) {
        return ResponseEntity.ok()
                .body(userService.patchUserInfo(patchUserInfo));
    }

}
