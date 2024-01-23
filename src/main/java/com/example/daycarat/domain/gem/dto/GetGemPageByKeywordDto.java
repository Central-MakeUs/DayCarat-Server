package com.example.daycarat.domain.gem.dto;

import com.example.daycarat.domain.episode.entity.EpisodeContentType;
import com.example.daycarat.global.util.StringParser;

public record GetGemPageByKeywordDto (
        Long episodeId,
        String title,
        String date,
        EpisodeContentType contentType,
        String content) {
    public GetGemPageByKeyword convert() {
        return new GetGemPageByKeyword(
                episodeId,
                title,
                date,
                contentType.getValue() + " / " + StringParser.getSubString(content)
        );
    }
}
