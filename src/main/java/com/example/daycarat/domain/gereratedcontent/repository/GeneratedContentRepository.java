package com.example.daycarat.domain.gereratedcontent.repository;

import com.example.daycarat.domain.gereratedcontent.entity.GeneratedContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GeneratedContentRepository extends JpaRepository<GeneratedContent, Long> {
    Optional<GeneratedContent> findByEpisodeIdAndIsDeleted(Long episodeId, boolean isDeleted);

    List<GeneratedContent> findAllByEpisodeIdAndIsDeleted(Long episodeId, boolean isDeleted);
}
