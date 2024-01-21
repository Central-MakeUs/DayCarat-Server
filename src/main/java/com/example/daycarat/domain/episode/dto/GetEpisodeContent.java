package com.example.daycarat.domain.episode.dto;

import com.example.daycarat.domain.episode.entity.EpisodeContent;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record GetEpisodeContent(
        @Schema(description = "내용 ID", example = "1") Long episodeContentId,
        @Schema(description = "작성 항목", example = "배울 점") String episodeContentType,
        @Schema(description = "내용", example = "팀원 간의 소통에 있어 부족한 부분이 많이 보여 PM으로서 이를 해결하는 방법에 대한 고민이 많았는데, 1:1 대화를 도입함으로써 그동안의 소통 문제를 해결할 수 있었다.") String content
) {

    public static GetEpisodeContent of(Long episodeContentId, String episodeContentType, String content) {
        return new GetEpisodeContent(episodeContentId, episodeContentType, content);
    }

    public static List<GetEpisodeContent> listOf(List<EpisodeContent> episodeContents) {
        return episodeContents.stream()
                .filter(episodeContent -> !episodeContent.getIsDeleted())
                .map(episodeContent -> new GetEpisodeContent(episodeContent.getId(), episodeContent.getEpisodeContentType().getValue(), episodeContent.getContent()))
                .toList();
    }
}
