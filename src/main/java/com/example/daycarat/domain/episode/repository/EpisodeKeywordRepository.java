package com.example.daycarat.domain.episode.repository;

import com.example.daycarat.domain.episode.entity.EpisodeKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpisodeKeywordRepository extends JpaRepository<EpisodeKeyword, Long> {
    List<EpisodeKeyword> findByEpisodeId(Long episodeId);

}
