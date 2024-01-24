package com.example.daycarat.domain.gem.repository;

import com.example.daycarat.domain.activity.entity.ActivityTag;
import com.example.daycarat.domain.episode.entity.EpisodeKeyword;
import com.example.daycarat.domain.gem.dto.GetEpisodeClipboardDto;
import com.example.daycarat.domain.gem.dto.GetGemCount;
import com.example.daycarat.domain.gem.dto.GetGemSummaryByKeywordDto;
import com.example.daycarat.domain.gem.entity.Gem;

import java.util.List;
import java.util.Optional;

public interface GemRepositoryCustom {
    GetGemCount getGemCount(Long userId);

    GetGemCount getGemCountByMonth(Long userId);

    List<GetGemSummaryByKeywordDto> getGemSummaryByKeyword(Long userId);

    EpisodeKeyword getMostGemKeyword(Long userId);

    ActivityTag getMostGemActivity(Long userId);

    Optional<GetEpisodeClipboardDto> getEpisodeClipboard(Long userId, Long episodeId);

    Optional<Gem> findByEpisodeIdAndUserId(Long episodeId, Long id);
}
