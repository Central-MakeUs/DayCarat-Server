package com.example.daycarat.domain.episode.repository;

import com.example.daycarat.domain.episode.entity.ActivityTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityTagRepository extends JpaRepository<ActivityTag, Long> {
    List<ActivityTag> findAllByUserId(Long userId);
}
