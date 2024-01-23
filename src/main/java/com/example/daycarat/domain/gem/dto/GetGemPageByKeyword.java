package com.example.daycarat.domain.gem.dto;

public record GetGemPageByKeyword(
        Long epiodeId,
        String title,
        String EpisodeKeyword,
        String date,
        String content
) {
}
