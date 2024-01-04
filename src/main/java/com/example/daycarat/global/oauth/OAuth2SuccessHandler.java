package com.example.daycarat.global.oauth;

import com.example.daycarat.domain.user.dto.UserDto;
import com.example.daycarat.domain.user.mapper.UserRequestMapper;
import com.example.daycarat.domain.user.repository.UserRepository;
import com.example.daycarat.global.jwt.Token;
import com.example.daycarat.global.jwt.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenService tokenService;
    private final UserRequestMapper userRequestMapper;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        UserDto userDto = userRequestMapper.toDto(oAuth2User);

        log.info("Principal에서 꺼낸 OAuth2User = {}", oAuth2User);

        // 최초 로그인이라면 회원가입 처리를 한다.
        if (userRepository.findByEmail(userDto.getEmail()).isEmpty()) {
            userRepository.save(userRequestMapper.toEntity(userDto));
        }

        log.info("토큰 발행 시작");

        Token token = tokenService.generateToken(userDto.getEmail(), "USER");
        log.info("{}", token);
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000")
                .queryParam("token", token.getToken())
                .queryParam("refreshToken", token.getRefreshToken())
                .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}