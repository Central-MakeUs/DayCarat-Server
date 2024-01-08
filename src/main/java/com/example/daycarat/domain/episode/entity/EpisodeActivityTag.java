package com.example.daycarat.domain.episode.entity;

import com.example.daycarat.global.entity.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
public class EpisodeActivityTag extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "episode_id", nullable = false)
    private Episode episode;

    @ManyToOne
    @JoinColumn(name = "activity_tag_id", nullable = false)
    private ActivityTag activityTag;

}
