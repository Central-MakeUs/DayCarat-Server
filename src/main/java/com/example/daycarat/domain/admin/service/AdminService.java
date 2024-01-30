package com.example.daycarat.domain.admin.service;

import com.example.daycarat.domain.admin.dto.PostFcmToken;
import com.example.daycarat.domain.announcement.service.AnnouncementService;
import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.domain.episode.entity.EpisodeContent;
import com.example.daycarat.domain.episode.repository.EpisodeContentRepository;
import com.example.daycarat.domain.episode.repository.EpisodeRepository;
import com.example.daycarat.domain.fcmtoken.entity.UserFcmTokenInfo;
import com.example.daycarat.domain.fcmtoken.service.UserFcmTokenInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class AdminService {

    private final EpisodeRepository episodeRepository;
    private final EpisodeContentRepository episodeContentRepository;
    private final UserFcmTokenInfoService userFcmTokenInfoService;
    private final AnnouncementService announcementService;

    @Transactional
    public Boolean setMainContentsOnEpisodeContents() {

        List<Episode> episodeList = episodeRepository.findAllByIsDeleted(false);

        for (Episode episode : episodeList) {
            List<EpisodeContent> episodeContents = episodeContentRepository.findByEpisodeIdAndIsDeleted(episode.getId(), false);

            boolean isMain = true;

            for (EpisodeContent episodeContent : episodeContents) {
                episodeContent.setIsMainContent(isMain);
                isMain = false;
            }
        }

        return true;
    }

    public UserFcmTokenInfo getFcmTokenByUserId(Long userId) {
        return userFcmTokenInfoService.getFcmToken(userId);
    }

    public Boolean saveFcmToken(PostFcmToken postFcmToken) {
        userFcmTokenInfoService.save(postFcmToken.userId(), postFcmToken.fcmToken());
        return true;
    }

    public Boolean saveOrUpdateFcmToken(PostFcmToken postFcmToken) {
        userFcmTokenInfoService.saveOrUpdate(postFcmToken.userId(), postFcmToken.fcmToken());
        return true;
    }

    public Boolean deleteFcmTokenByUserId(Long userId) {
        userFcmTokenInfoService.delete(userId);
        return true;
    }
}
