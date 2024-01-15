package com.example.daycarat.domain.gem.service;

import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.domain.episode.entity.EpisodeState;
import com.example.daycarat.domain.episode.repository.EpisodeRepository;
import com.example.daycarat.domain.episode.validator.EpisodeValidator;
import com.example.daycarat.domain.gem.dto.PatchGem;
import com.example.daycarat.domain.gem.dto.PostGem;
import com.example.daycarat.domain.gem.entity.Gem;
import com.example.daycarat.domain.gem.repository.GemRepository;
import com.example.daycarat.domain.user.domain.User;
import com.example.daycarat.domain.user.repository.UserRepository;
import com.example.daycarat.global.aws.S3UploadService;
import com.example.daycarat.global.error.exception.CustomException;
import com.example.daycarat.global.error.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

        String s3ObjectName = LocalDateTime.now().toString();

        gemRepository.save(postGem.toEntity(episode, s3ObjectName));

        episode.updateState(EpisodeState.FINALIZED);


        try {
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

            // user, episodeContents, postGem을 JSON으로 변환
            String userJson = objectMapper.writeValueAsString(user);
            String episodeContentsJson = objectMapper.writeValueAsString(episode.getEpisodeContents());
            String postGemJson = objectMapper.writeValueAsString(postGem);

            // 각각의 JSON을 하나의 Map에 저장
            Map<String, String> jsonMap = new HashMap<>();
            jsonMap.put("user", userJson);
            jsonMap.put("episodeContents", episodeContentsJson);
            jsonMap.put("postGem", postGemJson);

            // 최종적으로 이 Map을 다시 JSON으로 변환
            String finalJson = objectMapper.writeValueAsString(jsonMap);

            // 최종 JSON을 파일로 저장
            s3UploadService.saveJsonFileContent(episode.getId().toString(), s3ObjectName, finalJson);

        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.JSON_FILE_UPLOAD_FAILED);
        }

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

        episode.updateState(EpisodeState.UNFINALIZED);

        episodeRepository.save(episode);

        gem.delete();

        gemRepository.save(gem);

        return true;
    }

    public Boolean updateGem(PatchGem patchGem) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Gem gem = gemRepository.findById(patchGem.gemId())
                .orElseThrow(() -> new CustomException(ErrorCode.GEM_NOT_FOUND));

        Episode episode = episodeRepository.findByGemId(patchGem.gemId())
                .orElseThrow(() -> new CustomException(ErrorCode.EPISODE_NOT_FOUND));

        EpisodeValidator.checkIfUserEpisodeMatches(user, episode);

        String s3ObjectName = LocalDateTime.now().toString();

        gem.update(patchGem.appealPoint(), patchGem.content1(), patchGem.content2(), patchGem.content3(), s3ObjectName);

        return true;

    }
}
