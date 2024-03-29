package com.example.daycarat.domain.episode.service;

import com.example.daycarat.domain.activity.entity.ActivityTag;
import com.example.daycarat.domain.activity.repository.ActivityTagRepository;
import com.example.daycarat.domain.activity.service.ActivityTagService;
import com.example.daycarat.domain.episode.dto.*;
import com.example.daycarat.domain.episode.entity.*;
import com.example.daycarat.domain.episode.repository.EpisodeContentRepository;
import com.example.daycarat.domain.episode.repository.EpisodeRepository;
import com.example.daycarat.domain.episode.validator.EpisodeValidator;
import com.example.daycarat.domain.user.entity.User;
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
    private final ActivityTagService activityTagService;

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
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        ActivityTag activityTag = getActivityTag(user, postEpisode.activityTag());

        if (!activityTagRepository.existsByActivityTagNameAndUserIdAndIsDeleted(postEpisode.activityTag(), user.getId(), false)) {
            activityTagService.insertActivityTagSearch(user.getId(), postEpisode.activityTag());
        }

        activityTagRepository.save(activityTag);

        Episode episode = Episode.builder()
                .user(user)
                .activityTag(activityTag)
                .title(postEpisode.title())
                .selectedDate(LocalDateTimeParser.toLocalDate(postEpisode.date()))
                .episodeState(EpisodeState.UNFINALIZED)
                .episodeKeyword(EpisodeKeyword.UNSET)
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
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // update Episode

        Episode episode = episodeRepository.findById(patchEpisode.episodeId())
                .orElseThrow(() -> new CustomException(ErrorCode.EPISODE_NOT_FOUND));

        EpisodeValidator.checkIfDeleted(episode);
        EpisodeValidator.checkIfUserEpisodeMatches(user, episode);

        ActivityTag activityTag = getActivityTag(user, patchEpisode.activityTag());

        if (!activityTagRepository.existsByActivityTagNameAndUserIdAndIsDeleted(patchEpisode.activityTag(), user.getId(), false)) {
            activityTagService.insertActivityTagSearch(user.getId(), patchEpisode.activityTag());
        }

        activityTagRepository.save(activityTag);

        episode.update(activityTag, patchEpisode.title(), LocalDateTimeParser.toLocalDate(patchEpisode.selectedDate()));

        // delete previous EpisodeContents
        episode.getEpisodeContents().forEach(EpisodeContent::delete);
        episodeRepository.save(episode);

        // create new EpisodeContents
        createEpisodeContent(episode, patchEpisode.episodeContent());

        selectMainContent(episode.getId());

        return true;

    }

    public Boolean deleteEpisode(Long episodeId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

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
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return episodeRepository.findTop3ByUserOrderBySelectedDateDesc(user)
                .stream()
                .map(episode -> GetRecentEpisode.of(episode.getId(), episode.getTitle(), LocalDateTimeParser.toTimeAgo(episode.getCreatedDate()), episode.getEpisodeState()))
                .collect(Collectors.toList());

    }

    public List<GetEpisodeSummaryByDate> getEpisodeSummaryByDate(Integer year) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (year == null) year = 2024;

        return episodeRepository.getEpisodeSummaryByDate(user, year);
    }

    public List<GetEpisodeSummaryByActivity> getEpisodeSummaryByActivity() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return episodeRepository.getEpisodeSummaryByActivity(user);

    }

    public List<GetEpisodePage> getEpisodeByDate(Integer year, Integer month, Long cursorId, Integer pageSize) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if (pageSize == null) pageSize = 6;

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return episodeRepository.getEpisodePageByDate(user, year, month, cursorId, pageSize)
                .stream()
                .map(GetEpisodePageDto::convert)
                .collect(Collectors.toList());
    }

    public List<GetEpisodePage> getEpisodeByActivity(String activityTagName, Long cursorId, Integer pageSize) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if (pageSize == null) pageSize = 6;

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return episodeRepository.getEpisodePageByActivity(user, activityTagName, cursorId, pageSize)
                .stream()
                .map(GetEpisodePageDto::convert)
                .collect(Collectors.toList());
    }

    public GetEpisodeDetail getEpisodeDetail(Long episodeId) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new CustomException(ErrorCode.EPISODE_NOT_FOUND));

        EpisodeValidator.checkIfDeleted(episode);

        if (!episode.getUser().equals(user)) {
            throw new CustomException(ErrorCode.EPISODE_USER_NOT_MATCHED);
        }

        return GetEpisodeDetail.of(episode);

    }

    public GetEpisodeCount getEpisodeCount() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();

        return episodeRepository.getEpisodeCountOfTheMonth(user, now.getYear(), now.getMonthValue());

    }

    public GetEpisodeCount getAllEpisodeCount() {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return episodeRepository.getEpisodeCount(user);

    }

    public Boolean updateEpisodeKeyword(PatchEpisodeKeyword patchEpisodeKeyword) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Episode episode = episodeRepository.findById(patchEpisodeKeyword.episodeId())
                .orElseThrow(() -> new CustomException(ErrorCode.EPISODE_NOT_FOUND));

        EpisodeValidator.checkIfDeleted(episode);
        EpisodeValidator.checkIfUserEpisodeMatches(user, episode);
        EpisodeValidator.checkIfFinalized(episode);

//        AI 추천 문장 생성에 실패한 경우에도 직접 키워드를 수정해야 하므로 주석처리
//        generatedContentService.checkIfGeneratedContentExists(episode.getId());

        episode.updateKeyword(EpisodeKeyword.fromValue(patchEpisodeKeyword.keyword()));
        episode.updateIsEpisodeKeywordUserSelected(true);

        episodeRepository.save(episode);

        return true;

    }
}
