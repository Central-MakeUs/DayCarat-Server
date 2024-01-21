package com.example.daycarat.domain.episode.dto;

import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.domain.episode.entity.EpisodeState;
import com.example.daycarat.domain.gem.dto.GetGem;
import com.example.daycarat.domain.gereratedcontent.dto.GetGeneratedContent;
import com.example.daycarat.global.util.LocalDateTimeParser;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record GetEpisodeDetail(
        @Schema(description = "에피소드 ID", example = "1") Long episodeId,
        @Schema(description = "제목", example = "팀플 회의") String title,
        @Schema(description = "활동태그명", example = "CMC") String activityTagName,
        @Schema(description = "선택 날짜", example = "2024년 01월 09일 화요일") String selectedDate,
        @Schema(description = "보석 다듬기 여부", example = "UNFINALIZED") EpisodeState episodeState,
        @Schema(description = "에피소드 키워드", example = "커뮤니케이션") String episodeKeyword,
        List<GetEpisodeContent> episodeContents,
        GetGeneratedContent generatedContents,
        GetGem gem
) {
    public static GetEpisodeDetail of(Long episodeId, String title, String activityTagName, String selectedDate, String episodeKeyword, EpisodeState episodeState, List<GetEpisodeContent> episodeContents, GetGeneratedContent generatedContents, GetGem gem) {
        return new GetEpisodeDetail(episodeId, title, activityTagName, selectedDate, episodeState, episodeKeyword, episodeContents, generatedContents, gem);
    }

    public static GetEpisodeDetail of(Episode episode) {
        return GetEpisodeDetail.of(
                episode.getId(),
                episode.getTitle(),
                episode.getActivityTag().getActivityTagName(),
                LocalDateTimeParser.toStringWithDetail(episode.getSelectedDate()),
                episode.getEpisodeKeyword() == null ? null : episode.getEpisodeKeyword().getValue(),
                episode.getEpisodeState(),
                GetEpisodeContent.listOf(episode.getEpisodeContents()),
                GetGeneratedContent.of(episode.getGeneratedContents()),
                GetGem.of(episode.getGem())
        );
    }
}
