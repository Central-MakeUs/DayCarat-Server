package com.example.daycarat.domain.user.mapper;

import com.example.daycarat.domain.user.domain.User;
import com.example.daycarat.domain.user.dto.UserDto;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;


@Component
public class UserRequestMapper {

    public UserDto toDto(OAuth2User oAuth2User) {
        return UserDto.builder()
                .email(oAuth2User.getAttribute("email"))
                .name(oAuth2User.getAttribute("name"))
                .picture(oAuth2User.getAttribute("picture"))
                .build();
    }

    public User toEntity(UserDto userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }
}
