package com.example.daycarat.domain.health.api;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HelloController {

    @GetMapping
    public ResponseEntity<Boolean> health() {
        return ResponseEntity.ok().body(true);
    }


}

