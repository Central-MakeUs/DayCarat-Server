package com.example.daycarat.domain.gem.dto;

import com.example.daycarat.domain.episode.entity.EpisodeKeyword;

public record GetGemSummaryByKeywordDto(
        EpisodeKeyword episodeKeyword,
        Long count
) {
}
