package com.example.daycarat.domain.user.api;

import com.example.daycarat.domain.user.dto.UserDto;
import com.example.daycarat.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController @RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 정보 조회하기")
    @GetMapping("/userInfo")
    public ResponseEntity<UserDto> getUserInfo() {
        return ResponseEntity.ok()
                .body(userService.getUserInfo());
    }

}
