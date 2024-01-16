package com.example.daycarat.domain.episode.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record PatchEpisode (
        @Schema(description = "에피소드 ID", example = "1") Long episodeId,
        @Schema(description = "에피소드 제목", example = "기획 회의") String title,
        @Schema(description = "선택 날짜", example = "2024-01-09") String selectedDate,
        @Schema(description = "활동 태그", example = "CMC") String activityTag,
        List<PatchEpisodeContent> episodeContents){
}
