package com.example.daycarat.domain.gereratedcontent.dto;

import com.example.daycarat.domain.gereratedcontent.entity.GeneratedContent;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record GetGeneratedContent(
        @Schema(description = "AI 생성 컨텐츠 ID", example = "1") Long generatedContentId,
        @Schema(description = "AI 추천 문장 1") String generatedContent1,
        @Schema(description = "AI 추천 문장 2") String generatedContent2,
        @Schema(description = "AI 추천 문장 3") String generatedContent3
) {
    public static GetGeneratedContent of(List<GeneratedContent> generatedContents) {
        return generatedContents.stream()
                .filter(generatedContent -> !generatedContent.getIsDeleted())
                .map(generatedContent -> new GetGeneratedContent(generatedContent.getId(), generatedContent.getGeneratedContent1(), generatedContent.getGeneratedContent2(), generatedContent.getGeneratedContent3()))
                .findFirst()
                .orElse(null);
    }
}
