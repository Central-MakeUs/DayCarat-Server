package com.example.daycarat.domain.episode.dto;

import com.example.daycarat.domain.episode.entity.EpisodeContentType;
import com.example.daycarat.domain.episode.entity.EpisodeKeyword;
import com.example.daycarat.domain.episode.entity.EpisodeState;
import com.example.daycarat.global.util.StringParser;

public record GetEpisodePageDto(
        Long id,
        String title,
        String date,
        EpisodeState episodeState,
        EpisodeKeyword episodeKeyword,
        EpisodeContentType episodeContentType,
        String content
) {
    public static GetEpisodePage convert(GetEpisodePageDto getEpisodePageDto) {
        return new GetEpisodePage(
                getEpisodePageDto.id(),
                getEpisodePageDto.title(),
                getEpisodePageDto.date(),
                getEpisodePageDto.episodeState(),
                getEpisodePageDto.episodeKeyword() == null ? null : getEpisodePageDto.episodeKeyword().getValue(),
                getEpisodePageDto.episodeContentType.getValue() + " / " + StringParser.getSubString(getEpisodePageDto.content())
        );
    }
}
