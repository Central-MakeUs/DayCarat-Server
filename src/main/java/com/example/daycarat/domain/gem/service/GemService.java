package com.example.daycarat.domain.gem.service;

import com.example.daycarat.domain.activity.entity.ActivityTag;
import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.domain.episode.entity.EpisodeKeyword;
import com.example.daycarat.domain.episode.entity.EpisodeState;
import com.example.daycarat.domain.episode.repository.EpisodeRepository;
import com.example.daycarat.domain.episode.validator.EpisodeValidator;
import com.example.daycarat.domain.gem.dto.*;
import com.example.daycarat.domain.gem.entity.Gem;
import com.example.daycarat.domain.gem.repository.GemRepository;
import com.example.daycarat.domain.gem.validator.GemValidator;
import com.example.daycarat.domain.gereratedcontent.dto.GetGeneratedContent;
import com.example.daycarat.domain.gereratedcontent.entity.GeneratedContent;
import com.example.daycarat.domain.gereratedcontent.repository.GeneratedContentRepository;
import com.example.daycarat.domain.gereratedcontent.service.GeneratedContentService;
import com.example.daycarat.domain.user.domain.User;
import com.example.daycarat.domain.user.repository.UserRepository;
import com.example.daycarat.global.aws.S3UploadService;
import com.example.daycarat.global.error.exception.CustomException;
import com.example.daycarat.global.error.exception.ErrorCode;
import com.example.daycarat.global.util.StringParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service @RequiredArgsConstructor
public class GemService {

    private final GemRepository gemRepository;
    private final UserRepository userRepository;
    private final EpisodeRepository episodeRepository;
    private final S3UploadService s3UploadService;
    private final GeneratedContentService generatedContentService;
    private final GeneratedContentRepository generatedContentRepository;

