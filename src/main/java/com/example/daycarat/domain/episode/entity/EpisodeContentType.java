package com.example.daycarat.domain.episode.entity;

import com.example.daycarat.global.error.exception.CustomException;

import static com.example.daycarat.global.error.exception.ErrorCode.INVALID_EPISODE_CONTENT_TYPE;

public enum EpisodeContentType {
    T(1, "더미1"), E(2, "더미2"), M(3, "더미3"), P(4, "더미4"),
    LessonsLearned(5, "배운 점"),
    AreasForImprovement(6, "아쉬운 점"),
    WriteFreely(7, "자유롭게 작성"),
    PointsToImprove(8, "보완할 점");


    private final int id;
    private final String value;

    EpisodeContentType(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public static EpisodeContentType fromId(int id) {
        for (EpisodeContentType episodeContentType : values()) {
            if (episodeContentType.getId() == id) {
                return episodeContentType;
            }
        }
        throw new CustomException(INVALID_EPISODE_CONTENT_TYPE);
    }

    public static EpisodeContentType fromValue(String value) {
        for (EpisodeContentType type : values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new CustomException(INVALID_EPISODE_CONTENT_TYPE);
    }
}
