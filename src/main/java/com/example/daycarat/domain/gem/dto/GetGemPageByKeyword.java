package com.example.daycarat.domain.gem.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record GetGemPageByKeyword(
        @Schema(description = "에피소드 ID", example = "3") Long episodeId,
        @Schema(description = "에피소드 제목", example = "팀플 회의") String title,
        @Schema(description = "선택 날짜", example = "12/25") String date,
        @Schema(description = "내용", example = "배운 점 / 내용") String content) {
}
