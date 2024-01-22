package com.example.daycarat.domain.gem.repository;

import com.example.daycarat.domain.gem.dto.GetGemCount;
import com.example.daycarat.domain.gem.dto.GetGemSummaryByKeywordDto;

import java.util.List;

public interface GemRepositoryCustom {
    GetGemCount getGemCount(Long userId);

    List<GetGemSummaryByKeywordDto> getGemSummaryByKeyword(Long userId);
}
