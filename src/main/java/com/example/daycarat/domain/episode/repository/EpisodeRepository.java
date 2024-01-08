package com.example.daycarat.domain.episode.repository;

import com.example.daycarat.domain.episode.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpisodeRepository extends JpaRepository<Episode, Long>, EpisodeRepositoryCustom {
}
