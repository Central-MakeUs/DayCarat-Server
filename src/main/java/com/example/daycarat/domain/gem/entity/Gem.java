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

    private String s3ObjectKey;

    private String content1;
    private String content2;
    private String content3;
    private String content4;
    private String content5;

    @Builder
    public Gem(Episode episode, String s3ObjectKey, String content1, String content2, String content3, String content4, String content5) {
        this.episode = episode;
        this.s3ObjectKey = s3ObjectKey;
        this.content1 = content1;
        this.content2 = content2;
        this.content3 = content3;
        this.content4 = content4;
        this.content5 = content5;
    }

    public static Gem of(Episode episode, String s3ObjectKey, String content1, String content2, String content3, String content4, String content5) {
        return Gem.builder()
                .episode(episode)
                .s3ObjectKey(s3ObjectKey)
                .content1(content1)
                .content2(content2)
                .content3(content3)
                .content4(content4)
                .content5(content5)
                .build();
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void update(String s3ObjectKey, String content1, String content2, String content3, String content4, String content5) {
        this.s3ObjectKey = s3ObjectKey;
        this.content1 = content1 == null ? this.content1 : content1;
        this.content2 = content2 == null ? this.content2 : content2;
        this.content3 = content3 == null ? this.content3 : content3;
        this.content4 = content4 == null ? this.content4 : content4;
        this.content5 = content5 == null ? this.content5 : content5;
    }
}
