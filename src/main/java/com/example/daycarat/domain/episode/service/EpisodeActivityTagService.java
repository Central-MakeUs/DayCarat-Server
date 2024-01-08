package com.example.daycarat.domain.episode.service;

import com.example.daycarat.domain.episode.dto.PostActivityTag;
import com.example.daycarat.domain.episode.entity.ActivityTag;
import com.example.daycarat.domain.episode.repository.ActivityTagRepository;
import com.example.daycarat.domain.user.domain.User;
import com.example.daycarat.domain.user.repository.UserRepository;
import com.example.daycarat.global.error.exception.CustomException;
import com.example.daycarat.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class EpisodeActivityTagService {

    private final UserRepository userRepository;
    private final ActivityTagRepository activityTagRepository;

    public void createActivityTag(PostActivityTag postActivityTag) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        ActivityTag activityTag = ActivityTag.of(user, postActivityTag.activityTag());

        activityTagRepository.save(activityTag);

    }

}
