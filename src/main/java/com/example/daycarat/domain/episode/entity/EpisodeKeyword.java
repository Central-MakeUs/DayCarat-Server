package com.example.daycarat.domain.episode.entity;

import com.example.daycarat.global.error.exception.CustomException;

import static com.example.daycarat.global.error.exception.ErrorCode.INVALID_KEYWORD;
import static com.example.daycarat.global.error.exception.ErrorCode.INVALID_KEYWORD_ID;

public enum EpisodeKeyword {
    COMMUNICATION(1, "커뮤니케이션"),
    PROBLEM_SOLVING(2, "문제 해결"),
    CREATIVITY(3, "창의성"),
    CHALLENGE_SPIRIT(4, "도전 정신"),
    PROFICIENCY(5, "전문성"),
    EXECUTION(6, "실행력"),
    UNSET(7, "미선택");

    private final int id;
    private final String value;

    EpisodeKeyword(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public static EpisodeKeyword fromId(int id) {
        for (EpisodeKeyword episodeKeyword : values()) {
            if (episodeKeyword.getId() == id) {
                return episodeKeyword;
            }
        }
        throw new CustomException(INVALID_KEYWORD_ID);
    }

    public static EpisodeKeyword fromValue(String keyword) {
        for (EpisodeKeyword episodeKeyword : values()) {
            if (episodeKeyword.getValue().equals(keyword)) {
                return episodeKeyword;
            }
        }
        throw new CustomException(INVALID_KEYWORD);
    }
}
