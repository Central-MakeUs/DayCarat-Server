package com.example.daycarat.domain.episode.entity;

import com.example.daycarat.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EpisodeContent extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "episode_id", nullable = false)
    private Episode episode;

    @Enumerated(EnumType.STRING)
    private EpisodeContentType episodeContentType;

    private String content;

    @Builder
    public EpisodeContent(Episode episode, EpisodeContentType episodeContentType, String content) {
        this.episode = episode;
        this.episodeContentType = episodeContentType;
        this.content = content;
    }

    public static EpisodeContent of(Episode episode, EpisodeContentType episodeContentType, String content) {
        return EpisodeContent.builder()
                .episode(episode)
                .episodeContentType(episodeContentType)
                .content(content)
                .build();
    }

    public void update(EpisodeContentType episodeContentType, String content) {
        this.episodeContentType = episodeContentType;
        this.content = content;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
