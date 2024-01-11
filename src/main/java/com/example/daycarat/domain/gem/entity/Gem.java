package com.example.daycarat.domain.gem.entity;

import com.example.daycarat.domain.episode.entity.Episode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "episode_id", nullable = false)
    private Episode episode;

    @Enumerated(EnumType.STRING)
    private AppealPoint appealPoint;

    private String content1;
    private String content2;
    private String content3;

    @Builder
    public Gem(Episode episode, AppealPoint appealPoint, String content1, String content2, String content3) {
        this.episode = episode;
        this.appealPoint = appealPoint;
        this.content1 = content1;
        this.content2 = content2;
        this.content3 = content3;
    }

    public static Gem of(Episode episode, AppealPoint appealPoint, String content1, String content2, String content3) {
        return Gem.builder()
                .episode(episode)
                .appealPoint(appealPoint)
                .content1(content1)
                .content2(content2)
                .content3(content3)
                .build();
    }

}
