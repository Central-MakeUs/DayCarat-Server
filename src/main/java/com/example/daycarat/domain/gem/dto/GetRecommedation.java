package com.example.daycarat.domain.gem.dto;

import java.util.List;

public record GetRecommedation(
        List<String> keywordIds,
        String suggestedContent1,
        String suggestedContent2,
        String suggestedContent3
) {
}
