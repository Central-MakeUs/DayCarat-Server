package com.example.daycarat.domain.gem.api;

import com.example.daycarat.domain.gem.dto.GetGemCount;
import com.example.daycarat.domain.gem.dto.GetRecommedation;
import com.example.daycarat.domain.gem.dto.PatchGem;
import com.example.daycarat.domain.gem.dto.PostGem;
import com.example.daycarat.domain.gem.service.GemService;
import com.example.daycarat.global.error.ErrorResponse;
import com.example.daycarat.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

  
    @Operation(summary = "AI 추천 키워드 및 문장 조회하기",
            description = """
                    보석 ID를 받아서 해당 보석에 대한 AI 추천 키워드 및 문장을 조회합니다.
                    
                    상태 코드:
                    - 200 : 조회 성공
                    - 404 : AI 추천 키워드 생성중
                    - 500 : 서버 에러 (AI 추천 키워드 생성 실패)
                    
                    반환값:
                    - keywords[] : AI 추천 키워드 (배열) : 키워드가 없을 수 있음
                    - suggestedContent1: AI 추천 문장 1
                    - suggestedContent2: AI 추천 문장 2
                    - suggestedContent3: AI 추천 문장 3
                    
                    """)
    @GetMapping("/recommend/{gemId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "AI 추천 키워드 생성중"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                                        , schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러 (AI 추천 키워드 생성 실패)"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                                        , schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SuccessResponse<GetRecommedation> getRecommend(@PathVariable Long gemId) {
        return SuccessResponse.success(gemService.getRecommend(gemId));
    }

    @Operation(summary = "보석 개수 조회하기",
            description = """
                    보석 개수를 조회합니다.
                    
                    반환값:
                    - gemCount: 보석 개수
                    
                    """)
    @GetMapping("/count")
    public SuccessResponse<GetGemCount> getGemCount() {
        return SuccessResponse.success(gemService.getGemCount());
    }



}
