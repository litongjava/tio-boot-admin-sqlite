package com.litongjava.admin.services;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.jfinal.kit.Kv;
import com.litongjava.admin.costants.AppConstant;
import com.litongjava.admin.vo.LoginAccountVo;
import com.litongjava.db.activerecord.Db;
import com.litongjava.model.body.RespBodyVo;
import com.litongjava.model.token.AuthToken;
import com.litongjava.tio.utils.jwt.JwtUtils;
import com.litongjava.tio.utils.token.TokenManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginService {
  public Long getUserIdByUsernameAndPassword(LoginAccountVo loginAccountVo) {
    // digest
    String password = DigestUtils.sha256Hex(loginAccountVo.getPassword());
    log.info("password:{}", password);
    String sql = "select id from tio_boot_admin_system_users where username=? and password=?";
    return Db.queryLong(sql, loginAccountVo.getUsername(), password);
  }

  public RespBodyVo login(LoginAccountVo loginAccountVo) {
    RespBodyVo respVo;
    // 1.登录
    Long userId = getUserIdByUsernameAndPassword(loginAccountVo);

    if (userId != null) {
      // 2. 设置过期时间payload
      long tokenTimeout = (System.currentTimeMillis() + 3600000) / 1000;

      // 3.创建token
      AuthToken authToken = JwtUtils.createToken(AppConstant.SECRET_KEY, new AuthToken(userId, tokenTimeout));
      TokenManager.login(userId, authToken.getToken());

      Kv kv = new Kv();
      kv.set("token", authToken.getToken());
      kv.set("tokenTimeout", tokenTimeout);
      kv.set("type", loginAccountVo.getType());
      kv.set("status", "ok");

      respVo = RespBodyVo.ok(kv);
    } else {
      Map<String, String> data = new HashMap<>(1);
      data.put("status", "false");
      respVo = RespBodyVo.fail().data(data);
    }
    return respVo;
  }
}
