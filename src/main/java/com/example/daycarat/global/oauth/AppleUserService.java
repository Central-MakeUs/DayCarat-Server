package com.example.daycarat.global.oauth;

import com.example.daycarat.domain.user.dto.ApplePublicKeyResponse;
import com.example.daycarat.domain.user.dto.AppleUserDto;
import com.example.daycarat.domain.user.entity.Role;
import com.example.daycarat.domain.user.entity.User;
import com.example.daycarat.domain.user.repository.UserRepository;
import com.example.daycarat.global.error.exception.CustomException;
import com.example.daycarat.global.jwt.SecurityService;
import com.example.daycarat.global.jwt.TokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.text.ParseException;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import static com.example.daycarat.global.error.exception.ErrorCode.APPLE_LOGIN_FAILED;

@Service @RequiredArgsConstructor @Slf4j
public class AppleUserService {

    private final UserRepository userRepository;
    private final SecurityService securityService;

    @Value("${oauth.apple.client-id}")
    private String clientId;
    @Value("${oauth.apple.redirect-uri}")
    private String redirectUri;
    @Value("${oauth.apple.team-id}")
    private String teamId;
    @Value("${oauth.apple.key-id}")
    private String keyId;
    @Value("${oauth.apple.key-path}")
    private String keyPath;



    public Pair<TokenResponse, Boolean> appleLogin(String email) {
        AppleUserDto appleUserInfo = new AppleUserDto(null, email);

        Pair<User, Boolean> appleUser = registerAppleUserIfNeed(appleUserInfo);

        Authentication authentication = securityService.forceLogin(appleUser.getLeft());

        return Pair.of(securityService.usersAuthorizationInput(authentication), appleUser.getRight());
    }

    public AppleUserDto getUserInfo(String idToken) {
        try {
            // id_token 디코딩
            JWTClaimsSet claimsSet;
            try {
                SignedJWT signedJWT = SignedJWT.parse(idToken);
                claimsSet = signedJWT.getJWTClaimsSet();
            } catch (ParseException e) {
                throw new RuntimeException("Failed to decode id_token", e);
            }

            // AppleUserDto 객체 생성 및 반환
            AppleUserDto user = new AppleUserDto();
            user.setId(claimsSet.getSubject());  // 'sub' claim은 사용자의 고유 ID
            user.setEmail((String) claimsSet.getClaim("email"));  // 이메일
            return user;
        }
        catch (Exception e) {
            log.error(String.valueOf(e));
            throw new CustomException(APPLE_LOGIN_FAILED);
        }
    }

    private Pair<User, Boolean> registerAppleUserIfNeed (AppleUserDto appleUserDto) {

        String appleEmail = appleUserDto.getEmail();
        User appleUser = userRepository.findByEmail(appleEmail)
                .orElse(null);

        if (appleUser == null) {

            String password = UUID.randomUUID().toString();

            appleUser = User.builder()
                    .email(appleEmail)
                    .role(Role.USER)
                    .password(password)
                    .pushAllow(false)
                    .build();

            userRepository.save(appleUser);

            return Pair.of(appleUser, true);

        }
        return Pair.of(appleUser, false);
    }

    private ApplePublicKeyResponse getApplePublicKey() {
        String url = "https://appleid.apple.com/auth/keys";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        ApplePublicKeyResponse applePublicKeyResponse;

        try {
            applePublicKeyResponse = objectMapper.readValue(response.getBody(), ApplePublicKeyResponse.class);
        } catch (Exception e) {
            throw new CustomException(APPLE_LOGIN_FAILED);
        }

        return applePublicKeyResponse;
    }

    private void validate(String identityToken) {
        try {
            ApplePublicKeyResponse response = getApplePublicKey();

            String headerOfIdentityToken = identityToken.substring(0, identityToken.indexOf("."));
            Map header = new ObjectMapper().readValue(new String(Base64.getDecoder().decode(headerOfIdentityToken), StandardCharsets.UTF_8), Map.class);
            ApplePublicKeyResponse.Key key = response.getMatchedKeyBy((String) header.get("kid"), (String) header.get("alg"))
                    .orElseThrow(() -> new NullPointerException("Failed get public key from apple's id server."));

            byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
            byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());

            BigInteger n = new BigInteger(1, nBytes);
            BigInteger e = new BigInteger(1, eBytes);

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            Claims body = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(identityToken).getBody();

            if (!body.getIssuer().equals("https://appleid.apple.com") || !body.getAudience().equals(clientId)) {
                throw new CustomException(APPLE_LOGIN_FAILED);
            }

        } catch (Exception e) {
            throw new CustomException(APPLE_LOGIN_FAILED);
        }
    }

}