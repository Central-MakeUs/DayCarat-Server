package com.example.daycarat.domain.user.api;

import com.example.daycarat.domain.user.dto.GetUserInfo;
import com.example.daycarat.domain.user.dto.PatchUserInfo;
import com.example.daycarat.domain.user.service.UserService;
import com.example.daycarat.global.jwt.TokenResponse;
import com.example.daycarat.global.oauth.KakaoUserService;
import com.example.daycarat.global.response.SuccessResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController @RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final KakaoUserService kakaoUserService;

    @Operation(summary = "카카오 액세스 토큰으로 내부 토큰 발급하기",
            description = "최초 회원가입 시 statusCode = 201, 로그인 시 statusCode = 200")
    @GetMapping("/oauth/kakao")
    public SuccessResponse<TokenResponse> kakaoLogin(@Parameter(name = "accessToken", description = "카카오 인증서버에서 받은 토큰", required = true)
                                        @RequestParam String accessToken) throws JsonProcessingException {
        Pair<TokenResponse, Boolean> pair = kakaoUserService.kakaoLogin(accessToken);

        if (pair.getRight()) {
            return SuccessResponse.createSuccess(pair.getLeft());
        } else {
            return SuccessResponse.success(pair.getLeft());
        }

    }

    @Operation(summary = "유저 정보 조회하기")
    @GetMapping("/userInfo")
    public SuccessResponse<GetUserInfo> getUserInfo() {
        return SuccessResponse.success(userService.getUserInfo());
    }

    @Operation(summary = "유저 정보 수정하기", description = "보내지 않은 데이터에 대해서는 기존 값을 유지합니다.")
    @PatchMapping("/userInfo")
    public SuccessResponse<Boolean> patchUserInfo(@RequestBody PatchUserInfo patchUserInfo) {
        return SuccessResponse.updateSuccess(userService.patchUserInfo(patchUserInfo));
    }

    @Operation(summary = "유저 프로필 사진 등록/수정하기")
    @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SuccessResponse<Boolean> registerProfile(@RequestPart("multipartFile") MultipartFile profileImage) throws IOException {
        return SuccessResponse.updateSuccess(userService.registerProfile(profileImage));
    }

    @Operation(summary = "(개발용) 유저 삭제하기", description = "사용자와 사용자의 에피소드 관련 데이터를 모두 삭제합니다. 삭제 후 헤더의 토큰 값을 제거하세요.")
    @DeleteMapping("/delete")
    public SuccessResponse<Boolean> deleteUser() {
        return SuccessResponse.deleteSuccess(userService.deleteUser());
    }

}
