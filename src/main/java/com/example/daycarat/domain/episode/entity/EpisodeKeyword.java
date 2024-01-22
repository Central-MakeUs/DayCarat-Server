package com.example.daycarat.domain.episode.entity;

import com.example.daycarat.global.error.exception.CustomException;

import static com.example.daycarat.global.error.exception.ErrorCode.INVALID_KEYWORD;
import static com.example.daycarat.global.error.exception.ErrorCode.INVALID_KEYWORD_ID;

public enum EpisodeKeyword {
    COMMUNICATION(1, "커뮤니케이션"),
    CONFLICT_RESOLUTION(2, "갈등 해결"),
    PASSION(3, "열정"),
    DILIGENCE(4, "성실"),
    COLLABORATION(5, "협업 능력"),
    LEADERSHIP(6, "리더십"),
    FEEDBACK(7, "피드백"),
    UNSET(8, "미선택");

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
