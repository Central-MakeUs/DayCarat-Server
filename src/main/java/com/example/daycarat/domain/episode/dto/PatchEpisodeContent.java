package com.example.daycarat.domain.episode.dto;

import com.example.daycarat.domain.episode.entity.EpisodeContentType;
import io.swagger.v3.oas.annotations.media.Schema;

public record PatchEpisodeContent (
        @Schema(description = "내용 ID", example = "1") Long episodeContentId,
        @Schema(description = "작성 항목", example = "자유롭게 작성") EpisodeContentType episodeContentType,
        @Schema(description = "내용", example = "데이캐럿 화이팅") String content){
}
