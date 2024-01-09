package com.example.daycarat.domain.episode.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record GetEpisodeDetail(
        @Schema(description = "제목", example = "팀플 회의") String title,
        @Schema(description = "활동태그명", example = "CMC") String activityTagName,
        @Schema(description = "선택 날짜", example = "2024-01-20") String selectedDate,
        List<GetEpisodeContent> episodeContents
) {
    public static GetEpisodeDetail of(String title, String activityTagName, String selectedDate, List<GetEpisodeContent> episodeContents) {
        return new GetEpisodeDetail(title, activityTagName, selectedDate, episodeContents);
    }
}
