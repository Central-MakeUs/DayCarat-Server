package com.example.daycarat.domain.episode.entity;

import com.example.daycarat.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EpisodeActivityTag extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "episode_id", nullable = false)
    private Episode episode;

    @ManyToOne
    @JoinColumn(name = "activity_tag_id", nullable = false)
    private ActivityTag activityTag;

    @Builder
    public EpisodeActivityTag(Episode episode, ActivityTag activityTag) {
        this.episode = episode;
        this.activityTag = activityTag;
    }

    public static EpisodeActivityTag of(Episode episode, ActivityTag activityTag) {
        return EpisodeActivityTag.builder()
                .episode(episode)
                .activityTag(activityTag)
                .build();
    }

}
