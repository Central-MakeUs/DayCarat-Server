package com.example.daycarat.domain.episode.entity;

import com.example.daycarat.global.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne @JsonIgnore
    @JoinColumn(name = "episode_id", nullable = false)
    private Episode episode;

    @Enumerated(EnumType.STRING)
    private EpisodeContentType episodeContentType;

    @Column(length = 50000)
    private String content;

    private Boolean isMainContent = false;

    @Builder
    public EpisodeContent(Episode episode, EpisodeContentType episodeContentType, String content, Boolean isMainContent) {
        this.episode = episode;
        this.episodeContentType = episodeContentType;
        this.content = content;
        this.isMainContent = isMainContent;
    }

    public static EpisodeContent of(Episode episode, EpisodeContentType episodeContentType, String content, Boolean isMainContent) {
        return EpisodeContent.builder()
                .episode(episode)
                .episodeContentType(episodeContentType)
                .content(content)
                .isMainContent(isMainContent)
                .build();
    }

    public void update(EpisodeContentType episodeContentType, String content) {
        this.episodeContentType = episodeContentType == null ? this.episodeContentType : episodeContentType;
        this.content = content == null ? this.content : content;
    }

    public void setIsMainContent(Boolean isMainContent) {
        this.isMainContent = isMainContent;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
