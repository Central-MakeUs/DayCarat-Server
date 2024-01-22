package com.example.daycarat.domain.gem.repository;

import com.example.daycarat.domain.gem.dto.GetGemCount;

public interface GemRepositoryCustom {
    GetGemCount getGemCount(Long userId);
}
