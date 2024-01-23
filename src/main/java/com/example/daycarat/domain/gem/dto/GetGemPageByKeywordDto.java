package com.example.daycarat.domain.gem.dto;

import com.example.daycarat.domain.episode.entity.EpisodeKeyword;

public record GetGemPageByKeywordDto(
        Long epiodeId,
        String title,
        EpisodeKeyword EpisodeKeyword,
        String date,
        String content) {
    public GetGemPageByKeyword toGetGemPageByKeyword() {
        return new GetGemPageByKeyword(
                this.epiodeId,
                this.title,
                this.EpisodeKeyword.getValue(),
                this.date,
                this.content
        );
    }
}
