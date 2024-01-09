package com.example.daycarat.domain.episode.api;

import com.example.daycarat.domain.episode.dto.*;
import com.example.daycarat.domain.episode.service.ActivityTagService;
import com.example.daycarat.domain.episode.service.EpisodeService;
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
    public ResponseEntity<Boolean> createActivtyTag(@RequestBody PostActivityTag postActivityTag) {
        return ResponseEntity.ok()
                .body(activityTagService.createActivityTag(postActivityTag));
    }

    @Operation(summary = "활동 태그 조회하기")
    @GetMapping("/activityTag")
    public ResponseEntity<List<GetActivityTag>> getActivtyTag() {
        return ResponseEntity.ok()
                .body(activityTagService.getActivityTagList());
    }

    @Operation(summary = "에피소드 등록하기")
    @PostMapping("/register")
    public ResponseEntity<Boolean> createEpisode(@RequestBody PostEpisode postEpisode) {
        return ResponseEntity.ok()
                .body(episodeService.createEpisode(postEpisode));
    }

    @Operation(summary = "에피소드 최신순 조회 3개")
    @GetMapping("/recent")
    public ResponseEntity<List<GetRecentEpisode>> getRecentEpisode() {
        return ResponseEntity.ok()
                .body(episodeService.getRecentEpisode());
    }

    @Operation(summary = "에피소드 조회: 날짜 최신순")
    @GetMapping("/date")
    public ResponseEntity<List<GetEpisodeSummaryByDate>> getEpisodeSummaryByDate(
            @Parameter(description = "조회년도, null일 시 2024년") @RequestParam(required = false) Integer year) {

        return ResponseEntity.ok()
                .body(episodeService.getEpisodeSummaryByDate(year));
    }

    @Operation(summary = "에피소드 조회: 활동 많은순")
    @GetMapping("/activity")
    public ResponseEntity<List<GetEpisodeSummaryByActivity>> getEpisodeSummaryByActivity(
            @Parameter(description = "조회년도, null일 시 2024년") @RequestParam(required = false) Integer year,
            @Parameter(description = "1번째 페이지 조회시 null, " +
                    "2번째 이상 페이지 조회시 직전 페이지의 마지막 episode id") @RequestParam(required = false) Long cursorId,
            @Parameter(description = "한 페이지에 가져올 episode 수") @RequestParam(required = false) int pageSize) {

        return ResponseEntity.ok()
                .body(episodeService.getEpisodeSummaryByActivity(year, cursorId, pageSize));

    }


}
