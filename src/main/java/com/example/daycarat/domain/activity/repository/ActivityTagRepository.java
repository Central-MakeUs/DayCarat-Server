package com.example.daycarat.domain.activity.repository;

import com.example.daycarat.domain.activity.entity.ActivityTag;
import com.example.daycarat.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityTagRepository extends JpaRepository<ActivityTag, Long> {
    List<ActivityTag> findAllByUserId(Long userId);
    Optional<ActivityTag> findByUserIdAndActivityTagName(Long userId, String activityTagName);

    void deleteAllByUser(User user);

    boolean existsByActivityTagNameAndUserIdAndIsDeleted(String activityTagName, Long userId, boolean isDeleted);
}
