package com.example.daycarat.domain.episode.api;

import com.example.daycarat.domain.episode.dto.*;
import com.example.daycarat.domain.episode.service.ActivityTagService;
import com.example.daycarat.domain.episode.service.EpisodeService;
import com.example.daycarat.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Episode", description = "에피소드 관련 API")
@RestController @RequestMapping("/episode")
@RequiredArgsConstructor
public class EpisodeController {

    private final ActivityTagService activityTagService;
    private final EpisodeService episodeService;

//    * Deprecated method
//    @Operation(summary = "활동 태그 등록하기",
//            description = """
//                    활동 태그를 등록합니다.
//                    - activityTagName: 활동 태그 이름입니다.
//                    """)
//    @PostMapping("/activityTag")
//    public SuccessResponse<Boolean> createActivtyTag(@RequestBody PostActivityTag postActivityTag) {
//        return SuccessResponse.success(activityTagService.createActivityTag(postActivityTag));
//    }

    @Operation(summary = "활동 태그 조회하기",
            description = """
                        활동 태그를 조회합니다.
                        """)
    @GetMapping("/activityTag")
    public SuccessResponse<List<GetActivityTag>> getActivtyTag() {
        return SuccessResponse.success(activityTagService.getActivityTagList());
    }

    @Operation(summary = "활동 태그 삭제하기",
            description = """
                    활동 태그를 삭제합니다.
                    - activityTagId: 삭제할 활동 태그의 ID입니다.
                    """)
    @DeleteMapping("/activityTag/{activityTagId}")
    public SuccessResponse<Boolean> deleteActivityTag(@PathVariable Long activityTagId) {
        return SuccessResponse.success(activityTagService.deleteActivityTag(activityTagId));
    }

    @Operation(summary = "에피소드 등록하기",
            description = """
                    에피소드를 등록합니다.
                    - title: 에피소드의 제목입니다.
                    - date: 'yyyy-MM-dd' 형식으로 입력해야 합니다.
                    - activityTagId: 활동 태그 ID로, 활동 태그 조회하기 API를 통해 확인할 수 있습니다.
                    - episodeContents: 에피소드의 내용입니다. 배열로 여러 개를 보낼 수 있습니다.
                        - episodeContentType: 현재 'T', 'E', 'M', 'P' 중 하나를 입력해야 합니다. (미정)
                        - content: 작성한 내용입니다.\s
                    """)
    @PostMapping("/register")
    public SuccessResponse<Boolean> createEpisode(@RequestBody PostEpisode postEpisode) {
        return SuccessResponse.success(episodeService.createEpisode(postEpisode));
    }

    @Operation(summary = "에피소드 수정하기",
            description = """
                    에피소드를 수정합니다. 작성하지 않은 내용은 기존 값을 유지합니다.
                    - episodeId: 수정할 에피소드의 ID입니다.
                    - title: 에피소드의 제목입니다.
                    - date: 'yyyy-MM-dd' 형식으로 입력해야 합니다.
                    - activityTagId: 활동 태그 ID로, 활동 태그 조회하기 API를 통해 확인할 수 있습니다.
                    - episodeContents: 에피소드의 내용입니다. 배열로 여러 개를 보낼 수 있습니다.
                        - episodeContentType: 현재 'T', 'E', 'M', 'P' 중 하나를 입력해야 합니다. (미정)
                        - content: 작성한 내용입니다.\s
                    """)
    @PatchMapping("/update")
    public SuccessResponse<Boolean> updateEpisode(@RequestBody PatchEpisode patchEpisode) {
        return SuccessResponse.success(episodeService.updateEpisode(patchEpisode));
    }

    @Operation(summary = "에피소드 삭제하기",
            description = """
                    에피소드를 삭제합니다.
                    - episodeId: 삭제할 에피소드의 ID입니다.
                    """)
    @DeleteMapping("/delete/{episodeId}")
    public SuccessResponse<Boolean> deleteEpisode(@PathVariable Long episodeId) {
        return SuccessResponse.success(episodeService.deleteEpisode(episodeId));
    }

    @Operation(summary = "에피소드 상세 조회하기",
            description = """
                    에피소드의 상세 정보를 조회합니다.
                    - episodeId: 조회할 에피소드의 ID입니다.
                    """)
    @GetMapping("/{episodeId}")
    public SuccessResponse<GetEpisodeDetail> getEpisodeDetail(@PathVariable Long episodeId) {
        return SuccessResponse.success(episodeService.getEpisodeDetail(episodeId));
    }

