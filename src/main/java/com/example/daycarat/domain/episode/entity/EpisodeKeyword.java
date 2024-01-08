package com.example.daycarat.domain.episode.entity;

import com.example.daycarat.global.entity.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
public class EpisodeKeyword extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "episode_id", nullable = false)
    private Episode episode;

    private Keyword keyword;

    // 유저가 작성한 키워드인지 여부
    private boolean isUser;

}
