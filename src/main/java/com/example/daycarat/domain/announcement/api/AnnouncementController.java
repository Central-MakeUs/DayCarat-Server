package com.example.daycarat.domain.announcement.api;

import com.example.daycarat.domain.announcement.dto.GetAnnouncement;
import com.example.daycarat.domain.announcement.dto.PostAnnouncement;
import com.example.daycarat.domain.announcement.service.AnnouncementService;
import com.example.daycarat.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Announcement", description = "구현 안할듯 ..?")
@RestController @RequestMapping("/announcement") @RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @Operation(summary = "공지사항 등록하기")
    @PostMapping
    public SuccessResponse<Boolean> saveAnnouncement(@RequestBody PostAnnouncement postAnnouncement) {
        return SuccessResponse.success(announcementService.createAnnouncement(postAnnouncement));
    }

    @Operation(summary = "공지사항 삭제하기")
    @DeleteMapping("/{announcementId}")
    public SuccessResponse<Boolean> deleteAnnouncement(@PathVariable String announcementId) {
        return SuccessResponse.success(announcementService.deleteAnnouncement(announcementId));
    }

    @Operation(summary = "공지사항 조회하기")
    @GetMapping
    public SuccessResponse<List<GetAnnouncement>> getAnnouncement() {
        return SuccessResponse.success(announcementService.getAnnouncements());
    }
}

