package com.example.daycarat.domain.episode.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record PatchEpisodeKeyword(
        @Schema(description = "에피소드 ID", example = "4") Long episodeId,
        @Schema(description = "키워드 이름", example = "커뮤니케이션") String keyword
) {
}
