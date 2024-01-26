package com.example.daycarat.domain.user.service;

import com.example.daycarat.domain.activity.repository.ActivityTagRepository;
import com.example.daycarat.domain.episode.repository.EpisodeRepository;
import com.example.daycarat.domain.fcmtoken.service.UserFcmTokenInfoService;
import com.example.daycarat.domain.user.dto.GetUserInfo;
import com.example.daycarat.domain.user.dto.PatchUserInfo;
import com.example.daycarat.domain.user.entity.User;
import com.example.daycarat.domain.user.repository.UserRepository;
import com.example.daycarat.global.aws.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service @RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final S3UploadService s3UploadService;
    private final EpisodeRepository episodeRepository;
    private final ActivityTagRepository activityTagRepository;
    private final UserFcmTokenInfoService userFcmTokenInfoService;

    public GetUserInfo getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .map(GetUserInfo::of)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

    }

    public Boolean patchUserInfo(PatchUserInfo patchUserInfo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        user.update(patchUserInfo);

        userRepository.save(user);

        userFcmTokenInfoService.saveOrUpdate(user.getId(), patchUserInfo.fcmToken());

        return true;

    }

    public Boolean registerProfile(MultipartFile profileImage) throws IOException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        String profile = s3UploadService.saveFile(profileImage, "profile");

        user.updateProfile(profile);

        userRepository.save(user);

        return true;

    }

    @Transactional
    public Boolean deleteUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        episodeRepository.deleteAllByUser(user);
        activityTagRepository.deleteAllByUser(user);
        userRepository.delete(user);

        return true;
    }
}
