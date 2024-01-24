package com.example.daycarat.domain.gem.repository;

import com.example.daycarat.domain.gem.entity.Gem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GemRepository extends JpaRepository<Gem, Long>, GemRepositoryCustom {
    Optional<Gem> findByS3ObjectKeyAndIsDeleted(String s3ObjectKey, boolean isDeleted);

    Optional<Gem> findByEpisodeIdAndIsDeleted(Long episodeId, boolean isDeleted);
}