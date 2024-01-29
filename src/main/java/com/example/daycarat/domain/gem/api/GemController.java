package com.example.daycarat.domain.gem.api;

import com.example.daycarat.domain.gem.dto.GetEpisodeClipboard;
import com.example.daycarat.domain.gem.dto.*;
import com.example.daycarat.domain.gem.service.GemService;
import com.example.daycarat.domain.gereratedcontent.dto.GetGeneratedContent;
import com.example.daycarat.global.error.ErrorResponse;
import com.example.daycarat.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Gem", description = "보석 관련 API")
@RestController @RequestMapping("/gem") @RequiredArgsConstructor
public class GemController {

    private final GemService gemService;

    @Operation(summary = "SOARA 작성 항목 조회하기",
            description = """
                    SOARA 작성 항목을 조회합니다.
                    
                    요청값:
                    - episodeId : 에피소드 ID
                    
                    반환값:
                    - gemId : 보석 ID
                    - content1 : 내용1
                    - content2 : 내용2
                    - content3 : 내용3
                    - content4 : 내용4
                    - content5 : 내용5
                    """)
    @GetMapping("/soara/{episodeId}")
    public SuccessResponse<GetGem> getSoara(@PathVariable Long episodeId) {
        return SuccessResponse.success(gemService.getGem(episodeId));
    }

    @Operation(summary = "SOARA 작성 항목 개별 등록하기",
            description = """
                    SOARA 작성 항목을 개별 등록합니다.
                    
                    요청값 예시: 1번 에피소드의 content1을 개별 등록하기
                    {
                      "episodeId": 1,
                      "content1": "내용1"
                    }
                    
                    보내지 않은 부분들(null)은 기존 값이 유지됩니다.
                    """)
    @PatchMapping("/soara")
    public SuccessResponse<Boolean> createSoara(@RequestBody PostSoara postSoara) {
        return SuccessResponse.success(gemService.updateSoara(postSoara));
    }

    @Operation(summary = "보석 등록 완료하기",
            description = """
                    보석을 등록을 완료합니다.
                    
                    요청값:
                    - episodeId : 에피소드 ID
                    
                    반환값:
                    - true : 보석 등록 성공
                    """)
    @PostMapping("/register")
    public SuccessResponse<Boolean> createGem(@RequestBody PostGem postGem) {
        return SuccessResponse.success(gemService.createGem(postGem));
    }

    @Operation(summary = "현재 사용하지 않는 API (보석 수정하기)",
            description = """
                    현재 사용하지 않는 API
                    (사실 사용하려면 하셔도 됩니다.)
                    
                    SOARA 개별 등록하기로 수정한 후 해당 경로로 업데이트를 완료합니다.
                    
                    요청값:
                    - gemId : 보석 ID
                   
                    반환값:
                    - true : 보석 수정 성공
                    """)
    @PatchMapping("/update")
    public SuccessResponse<Boolean> updateGem(@RequestBody PatchGem patchGem) {
        return SuccessResponse.success(gemService.updateGem(patchGem));
    }


    @Operation(summary = "보석 삭제하기",
            description = """
                    보석을 삭제합니다.
                    
                    요청값:
                    - gemId : 보석 ID
                    
                    반환값:
                    - true : 보석 삭제 성공
                    """)
    @DeleteMapping("/delete/{gemId}")
    public SuccessResponse<Boolean> deleteGem(@PathVariable Long gemId) {
        return SuccessResponse.success(gemService.deleteGem(gemId));
    }

  
    @Operation(summary = "AI 추천 키워드 및 문장 조회하기",
            description = """
                    에피소드 ID를 받아서 해당 보석에 대한 AI 추천 키워드 및 문장을 조회합니다.
                    
                    상태 코드:
                    - 200 : 조회 성공
                    - 404 : AI 추천 키워드 생성중
                    - 500 : 서버 에러 (AI 추천 키워드 생성 실패)
                    
                    반환값:
                    - keyword : AI 추천 키워드
                    - generatedContent1: AI 추천 문장 1
                    - generatedContent2: AI 추천 문장 2
                    - generatedContent3: AI 추천 문장 3
                    
                    """)
    @GetMapping("/recommend/{episodeId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "201", description = "AI 추천 키워드 생성중"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                                        , schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "202", description = "서버 에러 (AI 추천 키워드 생성 실패)"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                                        , schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SuccessResponse<GetGeneratedContent> getRecommend(@PathVariable Long episodeId) {
        return SuccessResponse.success(gemService.getRecommend(episodeId));
    }