    private void uploadJsonFile(User user, Episode episode, PostSoara gem, String s3ObjectKey) {

        try {
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

            // user, episodeContents, postGem을 JSON으로 변환
            String userJson = objectMapper.writeValueAsString(user);
            String episodeContentsJson = objectMapper.writeValueAsString(episode.getEpisodeContents());
            String postGemJson = objectMapper.writeValueAsString(gem);

            // 각각의 JSON을 하나의 Map에 저장
            Map<String, String> jsonMap = new HashMap<>();
            jsonMap.put("user", userJson);
            jsonMap.put("episodeContents", episodeContentsJson);
            jsonMap.put("postGem", postGemJson);

            // 최종적으로 이 Map을 다시 JSON으로 변환
            String finalJson = objectMapper.writeValueAsString(jsonMap);

            // 최종 JSON을 파일로 저장
            s3UploadService.saveJsonFileContent(episode.getId().toString(), s3ObjectKey, finalJson);

        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.JSON_FILE_UPLOAD_FAILED);
        }
    }

    @Transactional
    public Boolean createGem(PostGem postGem) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Episode episode = episodeRepository.findById(postGem.episodeId())
                .orElseThrow(() -> new CustomException(ErrorCode.EPISODE_NOT_FOUND));

        EpisodeValidator.checkIfDeleted(episode);
        EpisodeValidator.checkIfUnfinalized(episode);
        EpisodeValidator.checkIfUserEpisodeMatches(user, episode);

        String s3ObjectKey = LocalDateTime.now().toString();

        Gem gem = gemRepository.findByEpisodeIdAndUserId(postGem.episodeId(), user.getId())
                        .orElseThrow(() -> new CustomException(ErrorCode.GEM_NOT_FOUND));

        gem.updateS3ObjectKey(s3ObjectKey);

        gemRepository.save(gem);

        episode.updateState(EpisodeState.FINALIZED);

        PostSoara postSoara = new PostSoara(
                episode.getId(),
                gem.getContent1(),
                gem.getContent2(),
                gem.getContent3(),
                gem.getContent4(),
                gem.getContent5());

        uploadJsonFile(user, episode, postSoara, s3ObjectKey);

        return true;

    }

    public Boolean deleteGem(Long gemId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Gem gem = gemRepository.findById(gemId)
                .orElseThrow(() -> new CustomException(ErrorCode.GEM_NOT_FOUND));

        GemValidator.checkIfGemExists(gem);

        Episode episode = episodeRepository.findByGemId(gemId)
                .orElseThrow(() -> new CustomException(ErrorCode.EPISODE_NOT_FOUND));

        EpisodeValidator.checkIfUserEpisodeMatches(user, episode);

        episode.initEpisodeKeyword();
        episodeRepository.save(episode);

        gem.delete();

        gemRepository.save(gem);

        generatedContentService.deleteGeneratedContent(episode.getId());

        return true;
    }

    @Transactional
    public Boolean updateGem(PatchGem patchGem) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Gem gem = gemRepository.findById(patchGem.gemId())
                .orElseThrow(() -> new CustomException(ErrorCode.GEM_NOT_FOUND));

        Episode episode = episodeRepository.findByGemId(patchGem.gemId())
                .orElseThrow(() -> new CustomException(ErrorCode.EPISODE_NOT_FOUND));

        EpisodeValidator.checkIfUserEpisodeMatches(user, episode);

        episode.initEpisodeKeyword();
        episodeRepository.save(episode);

        String s3ObjectKey = LocalDateTime.now().toString();

        gem.updateS3ObjectKey(s3ObjectKey);

        gemRepository.save(gem);

        PostSoara postSoara = new PostSoara(
                episode.getId(),
                gem.getContent1(),
                gem.getContent2(),
                gem.getContent3(),
                gem.getContent4(),
                gem.getContent5()
        );

        uploadJsonFile(user, episode, postSoara, s3ObjectKey);

        generatedContentService.deleteGeneratedContent(episode.getId());

        return true;

    }

    public GetGeneratedContent getRecommend(Long episodeId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new CustomException(ErrorCode.EPISODE_NOT_FOUND));

        EpisodeValidator.checkIfUserEpisodeMatches(user, episode);

        Gem gem = gemRepository.findByEpisodeIdAndIsDeleted(episodeId, false)
                .orElseThrow(() -> new CustomException(ErrorCode.GEM_NOT_FOUND));

        LocalDateTime createdDate = gem.getLastModifiedDate();

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdDate, now);

        GeneratedContent generatedContent = generatedContentRepository.findByEpisodeIdAndIsDeleted(episodeId, false)
                .orElseThrow(() -> {
                    if (duration.toMinutes() >= 2) {
                        return new CustomException(ErrorCode.AI_RECOMMENDATION_FAILED);
                    }
                    return new CustomException(ErrorCode.AI_RECOMMENDATION_NOT_FOUND);
                });

        return new GetGeneratedContent(
                generatedContent.getId(),
                generatedContent.getGeneratedContent1(),
                generatedContent.getGeneratedContent2(),
                generatedContent.getGeneratedContent3()
        );

    }

    public GetGemCount getGemCount() {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return gemRepository.getGemCount(user.getId());
    }

    public GetGemCount getGemCountByMonth() {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return gemRepository.getGemCountByMonth(user.getId());
    }

    public GetGemSummaryByKeyword getGemSummaryByKeyword() {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<GetGemSummaryByKeywordDto> getGemSummaryByKeywordDtoList = gemRepository.getGemSummaryByKeyword(user.getId());

        GetGemSummaryByKeyword getGemSummaryByKeyword = new GetGemSummaryByKeyword();

        for (GetGemSummaryByKeywordDto getGemSummaryByKeywordDto : getGemSummaryByKeywordDtoList) {

            switch (getGemSummaryByKeywordDto.episodeKeyword()) {
                case COMMUNICATION -> getGemSummaryByKeyword.setCommunication(getGemSummaryByKeywordDto.count());
                case CONFLICT_RESOLUTION -> getGemSummaryByKeyword.setConflictResolution(getGemSummaryByKeywordDto.count());
                case PASSION -> getGemSummaryByKeyword.setPassion(getGemSummaryByKeywordDto.count());
                case DILIGENCE -> getGemSummaryByKeyword.setDiligence(getGemSummaryByKeywordDto.count());
                case COLLABORATION -> getGemSummaryByKeyword.setCollaboration(getGemSummaryByKeywordDto.count());
                case LEADERSHIP -> getGemSummaryByKeyword.setLeadership(getGemSummaryByKeywordDto.count());
                case FEEDBACK -> getGemSummaryByKeyword.setFeedback(getGemSummaryByKeywordDto.count());
                case UNSET -> getGemSummaryByKeyword.setUnset(getGemSummaryByKeywordDto.count());
            }

        }

        getGemSummaryByKeyword.handleNull();
        return getGemSummaryByKeyword;

    }

    public List<GetGemPageByKeyword> getGemPageByKeyword(String keyword, Long cursorId, Integer pageSize) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (pageSize == null) pageSize = 6;

        return episodeRepository.getEpisodePageByKeyword(user, EpisodeKeyword.fromValue(keyword), cursorId, pageSize)
                .stream()
                .map(GetGemPageByKeywordDto::convert)
                .toList();

    }

    public GetMostGemKeyword getMostGemKeyword() {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        EpisodeKeyword episodeKeyword = gemRepository.getMostGemKeyword(user.getId());

        if (episodeKeyword == null) {
            return new GetMostGemKeyword("보석 없음");
        }

        return new GetMostGemKeyword(episodeKeyword.getValue());
    }

    public GetMostGemActivity getMostGemActivity() {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        ActivityTag activityTag = gemRepository.getMostGemActivity(user.getId());

        if (activityTag == null) {
            return new GetMostGemActivity("보석 없음");
        }

        return new GetMostGemActivity(activityTag.getActivityTagName());
    }


    public GetEpisodeClipboard getEpisodeClipboard(Long episodeId) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        GetEpisodeClipboardDto getEpisodeClipboardDto = gemRepository.getEpisodeClipboard(user.getId(), episodeId)
                .orElseThrow(() -> new CustomException(ErrorCode.EPISODE_NOT_FOUND));

        return new GetEpisodeClipboard(StringParser.getClipboard(getEpisodeClipboardDto));

    }

    public Boolean updateSoara(PostSoara postSoara) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Gem gem = gemRepository.findByEpisodeIdAndUserId(postSoara.episodeId(), user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.GEM_NOT_FOUND));

        gem.updateEach(postSoara.content1(), postSoara.content2(), postSoara.content3(), postSoara.content4(), postSoara.content5());

        gemRepository.save(gem);

        return true;

    }

    public GetGem getGem(Long episodeId) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Gem gem = gemRepository.findByEpisodeIdAndUserId(episodeId, user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.GEM_NOT_FOUND));

        return new GetGem(gem.getId(), gem.getContent1(), gem.getContent2(), gem.getContent3(), gem.getContent4(), gem.getContent5());
    }
}
