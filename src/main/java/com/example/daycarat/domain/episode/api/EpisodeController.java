package com.example.daycarat.domain.episode.api;

import com.example.daycarat.domain.episode.dto.PostActivityTag;
import com.example.daycarat.domain.episode.service.ActivityTagService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/episode")
@RequiredArgsConstructor
public class EpisodeController {

    private final ActivityTagService activityTagService;

    @Operation(summary = "활동 태그 등록하기")
    @PostMapping("/activityTag")
    public ResponseEntity<Boolean> createActivtyTag(@RequestBody PostActivityTag postActivityTag) {
        return ResponseEntity.ok()
                .body(activityTagService.createActivityTag(postActivityTag));
    }
}
