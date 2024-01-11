package com.example.daycarat.domain.episode.repository;

import com.example.daycarat.domain.episode.entity.EpisodeContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpisodeContentRepository extends JpaRepository<EpisodeContent, Long> {
    List<EpisodeContent> findByEpisodeId(Long episodeId);
}
