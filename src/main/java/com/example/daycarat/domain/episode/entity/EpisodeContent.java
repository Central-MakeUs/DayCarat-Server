package com.example.daycarat.domain.episode.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EpisodeContent {

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

}
