package com.example.daycarat.domain.episode.api;

import com.example.daycarat.domain.episode.dto.*;
import com.example.daycarat.domain.episode.service.ActivityTagService;
import com.example.daycarat.domain.episode.service.EpisodeService;
import com.example.daycarat.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/episode")
@RequiredArgsConstructor
public class EpisodeController {

    private final ActivityTagService activityTagService;
    private final EpisodeService episodeService;

    @Operation(summary = "활동 태그 등록하기")
    @PostMapping("/activityTag")
    public SuccessResponse<Boolean> createActivtyTag(@RequestBody PostActivityTag postActivityTag) {
        return SuccessResponse.success(activityTagService.createActivityTag(postActivityTag));
    }

    @Operation(summary = "활동 태그 조회하기")
    @GetMapping("/activityTag")
    public SuccessResponse<List<GetActivityTag>> getActivtyTag() {
        return SuccessResponse.success(activityTagService.getActivityTagList());
    }

    @Operation(summary = "에피소드 등록하기")
    @PostMapping("/register")
    public SuccessResponse<Boolean> createEpisode(@RequestBody PostEpisode postEpisode) {
        return SuccessResponse.success(episodeService.createEpisode(postEpisode));
    }

    @Operation(summary = "에피소드 수정하기")
    @PatchMapping("/update")
    public SuccessResponse<Boolean> updateEpisode(@RequestBody PatchEpisode patchEpisode) {
        return SuccessResponse.success(episodeService.updateEpisode(patchEpisode));
    }

    @Operation(summary = "에피소드 삭제하기")
    @DeleteMapping("/delete/{episodeId}")
    public SuccessResponse<Boolean> deleteEpisode(@PathVariable Long episodeId) {
        return SuccessResponse.success(episodeService.deleteEpisode(episodeId));
    }

    @Operation(summary = "에피소드 상세 조회하기")
    @GetMapping("/{episodeId}")
    public SuccessResponse<GetEpisodeDetail> getEpisodeDetail(@PathVariable Long episodeId) {
        return SuccessResponse.success(episodeService.getEpisodeDetail(episodeId));
    }

    @Operation(summary = "에피소드 최신순 조회 3개")
    @GetMapping("/recent")
    public SuccessResponse<List<GetRecentEpisode>> getRecentEpisode() {
        return SuccessResponse.success(episodeService.getRecentEpisode());
    }

    @Operation(summary = "에피소드 조회: 날짜 최신순")
    @GetMapping("/date")
    public SuccessResponse<List<GetEpisodeSummaryByDate>> getEpisodeSummaryByDate(
            @Parameter(description = "조회년도, null일 시 2024년") @RequestParam(required = false) Integer year) {

        return SuccessResponse.success(episodeService.getEpisodeSummaryByDate(year));
    }

    @Operation(summary = "에피소드 조회: 활동 많은순")
    @GetMapping("/activity")
    public SuccessResponse<List<GetEpisodeSummaryByActivity>> getEpisodeSummaryByActivity() {

        return SuccessResponse.success(episodeService.getEpisodeSummaryByActivity());

    }

    @Operation(summary = "에피소드 조회: 날짜 최신순: 월별 조회")
    @GetMapping("/date/{year}/{month}")
    public SuccessResponse<List<GetEpisodePage>> getEpisodeByDate(
            @Parameter(description = "년도", example = "2024") @PathVariable Integer year,
            @Parameter(description = "월", example = "3") @PathVariable Integer month,
            @Parameter(description = "1번째 페이지 조회시 null, " +
                    "2번째 이상 페이지 조회시 직전 페이지의 마지막 episode id") @RequestParam(required = false) Long cursorId,
            @Parameter(description = "한 페이지에 가져올 에피소드 개수, 기본값 6") @RequestParam(required = false) Integer pageSize) {

        return SuccessResponse.success(episodeService.getEpisodeByDate(year, month, cursorId, pageSize));
    }

    @Operation(summary = "에피소드 조회: 활동 많은순: 활동별 조회")
    @GetMapping("/activity/{activityTagName}")
    public SuccessResponse<List<GetEpisodePage>> getEpisodeByActivity(
            @Parameter(description = "활동 태그 이름", example = "운동") @PathVariable String activityTagName,
            @Parameter(description = "1번째 페이지 조회시 null, " +
                    "2번째 이상 페이지 조회시 직전 페이지의 마지막 episode id") @RequestParam(required = false) Long cursorId,
            @Parameter(description = "한 페이지에 가져올 에피소드 개수, 기본값 6") @RequestParam(required = false) Integer pageSize) {

        return SuccessResponse.success(episodeService.getEpisodeByActivity(activityTagName, cursorId, pageSize));
    }


}
