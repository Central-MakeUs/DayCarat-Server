package com.example.daycarat.domain.episode.dto;

public record GetRecentEpisode(String title, String time) {
    public static GetRecentEpisode of(String title, String time) {
        return new GetRecentEpisode(title, time);
    }
}
