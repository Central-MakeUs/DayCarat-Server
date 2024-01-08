package com.example.daycarat.domain.episode.repository;

import com.example.daycarat.domain.episode.dto.GetEpisodeSummary;
import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.domain.user.domain.User;

import java.util.List;

public interface EpisodeRepositoryCustom {
    List<Episode> findTop3ByUserOrderBySelectedDateDesc(User user);

    List<GetEpisodeSummary> getPageByDate(User user, Integer year);
}
