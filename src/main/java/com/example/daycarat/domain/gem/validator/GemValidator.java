package com.example.daycarat.domain.gem.validator;

import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.domain.episode.entity.EpisodeState;
import com.example.daycarat.global.error.exception.CustomException;
import com.example.daycarat.global.error.exception.ErrorCode;

public class GemValidator {

    public static void checkIfUnfinalized(Episode episode) {

        if (episode.getEpisodeState().equals(EpisodeState.FINALIZED)) {
            throw new CustomException(ErrorCode.GEM_ALREADY_EXISTS);
        }

        if (episode.getEpisodeState().equals(EpisodeState.DELETED)) {
            throw new CustomException(ErrorCode.EPISODE_NOT_FOUND);
        }

    }
}
