package com.example.daycarat.domain.episode.dto;

import com.example.daycarat.domain.episode.entity.ActivityTag;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record GetActivityTag(
        @Schema(description = "activityTagId", example = "5") Long id,
        @Schema(description = "활동태그명", example = "영국 교환학생") String activityTagName) {

    public static List<GetActivityTag> listOf(List<ActivityTag> allByUserId) {
        return allByUserId.stream()
                .map(activityTag -> new GetActivityTag(activityTag.getId(), activityTag.getActivityTagName()))
                .toList();
    }
}
