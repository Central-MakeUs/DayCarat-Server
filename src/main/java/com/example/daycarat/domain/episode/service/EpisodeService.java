package com.example.daycarat.domain.episode.service;

import com.example.daycarat.domain.episode.dto.*;
import com.example.daycarat.domain.activity.entity.ActivityTag;
import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.domain.episode.entity.EpisodeContent;
import com.example.daycarat.domain.episode.entity.EpisodeContentType;
import com.example.daycarat.domain.episode.entity.EpisodeState;
import com.example.daycarat.domain.activity.repository.ActivityTagRepository;
import com.example.daycarat.domain.episode.repository.EpisodeContentRepository;
import com.example.daycarat.domain.episode.repository.EpisodeRepository;
import com.example.daycarat.domain.episode.validator.EpisodeValidator;
import com.example.daycarat.domain.user.domain.User;
import com.example.daycarat.domain.user.repository.UserRepository;
import com.example.daycarat.global.error.exception.CustomException;
import com.example.daycarat.global.error.exception.ErrorCode;
import com.example.daycarat.global.util.LocalDateTimeParser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class EpisodeService {

    private final UserRepository userRepository;
    private final EpisodeRepository episodeRepository;
    private final ActivityTagRepository activityTagRepository;
    private final EpisodeContentRepository episodeContentRepository;

    private ActivityTag getActivityTag(User user, String activityTagName) {
        ActivityTag activityTag = activityTagRepository.findByUserIdAndActivityTagName(user.getId(), activityTagName)
                .orElse(ActivityTag.of(user, activityTagName));

        if (activityTag.getIsDeleted()) return ActivityTag.of(user, activityTagName);
        else return activityTag;

    }

    private void createEpisodeContent(Episode episode, List<PostEpisodeContent> postEpisodeContents) {
        for (PostEpisodeContent postEpisodeContent : postEpisodeContents) {
            EpisodeContent episodeContent = EpisodeContent.of(episode, EpisodeContentType.fromValue(postEpisodeContent.episodeContentType()), postEpisodeContent.content(), false);
            episodeContentRepository.save(episodeContent);
        }
    }

    public void selectMainContent(Long episodeId) {
        List<EpisodeContent> episodeContents = episodeContentRepository.findByEpisodeIdAndIsDeleted(episodeId, false);
        if (episodeContents.stream().noneMatch(EpisodeContent::getIsMainContent)) {
            episodeContents.get(0).setIsMainContent(true);
        }
    }

    @Transactional
    public Boolean createEpisode(PostEpisode postEpisode) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        ActivityTag activityTag = getActivityTag(user, postEpisode.activityTag());

        activityTagRepository.save(activityTag);

        Episode episode = Episode.builder()
                .user(user)
                .activityTag(activityTag)
                .title(postEpisode.title())
                .selectedDate(LocalDateTimeParser.toLocalDate(postEpisode.date()))
                .episodeState(EpisodeState.UNFINALIZED)
                .build();

        episodeRepository.save(episode);

        createEpisodeContent(episode, postEpisode.episodeContents());

        selectMainContent(episode.getId());

        return true;

    }

    @Transactional
    public Boolean updateEpisode(PatchEpisode patchEpisode) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // update Episode

        Episode episode = episodeRepository.findById(patchEpisode.episodeId())
                .orElseThrow(() -> new CustomException(ErrorCode.EPISODE_NOT_FOUND));

        EpisodeValidator.checkIfDeleted(episode);
        EpisodeValidator.checkIfUserEpisodeMatches(user, episode);

        ActivityTag activityTag = getActivityTag(user, patchEpisode.activityTag());

        activityTagRepository.save(activityTag);

        episode.update(activityTag, patchEpisode.title(), LocalDateTimeParser.toLocalDate(patchEpisode.selectedDate()));

        // delete EpisodeContent
        List<Long> episodeContentIds = patchEpisode.episodeContents().stream()
                .map(PatchEpisodeContent::episodeContentId)
                .toList();

        deleteEpisodeContent(episode.getId(), episodeContentIds);

        // update EpisodeContent
        for (PatchEpisodeContent patchEpisodeContent : patchEpisode.episodeContents()) {
            EpisodeContent episodeContent = episodeContentRepository.findById(patchEpisodeContent.episodeContentId())
                    .orElseThrow(() -> new CustomException(ErrorCode.EPISODE_CONTENT_NOT_FOUND));

            episodeContent.update(EpisodeContentType.fromValue(patchEpisodeContent.episodeContentType()), patchEpisodeContent.content());
        }

        // create EpisodeContent
        createEpisodeContent(episode, patchEpisode.newEpisodeContents());

        selectMainContent(episode.getId());

        return true;

    }

    private void deleteEpisodeContent(Long episodeId, List<Long> episodeContentIds) {

        List<EpisodeContent> byEpisodeId = episodeContentRepository.findByEpisodeId(episodeId);

        for (EpisodeContent episodeContent : byEpisodeId) {
            if (!episodeContentIds.contains(episodeContent.getId())) {
                episodeContent.delete();
            }
        }

    }

    public Boolean deleteEpisode(Long episodeId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new CustomException(ErrorCode.EPISODE_NOT_FOUND));

        EpisodeValidator.checkIfDeleted(episode);
        EpisodeValidator.checkIfUserEpisodeMatches(user, episode);

        episodeContentRepository.findByEpisodeId(episodeId)
                .forEach(EpisodeContent::delete);

        episode.delete();
        episodeRepository.save(episode);

        return true;

    }

    public List<GetRecentEpisode> getRecentEpisode() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return episodeRepository.findTop3ByUserOrderBySelectedDateDesc(user)
                .stream()
                .map(episode -> GetRecentEpisode.of(episode.getId(), episode.getTitle(), LocalDateTimeParser.toTimeAgo(episode.getCreatedDate()), episode.getEpisodeState()))
                .collect(Collectors.toList());

    }

    public List<GetEpisodeSummaryByDate> getEpisodeSummaryByDate(Integer year) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (year == null) year = 2024;

        return episodeRepository.getEpisodeSummaryByDate(user, year);
    }

    public List<GetEpisodeSummaryByActivity> getEpisodeSummaryByActivity() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return episodeRepository.getEpisodeSummaryByActivity(user);

    }

    public List<GetEpisodePage> getEpisodeByDate(Integer year, Integer month, Long cursorId, Integer pageSize) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if (pageSize == null) pageSize = 6;

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return episodeRepository.getEpisodePageByDate(user, year, month, cursorId, pageSize);
    }

    public List<GetEpisodePage> getEpisodeByActivity(String activityTagName, Long cursorId, Integer pageSize) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if (pageSize == null) pageSize = 6;

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return episodeRepository.getEpisodePageByActivity(user, activityTagName, cursorId, pageSize);
    }

    public GetEpisodeDetail getEpisodeDetail(Long episodeId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new CustomException(ErrorCode.EPISODE_NOT_FOUND));

        if (!episode.getUser().equals(user)) {
            throw new CustomException(ErrorCode.EPISODE_USER_NOT_MATCHED);
        }

        return GetEpisodeDetail.of(
                episode.getId(),
                episode.getTitle(),
                episode.getActivityTag().getActivityTagName(),
                LocalDateTimeParser.toStringWithDetail(episode.getSelectedDate()),
                episode.getEpisodeState(),
                episode.getEpisodeContents().stream()
                        .filter(episodeContent -> !episodeContent.getIsDeleted())
                        .map(episodeContent -> GetEpisodeContent.of(episodeContent.getId(), episodeContent.getEpisodeContentType(), episodeContent.getContent()))
                        .collect(Collectors.toList()));

    }

    public GetEpisodeCount getEpisodeCount() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();

        return episodeRepository.getEpisodeCountOfTheMonth(user, now.getYear(), now.getMonthValue());

    }
}
