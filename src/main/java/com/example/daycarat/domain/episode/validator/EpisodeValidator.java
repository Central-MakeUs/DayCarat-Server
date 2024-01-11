package com.example.daycarat.domain.episode.validator;

import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.domain.user.domain.User;
import com.example.daycarat.global.error.exception.CustomException;
import com.example.daycarat.global.error.exception.ErrorCode;

public class EpisodeValidator {

    public static void checkIfUserEpisodeMatches(User user, Episode episode) {
        if (!episode.getUser().equals(user)) {
            throw new CustomException(ErrorCode.EPISODE_USER_NOT_MATCHED);
        }
    }
}