    @Operation(summary = "보석 키워드별 개수 조회하기",
            description = """
                    보석 키워드별 개수를 조회합니다.
                    
                    반환값:
                    배열로 반환됩니다.
                        - episodeKeyword : 에피소드 키워드
                        - count : 개수
                    """)
    @GetMapping("/keyword")
    public SuccessResponse<GetGemSummaryByKeyword> getGemSummaryByKeyword() {
        return SuccessResponse.success(gemService.getGemSummaryByKeyword());
    }

    @Operation(summary = "보석 키워드별 보석 리스트 조회하기",
            description = """
                    보석 키워드별 보석 리스트를 조회합니다.
                    
                    요청값:
                    - keyword: 에피소드 키워드입니다. (커뮤니케이션, 문제 해결, 창의성, 도전 정신, 전문성, 실행력, 미선택)
                    - cursorId: 1번째 페이지 조회시 null, 2번째 이상 페이지 조회시 직전 페이지의 마지막 episode id을 입력합니다.
                    - pageSize: 한 페이지에 가져올 에피소드 개수, 기본값은 6입니다.
                    
                    반환값:
                    배열로 반환됩니다.
                        - episodeId : 에피소드 ID
                        - title : 보석 제목
                        - date : 보석 생성 날짜
                        - content : 보석 내용
                    """)
    @GetMapping("/keyword/{keyword}")
    public SuccessResponse<List<GetGemPageByKeyword>> getGemSummaryByKeyword(
            @PathVariable String keyword,
            @Parameter(description = "1번째 페이지 조회시 null, " +
                    "2번째 이상 페이지 조회시 직전 페이지의 마지막 episode id") @RequestParam(required = false) Long cursorId,
            @Parameter(description = "한 페이지에 가져올 에피소드 개수, 기본값 6") @RequestParam(required = false) Integer pageSize) {
        return SuccessResponse.success(gemService.getGemPageByKeyword(keyword, cursorId, pageSize));
    }

    @Operation(summary = "보석 개수 조회하기",
            description = """
                    보석 개수를 조회합니다.
                    
                    반환값:
                    - gemCount : 보석 개수
                    """)
    @GetMapping("/count")
    public SuccessResponse<GetGemCount> getGemCount() {
        return SuccessResponse.success(gemService.getGemCount());
    }

    @Operation(summary = "이번달 보석 개수 조회하기",
            description = """
                    이번달 보석 개수를 조회합니다.
                    
                    반환값:
                    - gemCount : 보석 개수
                    """)
    @GetMapping("/report/month-count")
    public SuccessResponse<GetGemCount> getGemCountByMonth() {
        return SuccessResponse.success(gemService.getGemCountByMonth());
    }

    @Operation(summary = "가장 보석이 많은 키워드 조회하기",
            description = """
                    가장 보석이 많은 키워드를 조회합니다.
                    
                    반환값:
                    - episodeKeyword : 에피소드 키워드, **보석이 하나도 없으면 "보석 없음" 이라고 반환합니다.**
                    """)
    @GetMapping("/report/keyword")
    public SuccessResponse<GetMostGemKeyword> getMostGemKeyword() {
        return SuccessResponse.success(gemService.getMostGemKeyword());
    }

    @Operation(summary = "가장 보석이 많은 활동 조회하기",
            description = """
                    가장 보석이 많은 활동을 조회합니다.
                    
                    반환값:
                    - episodeKeyword : 에피소드 키워드, **보석이 하나도 없으면 "보석 없음" 이라고 반환합니다.**
                    """)
    @GetMapping("/report/activity")
    public SuccessResponse<GetMostGemActivity> getMostGemActivity() {
        return SuccessResponse.success(gemService.getMostGemActivity());
    }

    @Operation(summary = "에피소드 클립보드 복사용 문자열 가져오기",
            description = """
                    에피소드 클립보드 복사용 문자열을 가져옵니다.
                    """)
    @GetMapping("/clipboard/{episodeId}")
    public SuccessResponse<GetEpisodeClipboard> getEpisodeClipboard(@PathVariable Long episodeId) {
        return SuccessResponse.success(gemService.getEpisodeClipboard(episodeId));
    }

}
