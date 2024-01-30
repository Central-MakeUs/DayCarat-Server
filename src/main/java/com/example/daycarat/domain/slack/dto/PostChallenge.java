package com.example.daycarat.domain.slack.dto;

public record PostChallenge(
        String token,
        String challenge,
        String type
) {
}
