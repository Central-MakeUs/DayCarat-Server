package com.example.daycarat.domain.episode.repository;

import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpisodeRepository extends JpaRepository<Episode, Long>, EpisodeRepositoryCustom {
    void deleteAllByUser(User user);
    List<Episode> findByActivityTagId(Long activityTagId);
}
