package com.example.daycarat.domain.user.domain;

import com.example.daycarat.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity @Data
@NoArgsConstructor
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String email;
    private String name;
    private String picture;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(String email, String name, String picture, Role role) {
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.role = role;
    }


}
