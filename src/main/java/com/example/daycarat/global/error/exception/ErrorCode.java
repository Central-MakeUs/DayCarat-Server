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
    USER_NOT_FOUND(404, "C005", "존재하지 않는 사용자입니다."),
    INVALID_REQUEST_ERROR(400, "C001", "잘못된 요청입니다."),

    // Apple OAuth
    APPLE_LOGIN_FAILED(400, "C001", "인증 코드가 올바르지 않거나 만료되었습니다."),

    // Episode
    EPISODE_NOT_FOUND(404, "C006", "존재하지 않는 에피소드입니다."),
    EPISODE_USER_NOT_MATCHED(403, "C007", "에피소드의 소유자가 아닙니다."),
    ACTIVITY_USER_NOT_MATCHED(403, "C007", "활동 태그의 소유자가 아닙니다."),
    ACTIVITY_TAG_NOT_FOUND(404, "C006", "존재하지 않는 활동 태그입니다."),
    ACTIVITY_TAG_CANNOT_DELETE(403, "C007", "에피소드에 연결된 활동 태그는 삭제할 수 없습니다."),

    // EpisodeContent
    EPISODE_CONTENT_NOT_FOUND(404, "C006", "존재하지 않는 에피소드 컨텐츠입니다."),
    INVALID_EPISODE_CONTENT_TYPE(400, "C001", "잘못된 에피소드 컨텐츠 타입입니다."),

    // EpisodeKeyword
    INVALID_KEYWORD_ID(400, "C001", "잘못된 키워드 ID입니다."),
    INVALID_KEYWORD(400, "C001", "잘못된 키워드입니다."),


    // Gem
    AI_RECOMMENDATION_NOT_FOUND(201, "C006", "AI 추천이 생성중입니다."),
    AI_RECOMMENDATION_FAILED(202, "C004", "AI 추천에 실패했습니다."),
    JSON_FILE_UPLOAD_FAILED(500, "C004", "JSON 파일 업로드에 실패했습니다."),
    JSON_FILE_READ_FAILED(500, "C004", "JSON 파일 읽기에 실패했습니다."),
    GEM_NOT_FOUND(404, "C006", "존재하지 않는 보석입니다."),
    GEM_USER_NOT_MATCHED(403, "C007", "보석의 소유자가 아닙니다."),
    GEM_ALREADY_EXISTS(409, "C008", "이미 보석이 존재합니다."),
    GEM_CONTENTS_NOT_FILLED(400, "C001", "보석의 내용이 모두 채워지지 않았습니다."),

    // GeneratedContent
    GENERATED_CONTENT_NOT_FOUND(404, "C006", "존재하지 않는 AI 생성 컨텐츠입니다."),

    //FCM
    USER_FCM_TOKEN_NOT_FOUND(404, "C006", "존재하지 않는 FCM 토큰입니다.");


    private final int status;
    private final String code;
    private final String message;

}
