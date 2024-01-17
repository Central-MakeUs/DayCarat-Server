package com.example.daycarat.domain.gem.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.example.daycarat.domain.episode.entity.QEpisode.episode;
import static com.example.daycarat.domain.gem.entity.QGem.gem;

@RequiredArgsConstructor
public class GemRepositoryImpl implements GemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Long countByUserId(Long userId) {
        return jpaQueryFactory
                .select(gem.count())
                .from(gem)
                .leftJoin(gem.episode)
                .on(gem.episode.isDeleted.eq(false))
                .where(episode.user.id.eq(userId)
                        .and(gem.isDeleted.eq(false)))
                .fetchOne();
    }
}
