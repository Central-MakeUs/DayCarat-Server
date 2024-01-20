package com.example.daycarat.domain.gem.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record GetRecommedation(
        @Schema(description = "AI 추천 문장 1") String generatedContent1,
        @Schema(description = "AI 추천 문장 2") String generatedContent2,
        @Schema(description = "AI 추천 문장 3") String generatedContent3
) {
}
