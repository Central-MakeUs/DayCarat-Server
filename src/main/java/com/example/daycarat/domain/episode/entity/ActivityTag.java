package com.example.daycarat.domain.episode.entity;

import com.example.daycarat.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityTag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String activityTagName;

    @Builder
    public ActivityTag(User user, String activityTagName) {
        this.user = user;
        this.activityTagName = activityTagName;
    }

    public static ActivityTag of(User user, String activityTagName) {
        return ActivityTag.builder()
                .user(user)
                .activityTagName(activityTagName)
                .build();
    }

}
