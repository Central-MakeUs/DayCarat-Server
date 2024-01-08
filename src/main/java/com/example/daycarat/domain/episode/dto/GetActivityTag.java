package com.example.daycarat.domain.episode.dto;

import com.example.daycarat.domain.episode.entity.ActivityTag;

import java.util.List;

public record GetActivityTag(Long id, String activityTagName) {
    public static List<GetActivityTag> listOf(List<ActivityTag> allByUserId) {
        return allByUserId.stream()
                .map(activityTag -> new GetActivityTag(activityTag.getId(), activityTag.getActivityTagName()))
                .toList();
    }
}
