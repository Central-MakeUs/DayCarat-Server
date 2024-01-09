package com.example.daycarat.global.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Common
    AUTHENTICATION_REQUIRED(401, "C001", "인증이 필요합니다."),
    ACCESS_DENIED(403, "C002", "권한이 없는 사용자입니다."),
    INTERNAL_SERVER_ERROR(500, "C004", "서버 에러입니다."),
    MEMBER_NOT_FOUND(404, "C005", "존재하지 않는 사용자입니다."),
    INVALID_REQUEST_ERROR(400, "C001", "잘못된 요청입니다."),

    // Episode
    EPISODE_NOT_FOUND(404, "C006", "존재하지 않는 에피소드입니다."),
    EPISODE_USER_NOT_MATCHED(403, "C007", "에피소드의 소유자가 아닙니다."),
    ACTIVITY_TAG_NOT_FOUND(404, "C006", "존재하지 않는 활동 태그입니다.");


    private final int status;
    private final String code;
    private final String message;

}
