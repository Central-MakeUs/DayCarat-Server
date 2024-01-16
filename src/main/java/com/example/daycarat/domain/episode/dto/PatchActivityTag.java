package com.example.daycarat.domain.episode.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record PatchActivityTag(
        @Schema(description = "활동 태그 ID", example = "3") Long activityTagId,
        @Schema(description = "활동 태그명", example = "CMC") String activityTagName
) {
}
