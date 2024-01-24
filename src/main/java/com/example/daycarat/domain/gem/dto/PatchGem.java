package com.example.daycarat.domain.gem.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record PatchGem (
        @Schema(description = "보석 ID", example = "1") Long gemId
) {
}
