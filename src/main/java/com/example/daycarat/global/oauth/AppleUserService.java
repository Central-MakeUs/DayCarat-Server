package com.example.daycarat.global.oauth;

import com.example.daycarat.domain.user.dto.AppleUserDto;
import com.example.daycarat.domain.user.entity.Role;
import com.example.daycarat.domain.user.entity.User;
import com.example.daycarat.domain.user.repository.UserRepository;
import com.example.daycarat.global.error.exception.CustomException;
import com.example.daycarat.global.jwt.SecurityService;
import com.example.daycarat.global.jwt.TokenResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URL;
import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
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



    public Pair<TokenResponse, Boolean> appleLogin(String code) {
        AppleUserDto appleUserInfo = getUserInfo(code);

        Pair<User, Boolean> appleUser = registerAppleUserIfNeed(appleUserInfo);

        Authentication authentication = securityService.forceLogin(appleUser.getLeft());

        return Pair.of(securityService.usersAuthorizationInput(authentication), appleUser.getRight());
    }

    public AppleUserDto getUserInfo(String authorizationCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", createClientSecret());
        body.add("code", authorizationCode);
        body.add("grant_type", "authorization_code");
        body.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.postForEntity("https://appleid.apple.com/auth/token", request, String.class);

            JsonObject json = JsonParser.parseString(response.getBody()).getAsJsonObject();
            String idToken = json.get("id_token").getAsString();

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
        try {
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(keyId).build();
            JWTClaimsSet claimsSet = new JWTClaimsSet();

            Date now = new Date();
            claimsSet.setIssuer(teamId);
            claimsSet.setIssueTime(now);
            claimsSet.setExpirationTime(new Date(now.getTime() + 3600000));
            claimsSet.setAudience(redirectUri);
            claimsSet.setSubject(clientId);

            SignedJWT jwt = new SignedJWT(header, claimsSet);

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(getPrivateKey());
            KeyFactory kf = KeyFactory.getInstance("EC");

            ECPrivateKey ecPrivateKey = (ECPrivateKey) kf.generatePrivate(spec);
            JWSSigner jwsSigner = new ECDSASigner(ecPrivateKey.getS());
            jwt.sign(jwsSigner);

            return jwt.serialize();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to create client secret", e);
        }
    }

    private byte[] getPrivateKey() throws Exception {
        byte[] content;
        File file;

        URL res = getClass().getResource(keyPath);
        if (res == null) {
            throw new Exception("Resource " + keyPath + " not found");
        }
        if ("jar".equals(res.getProtocol())) {
            file = createTempFileFromInputStream();
        } else {
            file = new File(res.getFile());
        }

        if (file != null && file.exists()) {
            content = readContentFromFile(file);
        } else {
            throw new Exception("File " + file + " not found");
        }

        return content;
    }

    private File createTempFileFromInputStream() throws Exception {
        File tempFile;
        try {
            InputStream input = getClass().getResourceAsStream(keyPath);
            tempFile = File.createTempFile("tempfile", ".tmp");
            try (OutputStream out = new FileOutputStream(tempFile)) {
                byte[] bytes = new byte[1024];
                int read;
                while ((read = input.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
            }
            tempFile.deleteOnExit();
        } catch (IOException ex) {
            throw new Exception("Failed to create temp file", ex);
        }
        return tempFile;
    }

    private byte[] readContentFromFile(File file) throws Exception {
        byte[] content;
        try (FileReader keyReader = new FileReader(file);
             PemReader pemReader = new PemReader(keyReader))
        {
            PemObject pemObject = pemReader.readPemObject();
            content = pemObject.getContent();
        } catch (IOException e) {
            throw new Exception("Failed to read private key", e);
        }
        return content;
    }


}