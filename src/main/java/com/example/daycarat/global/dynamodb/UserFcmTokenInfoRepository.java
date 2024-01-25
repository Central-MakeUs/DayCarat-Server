package com.example.daycarat.global.dynamodb;

import com.example.daycarat.domain.fcmtoken.entity.UserFcmTokenInfo;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface UserFcmTokenInfoRepository extends CrudRepository<UserFcmTokenInfo, Long> {
}
