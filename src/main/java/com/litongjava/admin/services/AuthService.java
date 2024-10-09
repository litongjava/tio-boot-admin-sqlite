package com.litongjava.admin.services;

import com.litongjava.admin.costants.AppConstant;
import com.litongjava.tio.utils.hutool.StrUtil;
import com.litongjava.tio.utils.token.TokenManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthService {
  public Long getIdByToken(String authorization) {
    log.info("authorization:{}", authorization);
    if (StrUtil.isBlank(authorization)) {
      return null;
    }
    String[] split = authorization.split(" ");

    Long userId = 0L;
    if (split.length > 1) {
      String idToken = split[1];
      userId = TokenManager.parseUserIdLong(AppConstant.SECRET_KEY, idToken);
    } else {
      userId = TokenManager.parseUserIdLong(AppConstant.SECRET_KEY, authorization);
    }
    return userId;
  }

}
