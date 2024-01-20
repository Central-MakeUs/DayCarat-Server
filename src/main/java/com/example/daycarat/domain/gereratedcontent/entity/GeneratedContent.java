package com.example.daycarat.domain.gereratedcontent.entity;

import com.example.daycarat.domain.episode.entity.Episode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GeneratedContent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "episode_id", nullable = false)
    private Episode episode;

    @Enumerated(EnumType.STRING)
    private Keyword keyword;

    private String generatedContent1;
    private String generatedContent2;
    private String generatedContent3;

    @Builder
    public GeneratedContent(Episode episode, Keyword keyword, String generatedContent1, String generatedContent2, String generatedContent3) {
        this.episode = episode;
        this.keyword = keyword;
        this.generatedContent1 = generatedContent1;
        this.generatedContent2 = generatedContent2;
        this.generatedContent3 = generatedContent3;
    }

    public static GeneratedContent of(Episode episode, Keyword keyword, String generatedContent1, String generatedContent2, String generatedContent3) {
        return GeneratedContent.builder()
                .episode(episode)
                .keyword(keyword)
                .generatedContent1(generatedContent1)
                .generatedContent2(generatedContent2)
                .generatedContent3(generatedContent3)
                .build();
    }

}
