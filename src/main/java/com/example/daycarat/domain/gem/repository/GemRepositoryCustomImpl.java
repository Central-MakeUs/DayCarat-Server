package com.example.daycarat.domain.gem.repository;

import com.example.daycarat.domain.episode.entity.EpisodeKeyword;
import com.example.daycarat.domain.episode.entity.EpisodeState;
import com.example.daycarat.domain.gem.dto.GetGemCount;
import com.example.daycarat.domain.gem.dto.GetGemSummaryByKeywordDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.daycarat.domain.episode.entity.QEpisode.episode;
import static com.example.daycarat.domain.gem.entity.QGem.gem;

@RequiredArgsConstructor
public class GemRepositoryCustomImpl implements GemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public GetGemCount getGemCount(Long userId) {
        return jpaQueryFactory
                .select(Projections.constructor(GetGemCount.class,
                        gem.count()
                ))
                .from(gem)
                .where(gem.episode.user.id.eq(userId)
                        .and(gem.isDeleted.eq(false)))
                .fetchOne();
    }

    @Override
    public List<GetGemSummaryByKeywordDto> getGemSummaryByKeyword(Long userId) {
        return jpaQueryFactory
                .select(Projections.constructor(GetGemSummaryByKeywordDto.class,
                        gem.episode.episodeKeyword,
                        gem.count()
                ))
                .from(gem)
                .where(gem.episode.user.id.eq(userId)
                        .and(gem.episode.isDeleted.eq(false))
                        .and(gem.episode.episodeState.eq(EpisodeState.FINALIZED))
                        .and(gem.isDeleted.eq(false)))
                .groupBy(gem.episode.episodeKeyword)
                .fetch();
    }

    @Override
    public EpisodeKeyword getMostGemKeyword(Long userId) {
        return jpaQueryFactory
                .select(episode.episodeKeyword)
                .from(episode)
                .where(episode.user.id.eq(userId)
                        .and(episode.isDeleted.eq(false))
                        .and(episode.episodeState.eq(EpisodeState.FINALIZED))
                        .and(episode.episodeKeyword.ne(EpisodeKeyword.UNSET)))
                .groupBy(episode.episodeKeyword)
                .orderBy(episode.episodeKeyword.count().desc())
                .fetchFirst();

    }

}
