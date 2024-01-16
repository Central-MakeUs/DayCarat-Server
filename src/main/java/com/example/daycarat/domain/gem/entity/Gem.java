package com.example.daycarat.domain.gem.entity;

import com.example.daycarat.domain.episode.entity.Episode;
import com.example.daycarat.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gem extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "episode_id", nullable = false)
    private Episode episode;

    @Enumerated(EnumType.STRING)
    private AppealPoint appealPoint;

    private String content1;
    private String content2;
    private String content3;
    private String s3ObjectName;

    @Builder
    public Gem(Episode episode, AppealPoint appealPoint, String content1, String content2, String content3, String s3ObjectName) {
        this.episode = episode;
        this.appealPoint = appealPoint;
        this.content1 = content1;
        this.content2 = content2;
        this.content3 = content3;
        this.s3ObjectName = s3ObjectName;
    }

    public static Gem of(Episode episode, AppealPoint appealPoint, String content1, String content2, String content3, String s3ObjectName) {
        return Gem.builder()
                .episode(episode)
                .appealPoint(appealPoint)
                .content1(content1)
                .content2(content2)
                .content3(content3)
                .s3ObjectName(s3ObjectName)
                .build();
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void update(AppealPoint appealPoint, String content1, String content2, String content3, String s3ObjectName) {
        this.appealPoint = appealPoint == null ? this.appealPoint : appealPoint;
        this.content1 = content1 == null ? this.content1 : content1;
        this.content2 = content2 == null ? this.content2 : content2;
        this.content3 = content3 == null ? this.content3 : content3;
        this.s3ObjectName = s3ObjectName;
    }
}
