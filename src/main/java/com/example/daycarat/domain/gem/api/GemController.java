package com.example.daycarat.domain.gem.api;

import com.example.daycarat.domain.gem.dto.PostGem;
import com.example.daycarat.domain.gem.service.GemService;
import com.example.daycarat.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/gem") @RequiredArgsConstructor
public class GemController {

    private final GemService gemService;


    @Operation(summary = "보석 등록하기")
    @PostMapping("/register")
    public SuccessResponse<Boolean> createGem(@RequestBody PostGem postGem) {
        return SuccessResponse.createSuccess(gemService.createGem(postGem));
    }


}
