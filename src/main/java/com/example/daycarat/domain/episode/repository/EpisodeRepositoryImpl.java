package com.example.daycarat.domain.episode.repository;

import com.example.daycarat.domain.episode.dto.GetEpisodeCount;
import com.example.daycarat.domain.episode.dto.GetEpisodePageDto;
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
                        .and(episode.isDeleted.eq(false)))
                .groupBy(episode.activityTag.activityTagName)
                .orderBy(episode.activityTag.count().desc())
                .fetch();
    }

    @Override
    public List<GetEpisodePageDto> getEpisodePageByDate(User user, Integer year, Integer month, Long cursorId, Integer pageSize) {
        return jpaQueryFactory
                .select(Projections.constructor(GetEpisodePageDto.class,
                        episode.id,
                        episode.title,
                        convertToMonthDayFormat(episode.selectedDate.stringValue()),
                        episode.episodeState,
                        episode.episodeKeyword,
                        episodeContent.content))
                .from(episode)
                .leftJoin(episodeContent)
                .on(episode.id.eq(episodeContent.episode.id))
                .where(episode.user.eq(user)
                        .and(episode.selectedDate.year().eq(year))
                        .and(episode.selectedDate.month().eq(month))
                        .and(ltEpisodeId(cursorId))
                        .and(episode.isDeleted.eq(false))
                        .and(episodeContent.isDeleted.eq(false))
                        .and(episodeContent.isMainContent.eq(true)))
                .orderBy(episode.id.desc())
                .limit(pageSize)
                .fetch()
                .stream().distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<GetEpisodePageDto> getEpisodePageByActivity(User user, String activityTagName, Long cursorId, Integer pageSize) {
        return jpaQueryFactory
                .select(Projections.constructor(GetEpisodePageDto.class,
                        episode.id,
                        episode.title,
                        convertToMonthDayFormat(episode.selectedDate.stringValue()),
                        episode.episodeState,
                        episode.episodeKeyword,
                        episodeContent.content))
                .from(episode)
                .leftJoin(episodeContent)
                .on(episode.id.eq(episodeContent.episode.id))
                .where(episode.user.eq(user)
                        .and(episode.activityTag.activityTagName.eq(activityTagName))
                        .and(ltEpisodeId(cursorId))
                        .and(episode.isDeleted.eq(false))
                        .and(episodeContent.isDeleted.eq(false))
                        .and(episodeContent.isMainContent.eq(true)))
                .orderBy(episode.id.desc())
                .limit(pageSize)
                .fetch()
                .stream().distinct()
                .collect(Collectors.toList());

    }

    @Override
    public GetEpisodeCount getEpisodeCountOfTheMonth(User user, Integer year, Integer month) {
        return jpaQueryFactory
                .select(Projections.constructor(GetEpisodeCount.class,
                        episode.selectedDate.count()))
                .from(episode)
                .where(episode.user.eq(user)
                        .and(episode.selectedDate.year().eq(year))
                        .and(episode.selectedDate.month().eq(month))
                        .and(episode.isDeleted.eq(false)))
                .fetchOne();
    }

    @Override
    public GetEpisodeCount getEpisodeCount(User user) {
        return jpaQueryFactory
                .select(Projections.constructor(GetEpisodeCount.class,
                        episode.selectedDate.count()))
                .from(episode)
                .where(episode.user.eq(user)
                        .and(episode.isDeleted.eq(false)))
                .fetchOne();
    }

    private StringExpression convertToMonthDayFormat(StringExpression dateExpression) {
        return Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})", dateExpression, "%m/%d");
    }

    private BooleanExpression ltEpisodeId(Long cursorId) {
        return cursorId == null ? null : episode.id.lt(cursorId);
    }

}

