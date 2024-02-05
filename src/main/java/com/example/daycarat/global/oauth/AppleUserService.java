package com.example.daycarat.global.oauth;

import com.example.daycarat.domain.user.dto.AppleUserDto;
import com.example.daycarat.domain.user.entity.Role;
import com.example.daycarat.domain.user.entity.User;
import com.example.daycarat.domain.user.repository.UserRepository;
import com.example.daycarat.global.error.exception.CustomException;
import com.example.daycarat.global.jwt.SecurityService;
import com.example.daycarat.global.jwt.TokenResponse;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.io.StringReader;
import java.security.PrivateKey;
import java.text.ParseException;
import java.util.Date;
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



    public Pair<TokenResponse, Boolean> appleLogin(String id_token) {
        AppleUserDto appleUserInfo = getUserInfo(id_token);

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
                claimsSet = (JWTClaimsSet) signedJWT.getJWTClaimsSet();
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

    private String createClientSecret() {
        Date now = new Date();
        Date exp = new Date(now.getTime() + (1000 * 60 * 60 * 24 * 180)); // 180일 후 만료

        try {
            return Jwts.builder()
                    .setHeaderParam("kid", keyId)
                    .setHeaderParam("alg", "ES256")
                    .setIssuer(teamId)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(exp)
                    .setAudience("https://appleid.apple.com")
                    .setSubject(clientId)
                    .signWith(getApplePrivateKey(), SignatureAlgorithm.ES256)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create client secret", e);
        }
    }

    private PrivateKey getApplePrivateKey() {
        try {
            ClassPathResource resource = new ClassPathResource(keyPath);
            String privateKey = new String(resource.getInputStream().readAllBytes());
            Reader pemReader = new StringReader(privateKey);
            PEMParser pemParser = new PEMParser(pemReader);
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            PrivateKeyInfo object = (PrivateKeyInfo)pemParser.readObject();

            return converter.getPrivateKey(object);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get apple private key", e);
        }
    }

}