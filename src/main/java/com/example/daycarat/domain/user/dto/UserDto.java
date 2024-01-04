package com.example.daycarat.domain.user.dto;

import com.example.daycarat.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDto {
    private String email;
    private String name;
    private String picture;

    @Builder
    public UserDto(String email, String name, String picture) {
        this.email = email;
        this.name = name;
        this.picture = picture;
    }

    public static UserDto of(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .picture(user.getPicture())
                .build();
    }
}