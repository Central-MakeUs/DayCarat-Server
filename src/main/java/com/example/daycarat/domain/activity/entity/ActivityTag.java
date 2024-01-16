package com.example.daycarat.domain.activity.entity;

import com.example.daycarat.domain.user.domain.User;
import com.example.daycarat.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityTag extends BaseEntity {

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

    public void delete() {
        this.isDeleted = true;
    }

    public void update(String activityTagName) {
        this.activityTagName = activityTagName;
    }
}
