package com.example.daycarat.global.oauth;

import com.example.daycarat.domain.user.domain.Role;
import com.example.daycarat.domain.user.domain.User;
import com.example.daycarat.domain.user.dto.UserDto;
import com.example.daycarat.domain.user.repository.UserRepository;
import com.example.daycarat.global.jwt.TokenResponse;
import com.example.daycarat.global.jwt.TokenService;
import com.example.daycarat.global.jwt.UserDetailsImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service @RequiredArgsConstructor
public class KakaoUserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Value("${oauth.kakao.client-id}")
    private String clientId;
    @Value("${oauth.kakao.client-secret}")
    private String clientSecret;

    public Pair<TokenResponse, Boolean> kakaoLogin(String accessToken) throws JsonProcessingException {

        UserDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        Pair<User, Boolean> kakaoUser = registerKakaoUserIfNeed(kakaoUserInfo);

        Authentication authentication = forceLogin(kakaoUser.getLeft());

        return Pair.of(kakaoUsersAuthorizationInput(authentication), kakaoUser.getRight());

    }

    private UserDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        return handleKakaoResponse(response.getBody());
    }

    private UserDto handleKakaoResponse(String responseBody) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String nickname = jsonNode.get("properties")
                .get("nickname").asText();

        String email = jsonNode.get("kakao_account").get("email").asText();

        String thumbnailImage = jsonNode.get("kakao_account").get("profile").get("thumbnail_image_url").asText();

        return UserDto.of(email, nickname, thumbnailImage);
    }

    private Pair<User, Boolean> registerKakaoUserIfNeed (UserDto kakaoUserInfo) {

        String kakaoEmail = kakaoUserInfo.getEmail();
        User kakaoUser = userRepository.findByEmail(kakaoEmail)
                .orElse(null);

        if (kakaoUser == null) {

            String password = UUID.randomUUID().toString();

            kakaoUser = User.builder()
                    .email(kakaoEmail)
                    .nickname(kakaoUserInfo.getNickname())
                    .profileImage(kakaoUserInfo.getProfileImage())
                    .role(Role.USER)
                    .password(password)
                    .pushAllow(false)
                    .build();

            userRepository.save(kakaoUser);

            return Pair.of(kakaoUser, true);

        }
        return Pair.of(kakaoUser, false);
    }

    private Authentication forceLogin(User kakaoUser) {
        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private TokenResponse kakaoUsersAuthorizationInput(Authentication authentication) {
        UserDetailsImpl userDetailsImpl = ((UserDetailsImpl) authentication.getPrincipal());
        String accessToken = tokenService.createAccessToken(userDetailsImpl);
        String refreshToken = tokenService.createRefreshToken(userDetailsImpl);

        return new TokenResponse(accessToken, refreshToken);
    }
}
