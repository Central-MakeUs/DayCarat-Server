package com.example.daycarat.domain.episode.entity;

import jakarta.persistence.*;

@Entity
public class ActivityTag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String activityTagName;

}
