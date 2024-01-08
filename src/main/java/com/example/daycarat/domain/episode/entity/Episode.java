package com.example.daycarat.domain.episode.entity;

import com.example.daycarat.domain.user.domain.User;
import com.example.daycarat.global.entity.BaseTimeEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
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

}
