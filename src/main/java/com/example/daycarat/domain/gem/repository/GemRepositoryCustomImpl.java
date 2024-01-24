package com.example.daycarat.domain.gem.repository;

import com.example.daycarat.domain.activity.entity.ActivityTag;
import com.example.daycarat.domain.episode.entity.EpisodeKeyword;
import com.example.daycarat.domain.episode.entity.EpisodeState;
import com.example.daycarat.domain.gem.dto.GetEpisodeClipboardDto;
import com.example.daycarat.domain.gem.dto.GetGemCount;
import com.example.daycarat.domain.gem.dto.GetGemSummaryByKeywordDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.daycarat.domain.episode.entity.QEpisode.episode;
import static com.example.daycarat.domain.gem.entity.QGem.gem;
import static com.example.daycarat.domain.gereratedcontent.entity.QGeneratedContent.generatedContent;

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
    public GetGemCount getGemCountByMonth(Long userId) {
        return jpaQueryFactory
                .select(Projections.constructor(GetGemCount.class,
                        gem.count()
                ))
                .from(gem)
                .where(gem.episode.user.id.eq(userId)
                        .and(gem.isDeleted.eq(false))
                        .and(gem.lastModifiedDate.year().eq(LocalDateTime.now().getYear()))
                        .and(gem.lastModifiedDate.month().eq(LocalDateTime.now().getMonthValue())))
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

    @Override
    public ActivityTag getMostGemActivity(Long userId) {
        return jpaQueryFactory
                .select(gem.episode.activityTag)
                .from(gem)
                .where(gem.episode.user.id.eq(userId)
                        .and(gem.isDeleted.eq(false))
                        .and(gem.episode.isDeleted.eq(false))
                        .and(gem.episode.episodeState.eq(EpisodeState.FINALIZED)))
                .groupBy(gem.episode.activityTag)
                .orderBy(gem.episode.activityTag.count().desc())
                .fetchFirst();
    }

    @Override
    public GetEpisodeClipboardDto getEpisodeClipboard(Long episodeId) {
        return jpaQueryFactory
                .select(Projections.constructor(GetEpisodeClipboardDto.class,
                        episode.user.id,
                        gem.content1,
                        gem.content2,
                        gem.content3,
                        gem.content4,
                        gem.content5,
                        generatedContent.generatedContent1,
                        generatedContent.generatedContent2,
                        generatedContent.generatedContent3
                ))
                .from(episode)
                .leftJoin(gem).on(episode.id.eq(gem.episode.id))
                .leftJoin(generatedContent).on(episode.id.eq(generatedContent.episode.id))
                .where(episode.id.eq(episodeId)
                        .and(episode.isDeleted.eq(false))
                        .and(gem.isDeleted.eq(false))
                        .and(generatedContent.isDeleted.eq(false)))
                .fetchOne();
    }

}
