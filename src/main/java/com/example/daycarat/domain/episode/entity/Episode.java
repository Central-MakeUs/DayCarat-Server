package com.example.daycarat.domain.episode.entity;

import com.example.daycarat.domain.user.domain.User;
import com.example.daycarat.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
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


    // 다듬기여부 (soft delete)
    private boolean isFinalized;

    @Builder
    public Episode(User user, List<EpisodeActivityTag> episodeActivityTags, List<EpisodeKeyword> episodeKeywords, String title, LocalDate selectedDate, boolean isFinalized) {
        this.user = user;
        this.episodeActivityTags = episodeActivityTags;
        this.episodeKeywords = episodeKeywords;
        this.title = title;
        this.selectedDate = selectedDate;
        this.isFinalized = isFinalized;
    }

    public static Episode of(User user, List<EpisodeActivityTag> episodeActivityTags, List<EpisodeKeyword> episodeKeywords, String title, LocalDate selectedDate, boolean isFinalized) {
        return Episode.builder()
                .user(user)
                .episodeActivityTags(episodeActivityTags)
                .episodeKeywords(episodeKeywords)
                .title(title)
                .selectedDate(selectedDate)
                .isFinalized(isFinalized)
                .build();
    }

}
