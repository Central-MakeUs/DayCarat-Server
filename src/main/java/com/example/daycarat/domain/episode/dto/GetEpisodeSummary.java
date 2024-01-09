package com.example.daycarat.domain.episode.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record GetEpisodeSummary (
        @Schema(description = "월", example = "3") Integer month,
        @Schema(description = "개수", example = "10") Long quantity) {

    @Builder @QueryProjection
    public GetEpisodeSummary(Integer month, Long quantity) {
        this.month = month;
        this.quantity = quantity;
    }
}
