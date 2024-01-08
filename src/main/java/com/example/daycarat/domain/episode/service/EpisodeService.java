package com.example.daycarat.domain.episode.service;

import com.example.daycarat.domain.episode.dto.GetEpisodeSummary;
import com.example.daycarat.domain.episode.dto.GetRecentEpisode;
import com.example.daycarat.domain.episode.dto.PostEpisode;
import com.example.daycarat.domain.episode.dto.PostEpisodeContent;
import com.example.daycarat.domain.episode.entity.ActivityTag;
import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.domain.episode.entity.EpisodeActivityTag;
import com.example.daycarat.domain.episode.entity.EpisodeContent;
import com.example.daycarat.domain.episode.repository.ActivityTagRepository;
import com.example.daycarat.domain.episode.repository.EpisodeActivityTagRepository;
import com.example.daycarat.domain.episode.repository.EpisodeContentRepository;
import com.example.daycarat.domain.episode.repository.EpisodeRepository;
import com.example.daycarat.domain.user.domain.User;
import com.example.daycarat.domain.user.repository.UserRepository;
import com.example.daycarat.global.error.exception.CustomException;
import com.example.daycarat.global.error.exception.ErrorCode;
import com.example.daycarat.global.util.LocalDateTimeParser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class EpisodeService {

    private final UserRepository userRepository;
    private final EpisodeRepository episodeRepository;
    private final ActivityTagRepository activityTagRepository;
    private final EpisodeActivityTagRepository episodeActivityTagRepository;
    private final EpisodeContentRepository episodeContentRepository;

    @Transactional
    public Boolean createEpisode(PostEpisode postEpisode) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // TODO : Validation for tagIds

        // create Episode

        Episode episode = Episode.builder()
                .user(user)
                .title(postEpisode.title())
                .selectedDate(LocalDateTimeParser.toLocalDate(postEpisode.date()))
                .episodeType(postEpisode.episodeType())
                .participationRole(postEpisode.participationRole())
                .isFinalized(false)
                .build();

        episodeRepository.save(episode);

        // create EpisodeActivityTag

        List<ActivityTag> activityTags = activityTagRepository.findAllById(postEpisode.activityTagIds());

        for (ActivityTag activityTag : activityTags) {
            EpisodeActivityTag episodeActivityTag = EpisodeActivityTag.builder()
                    .episode(episode)
                    .activityTag(activityTag)
                    .build();
            episodeActivityTagRepository.save(episodeActivityTag);
        }

        // create EpisodeContent

        for (PostEpisodeContent postEpisodeContent : postEpisode.episodeContents()) {
            EpisodeContent episodeContent = EpisodeContent.builder()
                    .episode(episode)
                    .episodeContentType(postEpisodeContent.episodeContentType())
                    .content(postEpisodeContent.content())
                    .build();
            episodeContentRepository.save(episodeContent);
        }

        return true;

    }

    public List<GetRecentEpisode> getRecentEpisode() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return episodeRepository.findTop3ByUserOrderBySelectedDateDesc(user)
                .stream()
                .map(episode -> GetRecentEpisode.of(episode.getTitle(), LocalDateTimeParser.toTimeAgo(episode.getCreatedDate())))
                .collect(Collectors.toList());

    }

    public List<GetEpisodeSummary> getEpisodeByDate(Integer year) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (year == null) year = 2024;

        return episodeRepository.getPageByDate(user, year);
    }
}
