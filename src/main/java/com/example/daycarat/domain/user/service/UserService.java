package com.example.daycarat.domain.user.service;

import com.example.daycarat.domain.user.domain.User;
import com.example.daycarat.domain.user.dto.GetUserInfo;
import com.example.daycarat.domain.user.dto.PatchUserInfo;
import com.example.daycarat.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public GetUserInfo getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .map(GetUserInfo::of)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

    }

    public Boolean patchUserInfo(PatchUserInfo patchUserInfo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        user.update(patchUserInfo);

        userRepository.save(user);

        return true;

    }
}
