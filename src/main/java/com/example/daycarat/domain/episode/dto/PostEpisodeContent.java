package com.example.daycarat.domain.episode.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record PostEpisodeContent (
        @Schema(description = "작성 항목", example = "자유롭게 작성") String episodeContentType,
        @Schema(description = "내용", example = "데이캐럿 화이팅") String content){
}
