package com.example.daycarat.domain.episode.repository;

import com.example.daycarat.domain.episode.dto.GetEpisodePage;
import com.example.daycarat.domain.episode.dto.GetEpisodeSummaryByActivity;
import com.example.daycarat.domain.episode.dto.GetEpisodeSummaryByDate;
import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.domain.user.domain.User;

import java.util.List;

public interface EpisodeRepositoryCustom {
    List<Episode> findTop3ByUserOrderBySelectedDateDesc(User user);

    List<GetEpisodeSummaryByDate> getEpisodeSummaryByDate(User user, Integer year);

    List<GetEpisodeSummaryByActivity> getEpisodeSummaryByActivity(User user);

    List<GetEpisodePage> getEpisodePageByDate(User user, Integer year, Integer month, Long cursorId, Integer pageSize);

    List<GetEpisodePage> getEpisodePageByActivity(User user, String activityTagName, Long cursorId, Integer pageSize);

}
