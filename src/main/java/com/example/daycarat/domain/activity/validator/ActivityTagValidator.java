package com.example.daycarat.domain.activity.validator;

import com.example.daycarat.domain.activity.entity.ActivityTag;
import com.example.daycarat.global.error.exception.CustomException;
import com.example.daycarat.global.error.exception.ErrorCode;

public class ActivityTagValidator {

    public static void checkIfDeleted(ActivityTag activityTag) {
        if (activityTag.getIsDeleted()) {
            throw new CustomException(ErrorCode.ACTIVITY_TAG_NOT_FOUND);
        }
    }

    public static void checkIfActivityTagAndUserMatches(ActivityTag activityTag, Long userId) {
        if (!activityTag.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.ACTIVITY_USER_NOT_MATCHED);
        }
    }
}
