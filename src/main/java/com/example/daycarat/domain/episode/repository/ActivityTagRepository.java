package com.example.daycarat.domain.episode.repository;

import com.example.daycarat.domain.episode.entity.ActivityTag;
import com.example.daycarat.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityTagRepository extends JpaRepository<ActivityTag, Long> {
    List<ActivityTag> findAllByUserId(Long userId);
    Optional<ActivityTag> findByUserIdAndActivityTagName(Long userId, String activityTagName);

    void deleteAllByUser(User user);
}
