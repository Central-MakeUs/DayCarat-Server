package com.example.daycarat.domain.gereratedcontent.api;

import com.example.daycarat.domain.gereratedcontent.dto.PostGeneratedContent;
import com.example.daycarat.domain.gereratedcontent.service.GeneratedContentService;
import com.example.daycarat.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "GeneratedContent", description = """
        Lambda 함수가 DB에 AI 생성 컨텐츠를 저장하기 위해 사용되는 API
        앱에서는 접근 할 필요 없으므로 무시하셔도 됩니다.
""")
@RestController @RequestMapping("/generated-content") @RequiredArgsConstructor
public class GeneratedContentController {

    private final GeneratedContentService generatedContentService;

    @PostMapping("/register")
    public SuccessResponse<Boolean> registerGeneratedContent(@RequestBody PostGeneratedContent postGeneratedContent) {
        return SuccessResponse.success(generatedContentService.registerGeneratedContent(postGeneratedContent));
    }
}
