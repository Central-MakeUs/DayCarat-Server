package com.example.daycarat.domain.activity.dto;

import com.example.daycarat.domain.activity.entity.ActivityTagSearch;

public record GetActivityTagSearch(
        String activityTagName
) {
    public static GetActivityTagSearch of(ActivityTagSearch activityTagSearch) {
        return new GetActivityTagSearch(activityTagSearch.getActivityTag());
    }
}
