package com.example.daycarat.domain.episode.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

public record GetEpisodeSummary (Integer month, Long quantity) {

    @Builder @QueryProjection
    public GetEpisodeSummary(Integer month, Long quantity) {
        this.month = month;
        this.quantity = quantity;
    }
}
