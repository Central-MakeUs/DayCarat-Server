package com.example.daycarat.domain.episode.entity;

import com.example.daycarat.domain.user.domain.User;
import com.example.daycarat.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Episode extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "episode")
    private List<EpisodeActivityTag> episodeActivityTags;

    @OneToMany(mappedBy = "episode")
    private List<EpisodeKeyword> episodeKeywords;

    private String title;

    // 선택날짜
    private LocalDate selectedDate;

    // 활동종류
    private String episodeType;

    // 참여역할
    private String participationRole;

    // 다듬기여부 (soft delete)
    private boolean isFinalized;

    @Builder
    public Episode(User user, List<EpisodeActivityTag> episodeActivityTags, List<EpisodeKeyword> episodeKeywords, String title, LocalDate selectedDate, String episodeType, String participationRole, boolean isFinalized) {
        this.user = user;
        this.episodeActivityTags = episodeActivityTags;
        this.episodeKeywords = episodeKeywords;
        this.title = title;
        this.selectedDate = selectedDate;
        this.episodeType = episodeType;
        this.participationRole = participationRole;
        this.isFinalized = isFinalized;
    }

}
