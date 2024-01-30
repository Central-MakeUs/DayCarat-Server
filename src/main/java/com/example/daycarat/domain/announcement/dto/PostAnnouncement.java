package com.example.daycarat.domain.announcement.dto;

import com.example.daycarat.domain.announcement.entity.Announcement;

public record PostAnnouncement(
        String title,
        String content
) {
    public Announcement toEntity() {
        return Announcement.of(title, content);
    }
}
