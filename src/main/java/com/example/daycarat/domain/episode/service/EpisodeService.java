package com.example.daycarat.domain.episode.service;

import com.example.daycarat.domain.episode.dto.PostEpisode;
import com.example.daycarat.domain.episode.entity.ActivityTag;
import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.domain.episode.entity.EpisodeActivityTag;
import com.example.daycarat.domain.episode.repository.ActivityTagRepository;
import com.example.daycarat.domain.episode.repository.EpisodeActivityTagRepository;
import com.example.daycarat.domain.episode.repository.EpisodeRepository;
import com.example.daycarat.domain.user.domain.User;
import com.example.daycarat.domain.user.repository.UserRepository;
import com.example.daycarat.global.error.exception.CustomException;
import com.example.daycarat.global.error.exception.ErrorCode;
import com.example.daycarat.global.util.LocalDateParser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class EpisodeService {

    private final UserRepository userRepository;
    private final EpisodeRepository episodeRepository;
    private final ActivityTagRepository activityTagRepository;
    private final EpisodeActivityTagRepository episodeActivityTagRepository;

    @Transactional
    public Boolean createEpisode(PostEpisode postEpisode) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Episode episode = Episode.builder()
                .user(user)
                .title(postEpisode.title())
                .selectedDate(LocalDateParser.toLocalDate(postEpisode.date()))
                .episodeType(postEpisode.episodeType())
                .participationRole(postEpisode.participationRole())
                .isFinalized(false)
                .build();

        episodeRepository.save(episode);

        List<ActivityTag> activityTags = activityTagRepository.findAllById(postEpisode.activityTagIds());

        for (ActivityTag activityTag : activityTags) {
            EpisodeActivityTag episodeActivityTag = EpisodeActivityTag.builder()
                    .episode(episode)
                    .activityTag(activityTag)
                    .build();
            episodeActivityTagRepository.save(episodeActivityTag);
        }

        return true;

    }

}
