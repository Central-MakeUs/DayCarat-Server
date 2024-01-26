package com.example.daycarat.domain.user.entity;

import com.example.daycarat.domain.user.dto.PatchUserInfo;
import com.example.daycarat.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity @Getter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String email;
    private String profileImage;
    private String password;

    // 추가 정보
    private String nickname;
    private String jobTitle;
    private String strength;

    // 푸시알림 허용 여부
    private Boolean pushAllow;
    private String fcmToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(String email, String nickname, String profileImage, String password, Role role, String jobTitle, String strength, Boolean pushAllow, String fcmToken) {
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.password = password;
        this.role = role;
        this.jobTitle = jobTitle;
        this.strength = strength;
        this.pushAllow = pushAllow;
        this.fcmToken = fcmToken;
    }


    public void update(PatchUserInfo patchUserInfo) {
        this.nickname = patchUserInfo.nickname() != null ? patchUserInfo.nickname() : this.nickname;
        this.jobTitle = patchUserInfo.jobTitle() != null ? patchUserInfo.jobTitle() : this.jobTitle;
        this.strength = patchUserInfo.strength() != null ? patchUserInfo.strength() : this.strength;
        this.pushAllow = patchUserInfo.pushAllow() != null ? patchUserInfo.pushAllow() : this.pushAllow;
        this.fcmToken = patchUserInfo.fcmToken() != null ? patchUserInfo.fcmToken() : this.fcmToken;
    }

    public void updateProfile(String profile) {
        this.profileImage = profile;
    }
}
