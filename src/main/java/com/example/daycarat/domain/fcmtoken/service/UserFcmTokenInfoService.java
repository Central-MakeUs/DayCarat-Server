package com.example.daycarat.domain.fcmtoken.service;

import com.example.daycarat.domain.fcmtoken.entity.UserFcmTokenInfo;
import com.example.daycarat.global.dynamodb.UserFcmTokenInfoRepository;
import com.example.daycarat.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.daycarat.global.error.exception.ErrorCode.USER_FCM_TOKEN_NOT_FOUND;

@Service @RequiredArgsConstructor
public class UserFcmTokenInfoService {

    private final UserFcmTokenInfoRepository userFcmTokenInfoRepository;

    public UserFcmTokenInfo getFcmToken(Long userId) {
        return userFcmTokenInfoRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_FCM_TOKEN_NOT_FOUND));
    }

    public void save(Long userId, String fcmToken) {
        userFcmTokenInfoRepository.save(new UserFcmTokenInfo(userId, fcmToken));
    }

    public void saveOrUpdate(Long userId, String fcmToken) {

        UserFcmTokenInfo userFcmTokenInfo = userFcmTokenInfoRepository.findById(userId)
                .orElse(new UserFcmTokenInfo(userId, fcmToken));

        userFcmTokenInfo.update(fcmToken);

        userFcmTokenInfoRepository.save(userFcmTokenInfo);

    }

    public void delete(Long userId) {
        try {
            userFcmTokenInfoRepository.deleteById(userId);
        }
        catch (Exception e) {
            throw new CustomException(USER_FCM_TOKEN_NOT_FOUND);
        }
    }
}
