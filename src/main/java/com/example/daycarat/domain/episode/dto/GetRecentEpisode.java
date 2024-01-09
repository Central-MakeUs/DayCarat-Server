package com.example.daycarat.domain.episode.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record GetRecentEpisode(
        @Schema(description = "에피소드 ID", example = "6") Long id,
        @Schema(description = "에피소드 제목", example = "기획 회의") String title,
        @Schema(description = "시간", example = "5시간 전") String time) {

    public static GetRecentEpisode of(Long id, String title, String time) {
        return new GetRecentEpisode(id, title, time);
    }
}
