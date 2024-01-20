package com.example.daycarat.domain.admin.service;

import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.domain.episode.entity.EpisodeContent;
import com.example.daycarat.domain.episode.repository.EpisodeContentRepository;
import com.example.daycarat.domain.episode.repository.EpisodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class AdminService {

    private final EpisodeRepository episodeRepository;
    private final EpisodeContentRepository episodeContentRepository;

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

}
