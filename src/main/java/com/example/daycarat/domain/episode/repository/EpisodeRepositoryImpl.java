package com.example.daycarat.domain.episode.repository;

import com.example.daycarat.domain.episode.dto.GetEpisodeByDate;
import com.example.daycarat.domain.episode.dto.GetEpisodeSummaryByActivity;
import com.example.daycarat.domain.episode.dto.GetEpisodeSummaryByDate;
import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.domain.user.domain.User;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.daycarat.domain.episode.entity.QEpisode.episode;
import static com.example.daycarat.domain.episode.entity.QEpisodeContent.episodeContent;

@RequiredArgsConstructor
public class EpisodeRepositoryImpl implements EpisodeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Episode> findTop3ByUserOrderBySelectedDateDesc(User user) {
        return jpaQueryFactory
                .selectFrom(episode)
                .where(episode.user.eq(user))
                .orderBy(episode.createdDate.desc())
                .limit(3)
                .fetch();
    }

    @Override
    public List<GetEpisodeSummaryByDate> getEpisodeSummaryByDate(User user, Integer year) {
        return jpaQueryFactory
                .select(Projections.constructor(GetEpisodeSummaryByDate.class,
                        episode.selectedDate.month(),
                        episode.selectedDate.count()))
                .from(episode)
                .where(episode.user.eq(user)
                        .and(episode.selectedDate.year().eq(year)))
                .groupBy(episode.selectedDate.month())
                .fetch();

    }

    @Override
    public List<GetEpisodeSummaryByActivity> getEpisodeSummaryPageByActivity(User user) {
        return jpaQueryFactory
                .select(Projections.constructor(GetEpisodeSummaryByActivity.class,
                        episode.activityTag.activityTagName,
                        episode.activityTag.count()))
                .from(episode)
                .where(episode.user.eq(user))
                .groupBy(episode.activityTag.activityTagName)
                .orderBy(episode.activityTag.count().desc())
                .fetch();
    }

    @Override
    public List<GetEpisodeByDate> getEpisodePageByDate(User user, Integer month, Long cursorId, Integer pageSize) {
        return jpaQueryFactory
                .select(Projections.constructor(GetEpisodeByDate.class,
                        episode.id,
                        episode.title,
                        episode.selectedDate.stringValue(),
                        episodeContent.content))
                .from(episode)
                .leftJoin(episodeContent)
                .on(episode.id.eq(episodeContent.episode.id))
                .where(episode.user.eq(user)
                        .and(episode.selectedDate.month().eq(month))
                        .and(ltEpisodeId(cursorId)))
                .orderBy(episode.id.desc())
                .limit(pageSize)
                .fetch()
                .stream().distinct()
                .collect(Collectors.toList());
    }

    private BooleanExpression ltEpisodeId(Long cursorId) {
        return cursorId == null ? null : episode.id.lt(cursorId);
    }

}

