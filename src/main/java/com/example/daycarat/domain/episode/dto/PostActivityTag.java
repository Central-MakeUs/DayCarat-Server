package com.example.daycarat.domain.episode.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record PostActivityTag(
        @Schema(description = "활동태그명", example = "영국 교환학생") String activityTagName) {
}
