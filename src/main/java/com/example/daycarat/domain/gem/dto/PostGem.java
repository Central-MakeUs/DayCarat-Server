package com.example.daycarat.domain.gem.dto;

import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.domain.gem.entity.Gem;
import io.swagger.v3.oas.annotations.media.Schema;

public record PostGem(
        @Schema(description = "에피소드 ID", example = "1") Long episodeId,
        @Schema(description = "내용1", example = "팀원 간의 소통에 있어 부족한 부분이 많이 보여 PM으로서 이를 해결하는 방법에 대한 고민이 많았는데, 1:1 대화를 도입함으로써 그동안의 소통 문제를 해결할 수 있었다. ") String content1,
        @Schema(description = "내용2", example = "팀원 간의 소통에 있어 부족한 부분이 많이 보여 PM으로서 이를 해결하는 방법에 대한 고민이 많았는데, 1:1 대화를 도입함으로써 그동안의 소통 문제를 해결할 수 있었다. ") String content2,
        @Schema(description = "내용3", example = "팀원 간의 소통에 있어 부족한 부분이 많이 보여 PM으로서 이를 해결하는 방법에 대한 고민이 많았는데, 1:1 대화를 도입함으로써 그동안의 소통 문제를 해결할 수 있었다. ") String content3,
        @Schema(description = "내용4", example = "팀원 간의 소통에 있어 부족한 부분이 많이 보여 PM으로서 이를 해결하는 방법에 대한 고민이 많았는데, 1:1 대화를 도입함으로써 그동안의 소통 문제를 해결할 수 있었다. ") String content4,
        @Schema(description = "내용5", example = "팀원 간의 소통에 있어 부족한 부분이 많이 보여 PM으로서 이를 해결하는 방법에 대한 고민이 많았는데, 1:1 대화를 도입함으로써 그동안의 소통 문제를 해결할 수 있었다. ") String content5
) {
    public Gem toEntity(PostGem postGem, Episode episode) {
        return Gem.of(episode, content1, content2, content3, content4, content5);
    }
}
