package com.example.daycarat.domain.activity.service;

import com.example.daycarat.domain.activity.dto.GetActivityTag;
import com.example.daycarat.domain.activity.dto.GetActivityTagSearch;
import com.example.daycarat.domain.activity.dto.PatchActivityTag;
import com.example.daycarat.domain.activity.entity.ActivityTag;
import com.example.daycarat.domain.activity.entity.ActivityTagSearch;
import com.example.daycarat.domain.activity.repository.ActivityTagRepository;
import com.example.daycarat.domain.activity.validator.ActivityTagValidator;
import com.example.daycarat.domain.episode.repository.EpisodeRepository;
import com.example.daycarat.domain.user.entity.User;
import com.example.daycarat.domain.user.repository.UserRepository;
import com.example.daycarat.dynamodb.ActivityTagSearchRepository;
import com.example.daycarat.global.error.exception.CustomException;
import com.example.daycarat.global.error.exception.ErrorCode;
import com.example.daycarat.global.util.StringParser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class ActivityTagService {

    private final UserRepository userRepository;
    private final ActivityTagRepository activityTagRepository;
    private final EpisodeRepository episodeRepository;
    private final ActivityTagSearchRepository activityTagSearchRepository;

//    * Deprecated method
//    public Boolean createActivityTag(PostActivityTag postActivityTag) {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
//
//        ActivityTag activityTag = ActivityTag.of(user, postActivityTag.activityTagName());
//
//        activityTagRepository.save(activityTag);
//
//        return true;
//    }

    public void insertActivityTagSearch(Long userId, String activityTagName) {
        String decompose = StringParser.decompose(activityTagName);

        ActivityTagSearch activityTagSearch = ActivityTagSearch.builder()
                .userId(userId)
                .activityTag(activityTagName)
                .activityTagSearch(decompose)
                .build();

        activityTagSearchRepository.save(activityTagSearch);
    }

    public List<GetActivityTag> getActivityTagList() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<ActivityTag> allByUserId = activityTagRepository.findAllByUserId(user.getId());

        allByUserId = allByUserId.stream()
                .filter(activityTag -> !activityTag.getIsDeleted())
                .collect(Collectors.toList());

        return GetActivityTag.listOf(allByUserId);

    }

    @Transactional
    public Boolean updateActivityTag(PatchActivityTag patchActivityTag) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        ActivityTag activityTag = activityTagRepository.findById(patchActivityTag.activityTagId())
                .orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_TAG_NOT_FOUND));
        ActivityTagValidator.checkIfActivityTagAndUserMatches(activityTag, user.getId());
        ActivityTagValidator.checkIfDeleted(activityTag);

        System.out.println("activityTag.getActivityTagName() = " + activityTag.getActivityTagName());
        updateActivityTagSearch(user.getId(), activityTag.getActivityTagName(), patchActivityTag.activityTagName());

        activityTag.update(patchActivityTag.activityTagName());

        activityTagRepository.save(activityTag);


        return true;

    }

    public void updateActivityTagSearch(Long userId, String activityTagName, String updateActivityTagName) {
        activityTagSearchRepository.findAllByActivityTagAndUserId(activityTagName, userId)
                .forEach(activityTagSearch -> {
                    activityTagSearch.update(updateActivityTagName, StringParser.decompose(updateActivityTagName));
                    activityTagSearchRepository.save(activityTagSearch);
                });
    }

    @Transactional
    public Boolean deleteActivityTag(Long activityTagId) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        ActivityTag activityTag = activityTagRepository.findById(activityTagId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_TAG_NOT_FOUND));

        ActivityTagValidator.checkIfActivityTagAndUserMatches(activityTag, user.getId());

        // check isDeleted
        episodeRepository.findAllByActivityTagId(activityTagId)
                .forEach(episode -> {
                    System.out.println("episode = " + episode.toString());
                    if (!episode.getIsDeleted()) {
                        throw new CustomException(ErrorCode.ACTIVITY_TAG_CANNOT_DELETE);
                    }
                });

        activityTag.delete();
        activityTagRepository.save(activityTag);

        List<ActivityTagSearch> activityTagSearchAndUserId = activityTagSearchRepository.findAllByActivityTagAndUserId(activityTag.getActivityTagName(), user.getId());

        activityTagSearchAndUserId.forEach(activityTagSearch -> {
            activityTagSearch.delete();
            System.out.println("activityTagSearch = " + activityTagSearch.getIsDeleted());
            activityTagSearchRepository.save(activityTagSearch);
        });

        return true;
    }

    public List<GetActivityTagSearch> searchActivityTag(String activityTagName) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<ActivityTagSearch> activityTagSearches = activityTagSearchRepository
                .findDistinctByActivityTagSearchContainingAndUserId(StringParser.decompose(activityTagName), user.getId());

        return activityTagSearches.stream()
                .filter(activityTagSearch -> !activityTagSearch.getIsDeleted())
                .map(GetActivityTagSearch::of)
                .collect(Collectors.toList());
    }
}
