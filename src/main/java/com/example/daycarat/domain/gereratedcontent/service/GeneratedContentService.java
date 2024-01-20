package com.example.daycarat.domain.gereratedcontent.service;

import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.domain.episode.repository.EpisodeRepository;
import com.example.daycarat.domain.gereratedcontent.dto.PostGeneratedContent;
import com.example.daycarat.domain.gereratedcontent.entity.GeneratedContent;
import com.example.daycarat.domain.gereratedcontent.entity.Keyword;
import com.example.daycarat.domain.gereratedcontent.repository.GeneratedContentRepository;
import com.example.daycarat.global.error.exception.CustomException;
import com.example.daycarat.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class GeneratedContentService {

    private final GeneratedContentRepository generatedContentRepository;
    private final EpisodeRepository episodeRepository;

    public Boolean registerGeneratedContent(PostGeneratedContent postGeneratedContent) {

        Episode episode = episodeRepository.findById(postGeneratedContent.episodeId())
                .orElseThrow(() -> new CustomException(ErrorCode.EPISODE_NOT_FOUND));

        GeneratedContent generatedContent = GeneratedContent.of(
                episode,
                Keyword.fromId(postGeneratedContent.keywordId()),
                postGeneratedContent.generatedContent1(),
                postGeneratedContent.generatedContent2(),
                postGeneratedContent.generatedContent3()
        );

        generatedContentRepository.save(generatedContent);

        return true;

    }
}
