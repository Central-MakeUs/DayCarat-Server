package com.example.daycarat.domain.episode.dto;

import com.example.daycarat.domain.episode.entity.EpisodeState;
import io.swagger.v3.oas.annotations.media.Schema;

public record GetEpisodePage(
        @Schema(description = "에피소드 ID", example = "1") Long id,
        @Schema(description = "에피소드 제목", example = "기획 회의") String title,
        @Schema(description = "선택 날짜", example = "12/28") String date,
        @Schema(description = "보석 다듬기 여부", example = "UNFINALIZED") EpisodeState episodeState,
        @Schema(description = "내용", example = "배운 점 / 팀원 간의 소통에 있어 부족한 부분이 많이 보여 PM으로서 이를 해결하는 방법에 대한 ...") String content
) {
}
