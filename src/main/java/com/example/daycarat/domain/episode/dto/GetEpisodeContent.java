package com.example.daycarat.domain.episode.dto;

import com.example.daycarat.domain.episode.entity.EpisodeContentType;
import io.swagger.v3.oas.annotations.media.Schema;

public record GetEpisodeContent(
        @Schema(description = "작성 항목", example = "배울 점") EpisodeContentType episodeContentType,
        @Schema(description = "내용", example = "팀원 간의 소통에 있어 부족한 부분이 많이 보여 PM으로서 이를 해결하는 방법에 대한 고민이 많았는데, 1:1 대화를 도입함으로써 그동안의 소통 문제를 해결할 수 있었다.") String content
) {

    public static GetEpisodeContent of(EpisodeContentType episodeContentType, String content) {
        return new GetEpisodeContent(episodeContentType, content);
    }
}