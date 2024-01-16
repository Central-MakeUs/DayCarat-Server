package com.example.daycarat.domain.episode.service;

import com.example.daycarat.domain.episode.dto.GetActivityTag;
import com.example.daycarat.domain.episode.dto.PatchActivityTag;
import com.example.daycarat.domain.episode.entity.ActivityTag;
import com.example.daycarat.domain.episode.repository.ActivityTagRepository;
import com.example.daycarat.domain.episode.repository.EpisodeRepository;
import com.example.daycarat.domain.episode.validator.ActivityTagValidator;
import com.example.daycarat.domain.user.domain.User;
import com.example.daycarat.domain.user.repository.UserRepository;
import com.example.daycarat.global.error.exception.CustomException;
import com.example.daycarat.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class ActivityTagService {

    private final UserRepository userRepository;
    private final ActivityTagRepository activityTagRepository;
    private final EpisodeRepository episodeRepository;

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

    public List<GetActivityTag> getActivityTagList() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<ActivityTag> allByUserId = activityTagRepository.findAllByUserId(user.getId());

        allByUserId = allByUserId.stream()
                .filter(activityTag -> !activityTag.getIsDeleted())
                .collect(Collectors.toList());

        return GetActivityTag.listOf(allByUserId);

    }

    public Boolean updateActivityTag(PatchActivityTag patchActivityTag) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        ActivityTag activityTag = activityTagRepository.findById(patchActivityTag.activityTagId())
                .orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_TAG_NOT_FOUND));

        ActivityTagValidator.checkIfActivityTagAndUserMatches(activityTag, user.getId());

        ActivityTagValidator.checkIfDeleted(activityTag);

        activityTag.update(patchActivityTag.activityTagName());

        activityTagRepository.save(activityTag);

        return true;

    }

    public Boolean deleteActivityTag(Long activityTagId) {
        ActivityTag activityTag = activityTagRepository.findById(activityTagId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_TAG_NOT_FOUND));

        System.out.println("activityTag = " + activityTag.getActivityTagName());

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

        return true;
    }
}
