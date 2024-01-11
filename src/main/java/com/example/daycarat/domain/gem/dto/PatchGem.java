package com.example.daycarat.domain.gem.dto;

import com.example.daycarat.domain.gem.entity.AppealPoint;
import io.swagger.v3.oas.annotations.media.Schema;

public record PatchGem (
        @Schema(description = "보석 ID", example = "1") Long gemId,
        @Schema(description = "어필포인트")AppealPoint appealPoint,
        @Schema(description = "내용1", example = "팀원 간의 소통에 있어 부족한 부분이 많이 보여 PM으로서 이를 해결하는 방법에 대한 고민이 많았는데, 1:1 대화를 도입함으로써 그동안의 소통 문제를 해결할 수 있었다. ") String content1,
        @Schema(description = "내용2", example = "팀원 간의 소통에 있어 부족한 부분이 많이 보여 PM으로서 이를 해결하는 방법에 대한 고민이 많았는데, 1:1 대화를 도입함으로써 그동안의 소통 문제를 해결할 수 있었다. ") String content2,
        @Schema(description = "내용3", example = "팀원 간의 소통에 있어 부족한 부분이 많이 보여 PM으로서 이를 해결하는 방법에 대한 고민이 많았는데, 1:1 대화를 도입함으로써 그동안의 소통 문제를 해결할 수 있었다. ") String content3
) {
}
