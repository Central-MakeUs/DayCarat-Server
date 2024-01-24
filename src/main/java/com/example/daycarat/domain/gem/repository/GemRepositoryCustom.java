package com.example.daycarat.domain.gem.repository;

import com.example.daycarat.domain.activity.entity.ActivityTag;
import com.example.daycarat.domain.episode.entity.EpisodeKeyword;
import com.example.daycarat.domain.gem.dto.GetEpisodeClipboardDto;
import com.example.daycarat.domain.gem.dto.GetGemCount;
import com.example.daycarat.domain.gem.dto.GetGemSummaryByKeywordDto;

import java.util.List;

public interface GemRepositoryCustom {
    GetGemCount getGemCount(Long userId);

    GetGemCount getGemCountByMonth(Long userId);

    List<GetGemSummaryByKeywordDto> getGemSummaryByKeyword(Long userId);

    EpisodeKeyword getMostGemKeyword(Long userId);

    ActivityTag getMostGemActivity(Long userId);

    GetEpisodeClipboardDto getEpisodeClipboard(Long episodeId);
}
