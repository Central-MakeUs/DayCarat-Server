package com.example.daycarat.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record PatchUserInfo(
        @Schema(description = "닉네임", example = "테스트") String nickname,
        @Schema(description = "프로필 사진", example = "https://d1j8r0kxyu9tj8.cloudfront.net/files/1617616479Z1X6X1X1/profile_pic.jpg") String picture,
        @Schema(description = "희망 직군 분야", example = "기획/전략") String jobTitle,
        @Schema(description = "나의 강점", example = "시간관리 능통") String strength
) {
}
