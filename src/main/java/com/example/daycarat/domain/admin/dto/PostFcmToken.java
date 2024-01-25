package com.example.daycarat.domain.admin.dto;

public record PostFcmToken(
        Long userId,
        String fcmToken
) {
}
