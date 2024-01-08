package com.example.daycarat.domain.episode.repository;

import com.example.daycarat.domain.episode.dto.GetEpisodeSummary;
import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.domain.user.domain.User;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.daycarat.domain.episode.entity.QEpisode.episode;

@RequiredArgsConstructor
public class EpisodeRepositoryImpl implements EpisodeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Episode> findTop3ByUserOrderBySelectedDateDesc(User user) {
        return jpaQueryFactory
                .selectFrom(episode)
                .where(episode.user.eq(user))
                .orderBy(episode.selectedDate.desc())
                .limit(3)
                .fetch();
    }

    @Override
    public List<GetEpisodeSummary> getPageByDate(User user, Integer year) {
        return jpaQueryFactory
                .select(Projections.constructor(GetEpisodeSummary.class,
                        episode.selectedDate.month(),
                        episode.selectedDate.count()))
                .from(episode)
                .where(episode.user.eq(user)
                        .and(episode.selectedDate.year().eq(year)))
                .groupBy(episode.selectedDate.month())
                .fetch();

    }

    private BooleanExpression cursorId(Long cursorId) { // 첫 페이지 조회와 두번째 이상 페이지 조회를 구분하기 위함
        if(cursorId == null) return null;

        return episode.id.lt(cursorId);

    }

}
