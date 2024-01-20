package com.example.daycarat.domain.gereratedcontent.dto;

public record PostGeneratedContent(
        Long episodeId,
        int keywordId,
        String s3ObjectKey,
        String generatedContent1,
        String generatedContent2,
        String generatedContent3
) {
}
