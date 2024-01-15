package com.example.daycarat.domain.gem.api;

import com.example.daycarat.domain.gem.dto.GetRecommedation;
import com.example.daycarat.domain.gem.dto.PatchGem;
import com.example.daycarat.domain.gem.dto.PostGem;
import com.example.daycarat.domain.gem.service.GemService;
import com.example.daycarat.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Gem", description = "보석 관련 API")
@RestController @RequestMapping("/gem") @RequiredArgsConstructor
public class GemController {

    private final GemService gemService;


    @Operation(summary = "보석 등록하기")
    @PostMapping("/register")
    public SuccessResponse<Boolean> createGem(@RequestBody PostGem postGem) {
        return SuccessResponse.success(gemService.createGem(postGem));
    }

    @Operation(summary = "보석 수정하기", description = "보내지 않은 데이터에 대해서는 기존 값을 유지합니다.")
    @PatchMapping("/update")
    public SuccessResponse<Boolean> updateGem(@RequestBody PatchGem patchGem) {
        return SuccessResponse.success(gemService.updateGem(patchGem));
    }


    @Operation(summary = "보석 삭제하기")
    @DeleteMapping("/delete/{gemId}")
    public SuccessResponse<Boolean> deleteGem(@PathVariable Long gemId) {
        return SuccessResponse.success(gemService.deleteGem(gemId));
    }

    @Operation(summary = "AI 추천 키워드 및 문장 조회하기")
    @GetMapping("/recommend/{gemId}")
    public SuccessResponse<GetRecommedation> getRecommend(@PathVariable Long gemId) {
        return SuccessResponse.success(gemService.getRecommend(gemId));
    }





}
