package com.example.daycarat.domain.episode.api;

import com.example.daycarat.domain.episode.dto.GetActivityTag;
import com.example.daycarat.domain.episode.dto.PostActivityTag;
import com.example.daycarat.domain.episode.dto.PostEpisode;
import com.example.daycarat.domain.episode.service.ActivityTagService;
import com.example.daycarat.domain.episode.service.EpisodeService;
import io.swagger.v3.oas.annotations.Operation;
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

}
