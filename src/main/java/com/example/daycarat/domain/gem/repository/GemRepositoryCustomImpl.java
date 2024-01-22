package com.example.daycarat.domain.gem.repository;

import com.example.daycarat.domain.gem.dto.GetGemCount;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

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
}
