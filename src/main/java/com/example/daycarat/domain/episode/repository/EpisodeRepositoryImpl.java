package com.example.daycarat.domain.episode.repository;

import com.example.daycarat.domain.episode.dto.GetEpisodePage;
import com.example.daycarat.domain.episode.dto.GetEpisodeSummaryByActivity;
import com.example.daycarat.domain.episode.dto.GetEpisodeSummaryByDate;
import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.domain.user.domain.User;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.daycarat.domain.episode.entity.EpisodeState.UNFINALIZED;
import static com.example.daycarat.domain.episode.entity.QEpisode.episode;
import static com.example.daycarat.domain.episode.entity.QEpisodeContent.episodeContent;

@RequiredArgsConstructor
public class EpisodeRepositoryImpl implements EpisodeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Episode> findTop3ByUserOrderBySelectedDateDesc(User user) {
        return jpaQueryFactory
                .selectFrom(episode)
                .where(episode.user.eq(user)
                        .and(episode.episodeState.eq(UNFINALIZED))
                        .and(episode.isDeleted.eq(false)))
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
                        .and(episode.selectedDate.year().eq(year))
                        .and(episode.episodeState.eq(UNFINALIZED))
                        .and(episode.isDeleted.eq(false)))
                .groupBy(episode.selectedDate.month())
                .fetch();

    }

    @Override
    public List<GetEpisodeSummaryByActivity> getEpisodeSummaryByActivity(User user) {
        return jpaQueryFactory
                .select(Projections.constructor(GetEpisodeSummaryByActivity.class,
                        episode.activityTag.activityTagName,
                        episode.activityTag.count()))
                .from(episode)
                .where(episode.user.eq(user)
                        .and(episode.episodeState.eq(UNFINALIZED))
                        .and(episode.isDeleted.eq(false)))
                .groupBy(episode.activityTag.activityTagName)
                .orderBy(episode.activityTag.count().desc())
                .fetch();
    }

    @Override
    public List<GetEpisodePage> getEpisodePageByDate(User user, Integer year, Integer month, Long cursorId, Integer pageSize) {
        return jpaQueryFactory
                .select(Projections.constructor(GetEpisodePage.class,
                        episode.id,
                        episode.title,
                        convertToMonthDayFormat(episode.selectedDate.stringValue()),
                        episodeContent.content))
                .from(episode)
                .leftJoin(episodeContent)
                .on(episode.id.eq(episodeContent.episode.id))
                .where(episode.user.eq(user)
                        .and(episode.selectedDate.year().eq(year))
                        .and(episode.selectedDate.month().eq(month))
                        .and(ltEpisodeId(cursorId))
                        .and(episode.episodeState.eq(UNFINALIZED))
                        .and(episode.isDeleted.eq(false)))
                .orderBy(episode.id.desc())
                .limit(pageSize)
                .fetch()
                .stream().distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<GetEpisodePage> getEpisodePageByActivity(User user, String activityTagName, Long cursorId, Integer pageSize) {
        return jpaQueryFactory
                .select(Projections.constructor(GetEpisodePage.class,
                        episode.id,
                        episode.title,
                        convertToMonthDayFormat(episode.selectedDate.stringValue()),
                        episodeContent.content))
                .from(episode)
                .leftJoin(episodeContent)
                .on(episode.id.eq(episodeContent.episode.id))
                .where(episode.user.eq(user)
                        .and(episode.activityTag.activityTagName.eq(activityTagName))
                        .and(ltEpisodeId(cursorId))
                        .and(episode.episodeState.eq(UNFINALIZED))
                        .and(episode.isDeleted.eq(false)))
                .orderBy(episode.id.desc())
                .limit(pageSize)
                .fetch()
                .stream().distinct()
                .collect(Collectors.toList());

    }

    private StringExpression convertToMonthDayFormat(StringExpression dateExpression) {
        return Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})", dateExpression, "%m/%d");
    }

    private BooleanExpression ltEpisodeId(Long cursorId) {
        return cursorId == null ? null : episode.id.lt(cursorId);
    }

}

