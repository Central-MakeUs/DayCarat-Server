package com.example.daycarat.domain.gereratedcontent.service;

import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.domain.episode.entity.EpisodeKeyword;
import com.example.daycarat.domain.episode.repository.EpisodeRepository;
import com.example.daycarat.domain.episode.validator.EpisodeValidator;
import com.example.daycarat.domain.gem.repository.GemRepository;
import com.example.daycarat.domain.gereratedcontent.dto.PostGeneratedContent;
import com.example.daycarat.domain.gereratedcontent.entity.GeneratedContent;
import com.example.daycarat.domain.gereratedcontent.repository.GeneratedContentRepository;
import com.example.daycarat.global.error.exception.CustomException;
import com.example.daycarat.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class GeneratedContentService {

    private final GeneratedContentRepository generatedContentRepository;
    private final EpisodeRepository episodeRepository;
    private final GemRepository gemRepository;

    public void checkIfGeneratedContentExists(Long episodeId) {
        generatedContentRepository.findByEpisodeIdAndIsDeleted(episodeId, false)
                .orElseThrow(() -> new CustomException(ErrorCode.GENERATED_CONTENT_NOT_FOUND));
    }

    @Transactional
    public Boolean registerGeneratedContent(PostGeneratedContent postGeneratedContent) {

        Episode episode = episodeRepository.findById(postGeneratedContent.episodeId())
                .orElseThrow(() -> new CustomException(ErrorCode.EPISODE_NOT_FOUND));

        EpisodeValidator.checkIfDeleted(episode);

        gemRepository.findByS3ObjectKeyAndIsDeleted(postGeneratedContent.s3ObjectKey(), false)
                .orElseThrow(() -> new CustomException(ErrorCode.GEM_NOT_FOUND));

        // 유저가 키워드를 직접 선택하지 않은 경우에만 키워드 등록
        if (!episode.getIsEpisodeKeywordUserSelected())
            episode.updateKeyword(EpisodeKeyword.fromId(postGeneratedContent.keywordId()));

        episodeRepository.save(episode);

        // 중복 생성의 경우 이전 데이터 삭제 처리 후 새로 생성
        generatedContentRepository.findByEpisodeIdAndIsDeleted(episode.getId(), false)
                .ifPresent(generatedContent -> {
                    generatedContent.delete();
                    generatedContentRepository.save(generatedContent);
                });

        GeneratedContent generatedContent = GeneratedContent.of(
                episode,
                postGeneratedContent.generatedContent1(),
                postGeneratedContent.generatedContent2(),
                postGeneratedContent.generatedContent3()
        );

        generatedContentRepository.save(generatedContent);

        return true;

    }

    public void deleteGeneratedContent(Long episodeId) {
        List<GeneratedContent> generatedContentList = generatedContentRepository.findAllByEpisodeIdAndIsDeleted(episodeId, false);

        for (GeneratedContent generatedContent : generatedContentList) {
            generatedContent.delete();
        }

        generatedContentRepository.saveAll(generatedContentList);

    }
}
