package com.example.daycarat.domain.admin.api;

import com.example.daycarat.domain.admin.service.AdminService;
import com.example.daycarat.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin", description = """
    # 관리자 API: 개발용 토큰으로만 작동함
    
    그냥 찬혁이 쓰려고 만든 API이므로 안보셔도 됩니다.
""")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "대표 컨텐츠 설정(flush)")
    @PatchMapping("/episode/content/setMainContents")
    public SuccessResponse<Boolean> setMainContentsOnEpisodeContents() {
        return SuccessResponse.success(adminService.setMainContentsOnEpisodeContents());

    }

}
