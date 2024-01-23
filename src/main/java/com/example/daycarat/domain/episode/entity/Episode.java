package com.example.daycarat.domain.episode.entity;

import com.example.daycarat.domain.activity.entity.ActivityTag;
import com.example.daycarat.domain.gem.entity.Gem;
import com.example.daycarat.domain.gereratedcontent.entity.GeneratedContent;
import com.example.daycarat.domain.user.domain.User;
import com.example.daycarat.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Entity @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Episode extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="activity_tag_id", nullable = false)
    private ActivityTag activityTag;

    @OneToMany(mappedBy = "episode", cascade = CascadeType.ALL)
    private List<EpisodeContent> episodeContents;

    @OneToMany(mappedBy = "episode", cascade = CascadeType.ALL)
    private List<GeneratedContent> generatedContents;

    @OneToMany(mappedBy = "episode", cascade = CascadeType.ALL)
    private List<Gem> gem;

    private String title;

    // 선택날짜
    private LocalDate selectedDate;

    @Enumerated(EnumType.STRING)
    private EpisodeState episodeState;

    @Enumerated(EnumType.STRING)
    private EpisodeKeyword episodeKeyword;

    @Column(columnDefinition = "Boolean default false")
    private Boolean isEpisodeKeywordUserSelected;

    @Builder
    public Episode(User user, ActivityTag activityTag, String title, LocalDate selectedDate, EpisodeState episodeState, EpisodeKeyword episodeKeyword) {
        this.user = user;
        this.activityTag = activityTag;
        this.title = title;
        this.selectedDate = selectedDate;
        this.episodeState = episodeState;
        this.episodeKeyword = episodeKeyword;
    }

    public static Episode of(User user, ActivityTag activityTag, String title, LocalDate selectedDate, EpisodeState episodeState, EpisodeKeyword episodeKeyword) {
        return Episode.builder()
                .user(user)
                .activityTag(activityTag)
                .title(title)
                .selectedDate(selectedDate)
                .episodeState(episodeState)
                .episodeKeyword(episodeKeyword)
                .build();
    }

    public void update(ActivityTag activityTag, String title, LocalDate selectedDate) {
        this.activityTag = activityTag == null ? this.activityTag : activityTag;
        this.title = title == null ? this.title : title;
        this.selectedDate = selectedDate == null ? this.selectedDate : selectedDate;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void updateState(EpisodeState episodeState) {
        this.episodeState = episodeState;
    }
    public void updateKeyword(EpisodeKeyword episodeKeyword) {
        this.episodeKeyword = episodeKeyword;
    }

    public void updateIsEpisodeKeywordUserSelected(boolean isEpisodeKeywordUserSelected) {
        this.isEpisodeKeywordUserSelected = isEpisodeKeywordUserSelected;
    }

    public void initEpisodeKeyword() {
        this.episodeKeyword = EpisodeKeyword.UNSET;
        this.isEpisodeKeywordUserSelected = false;
    }
}
