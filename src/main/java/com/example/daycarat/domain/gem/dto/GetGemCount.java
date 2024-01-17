package com.example.daycarat.domain.gem.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record GetGemCount(
        @Schema(description = "보석 개수", example = "5") Long gemCount
) {
    public static GetGemCount of(Long gemCount) {
        return new GetGemCount(gemCount);
    }
}
