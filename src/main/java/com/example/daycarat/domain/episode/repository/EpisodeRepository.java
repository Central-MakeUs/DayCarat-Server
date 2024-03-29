package com.example.daycarat.domain.episode.repository;

import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EpisodeRepository extends JpaRepository<Episode, Long>, EpisodeRepositoryCustom {
    void deleteAllByUser(User user);
    List<Episode> findAllByActivityTagId(Long activityTagId);
    Optional<Episode> findByGemId(Long gemId);
    List<Episode> findAllByIsDeleted(boolean isDeleted);
}
