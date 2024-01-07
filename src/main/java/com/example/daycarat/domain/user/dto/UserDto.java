package com.example.daycarat.domain.user.dto;

import com.example.daycarat.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDto {
    private String email;
    private String nickname;
    private String picture;

    @Builder
    public UserDto(String email, String nickname, String picture) {
        this.email = email;
        this.nickname = nickname;
        this.picture = picture;
    }

    public static UserDto of(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .picture(user.getPicture())
                .build();
    }

    public static UserDto of(String email, String nickname) {
        return UserDto.builder()
                .email(email)
                .nickname(nickname)
                .build();
    }
}