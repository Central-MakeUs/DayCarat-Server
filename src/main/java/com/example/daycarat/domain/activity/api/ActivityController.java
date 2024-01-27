package com.example.daycarat.domain.activity.api;

import com.example.daycarat.domain.activity.dto.GetActivityTag;
import com.example.daycarat.domain.activity.dto.GetActivityTagSearch;
import com.example.daycarat.domain.activity.dto.PatchActivityTag;
import com.example.daycarat.domain.activity.service.ActivityTagService;
import com.example.daycarat.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Activity", description = "활동 태그 관련 API")
@RestController @RequestMapping("/activity") @RequiredArgsConstructor
public class ActivityController {

    private final ActivityTagService activityTagService;

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
    @GetMapping
    public SuccessResponse<List<GetActivityTag>> getActivtyTag() {
        return SuccessResponse.success(activityTagService.getActivityTagList());
    }

    @Operation(summary = "활동 태그 수정하기",
            description = """
                    활동 태그를 수정합니다.
                    - activityTagId: 수정할 활동 태그의 ID입니다.
                    - activityTagName: 수정할 활동 태그의 이름입니다.
                    """)
    @PatchMapping
    public SuccessResponse<Boolean> updateActivityTag(@RequestBody PatchActivityTag patchActivityTag) {
        return SuccessResponse.success(activityTagService.updateActivityTag(patchActivityTag));
    }


    @Operation(summary = "활동 태그 삭제하기",
            description = """
                    활동 태그를 삭제합니다.
                    - activityTagId: 삭제할 활동 태그의 ID입니다.
                    """)
    @DeleteMapping("/{activityTagId}")
    public SuccessResponse<Boolean> deleteActivityTag(@PathVariable Long activityTagId) {
        return SuccessResponse.success(activityTagService.deleteActivityTag(activityTagId));
    }

    @Operation(summary = "활동 태그 검색하기",
            description = """
                    활동 태그를 검색합니다.
                    - activityTagName: 검색할 활동 태그의 이름입니다.
                    """)
    @GetMapping("/search")
    public SuccessResponse<List<GetActivityTagSearch>> searchActivityTag(@RequestParam String activityTagName) {
        return SuccessResponse.success(activityTagService.searchActivityTag(activityTagName));
    }
}
