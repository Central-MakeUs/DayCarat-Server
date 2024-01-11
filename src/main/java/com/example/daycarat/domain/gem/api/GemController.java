package com.example.daycarat.domain.gem.api;

import com.example.daycarat.domain.gem.dto.PatchGem;
import com.example.daycarat.domain.gem.dto.PostGem;
import com.example.daycarat.domain.gem.service.GemService;
import com.example.daycarat.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/gem") @RequiredArgsConstructor
public class GemController {

    private final GemService gemService;


    @Operation(summary = "보석 등록하기")
    @PostMapping("/register")
    public SuccessResponse<Boolean> createGem(@RequestBody PostGem postGem) {
        return SuccessResponse.createSuccess(gemService.createGem(postGem));
    }

    @Operation(summary = "보석 수정하기", description = "보내지 않은 데이터에 대해서는 기존 값을 유지합니다.")
    @PatchMapping("/update")
    public SuccessResponse<Boolean> updateGem(@RequestBody PatchGem patchGem) {
        return SuccessResponse.updateSuccess(gemService.updateGem(patchGem));
    }


    @Operation(summary = "보석 삭제하기")
    @DeleteMapping("/delete/{gemId}")
    public SuccessResponse<Boolean> deleteGem(@PathVariable Long gemId) {
        return SuccessResponse.deleteSuccess(gemService.deleteGem(gemId));
    }



}
