package com.example.daycarat.dynamodb;

import com.example.daycarat.domain.activity.entity.ActivityTagSearch;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface ActivityTagSearchRepository extends CrudRepository<ActivityTagSearch, Long> {
    List<ActivityTagSearch> findDistinctByActivityTagSearchContainingAndUserId(String activityTagName, Long userId);
    List<ActivityTagSearch> findAllByActivityTagSearchAndUserId(String activityTagName, Long userId);
    List<ActivityTagSearch> findAllByActivityTagAndUserId(String activityTagName, Long userId);

    void deleteAllByUserId(Long userId);
}

