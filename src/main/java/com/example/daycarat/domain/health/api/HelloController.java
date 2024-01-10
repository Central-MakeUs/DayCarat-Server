package com.example.daycarat.domain.health.api;


import com.example.daycarat.global.response.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HelloController {

    @GetMapping
    public SuccessResponse<Boolean> health() {
        return SuccessResponse.success(true);
    }

}