    @Operation(summary = "에피소드 최신순 조회 3개",
            description = """
                    에피소드를 최신순으로 3개 조회합니다.
                    """)
    @GetMapping("/recent")
    public SuccessResponse<List<GetRecentEpisode>> getRecentEpisode() {
        return SuccessResponse.success(episodeService.getRecentEpisode());
    }

    @Operation(summary = "에피소드 조회: 날짜 최신순",
            description = """
                    에피소드를 년도별로 조회합니다. 월별 에피소드 개수를 내림차순으로 반환합니다.
                    - year: 조회할 년도입니다. null일 시 2024년으로 조회합니다.
                    
                    반환값:
                    - month: 월
                    - quantity: 해당 월의 에피소드 개수
                    """)
    @GetMapping("/date")
    public SuccessResponse<List<GetEpisodeSummaryByDate>> getEpisodeSummaryByDate(
            @Parameter(description = "조회년도, null일 시 2024년") @RequestParam(required = false) Integer year) {

        return SuccessResponse.success(episodeService.getEpisodeSummaryByDate(year));
    }

    @Operation(summary = "에피소드 조회: 활동 많은순",
            description = """
                    에피소드를 활동별로 조회합니다. 활동별 에피소드 개수를 내림차순으로 반환합니다.
                    
                    반환값:
                    - activityTagName: 활동 태그 이름
                    - quantity: 해당 활동 태그의 에피소드 개수
                    """)
    @GetMapping("/activity")
    public SuccessResponse<List<GetEpisodeSummaryByActivity>> getEpisodeSummaryByActivity() {

        return SuccessResponse.success(episodeService.getEpisodeSummaryByActivity());

    }

    @Operation(summary = "에피소드 조회: 날짜 최신순: 월별 조회",
            description = """
                    에피소드를 년도별, 월별로 조회합니다. 해당 월의 에피소드를 최신순으로 반환합니다.
                    - year: 조회할 년도입니다.
                    - month: 조회할 월입니다.
                    - cursorId: 1번째 페이지 조회시 null, 2번째 이상 페이지 조회시 직전 페이지의 마지막 episode id을 입력합니다.
                    - pageSize: 한 페이지에 가져올 에피소드 개수, 기본값은 6입니다.
                    """)
    @GetMapping("/date/{year}/{month}")
    public SuccessResponse<List<GetEpisodePage>> getEpisodeByDate(
            @Parameter(description = "년도", example = "2024") @PathVariable Integer year,
            @Parameter(description = "월", example = "3") @PathVariable Integer month,
            @Parameter(description = "1번째 페이지 조회시 null, " +
                    "2번째 이상 페이지 조회시 직전 페이지의 마지막 episode id") @RequestParam(required = false) Long cursorId,
            @Parameter(description = "한 페이지에 가져올 에피소드 개수, 기본값 6") @RequestParam(required = false) Integer pageSize) {

        return SuccessResponse.success(episodeService.getEpisodeByDate(year, month, cursorId, pageSize));
    }

    @Operation(summary = "에피소드 조회: 활동 많은순: 활동별 조회",
            description = """
                    에피소드를 활동별로 조회합니다. 해당 활동 태그의 에피소드를 최신순으로 반환합니다.
                    - activityTagName: 활동 태그 이름입니다.
                    - cursorId: 1번째 페이지 조회시 null, 2번째 이상 페이지 조회시 직전 페이지의 마지막 episode id을 입력합니다.
                    - pageSize: 한 페이지에 가져올 에피소드 개수, 기본값은 6입니다.
                    """)
    @GetMapping("/activity/{activityTagName}")
    public SuccessResponse<List<GetEpisodePage>> getEpisodeByActivity(
            @Parameter(description = "활동 태그 이름", example = "운동") @PathVariable String activityTagName,
            @Parameter(description = "1번째 페이지 조회시 null, " +
                    "2번째 이상 페이지 조회시 직전 페이지의 마지막 episode id") @RequestParam(required = false) Long cursorId,
            @Parameter(description = "한 페이지에 가져올 에피소드 개수, 기본값 6") @RequestParam(required = false) Integer pageSize) {

        return SuccessResponse.success(episodeService.getEpisodeByActivity(activityTagName, cursorId, pageSize));
    }


}
