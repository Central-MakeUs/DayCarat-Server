package com.example.daycarat.domain.episode.repository;

import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpisodeRepository extends JpaRepository<Episode, Long>, EpisodeRepositoryCustom {
    void deleteAllByUser(User user);
}
