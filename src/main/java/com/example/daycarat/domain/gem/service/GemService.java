package com.example.daycarat.domain.gem.service;

import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.domain.episode.repository.EpisodeRepository;
import com.example.daycarat.domain.episode.validator.EpisodeValidator;
import com.example.daycarat.domain.gem.dto.PostGem;
import com.example.daycarat.domain.gem.entity.Gem;
import com.example.daycarat.domain.gem.repository.GemRepository;
import com.example.daycarat.domain.user.domain.User;
import com.example.daycarat.domain.user.repository.UserRepository;
import com.example.daycarat.global.aws.S3UploadService;
import com.example.daycarat.global.error.exception.CustomException;
import com.example.daycarat.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class GemService {

    private final GemRepository gemRepository;
    private final UserRepository userRepository;
    private final EpisodeRepository episodeRepository;
    private final S3UploadService s3UploadService;

    @Transactional
    public Boolean createGem(PostGem postGem) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Episode episode = episodeRepository.findById(postGem.episodeId())
                .orElseThrow(() -> new CustomException(ErrorCode.EPISODE_NOT_FOUND));

        EpisodeValidator.checkIfDeleted(episode);
        EpisodeValidator.checkIfUnfinalized(episode);
        EpisodeValidator.checkIfUserEpisodeMatches(user, episode);

        gemRepository.save(postGem.toEntity(episode));

        episode.makeFinalized();

        // TODO : Upload data to S3

        return true;

    }

    public Boolean deleteGem(Long gemId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Gem gem = gemRepository.findById(gemId)
                .orElseThrow(() -> new CustomException(ErrorCode.GEM_NOT_FOUND));

        Episode episode = episodeRepository.findByGemId(gemId)
                .orElseThrow(() -> new CustomException(ErrorCode.EPISODE_NOT_FOUND));

        EpisodeValidator.checkIfUserEpisodeMatches(user, episode);

        gem.delete();

        gemRepository.save(gem);

        return true;
    }
}
