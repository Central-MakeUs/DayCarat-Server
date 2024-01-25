package com.example.daycarat.domain.episode.validator;

import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.domain.episode.entity.EpisodeState;
import com.example.daycarat.domain.user.entity.User;
import com.example.daycarat.global.error.exception.CustomException;
import com.example.daycarat.global.error.exception.ErrorCode;

public class EpisodeValidator {

    public static void checkIfDeleted(Episode episode) {
        if (episode.getIsDeleted()) {
            throw new CustomException(ErrorCode.EPISODE_NOT_FOUND);
        }
    }

    public static void checkIfUserEpisodeMatches(User user, Episode episode) {
        if (!episode.getUser().equals(user)) {
            throw new CustomException(ErrorCode.EPISODE_USER_NOT_MATCHED);
        }
    }

    public static void checkIfUnfinalized(Episode episode) {
        if (episode.getEpisodeState().equals(EpisodeState.FINALIZED)) {
            throw new CustomException(ErrorCode.GEM_ALREADY_EXISTS);
        }
    }

    public static void checkIfFinalized(Episode episode) {
        if (episode.getEpisodeState().equals(EpisodeState.UNFINALIZED)) {
            throw new CustomException(ErrorCode.GEM_NOT_FOUND);
        }
    }

}
