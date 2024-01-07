package com.example.daycarat.domain.user.domain;

import com.example.daycarat.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity @Getter
@NoArgsConstructor
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String email;
    private String nickname;
    private String picture;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(String email, String nickname, String picture, String password, Role role) {
        this.email = email;
        this.nickname = nickname;
        this.picture = picture;
        this.password = password;
        this.role = role;
    }


}
