package com.litongjava.admin.services;

import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.jfinal.kit.Kv;
import com.litongjava.admin.costants.TableNames;
import com.litongjava.db.activerecord.Db;
import com.litongjava.model.body.RespBodyVo;

public class SystemUserService {

  public RespBodyVo changePassword(Long userId, Map<String, String> requestMap) {
    Kv kv = Kv.create().set(requestMap);
    String oldPassword = kv.getStr("oldPassword");
    String newPassword = kv.getStr("newPassword");
    
    String confirmNewPassword = kv.getStr("confirmNewPassword");
    if (!newPassword.equals(confirmNewPassword)) {
      return RespBodyVo.fail("password does not match");
    }

    String hashedPassword = DigestUtils.sha256Hex(oldPassword);

    String sqlTemplate = String.format("select count(1) from %s where id=? and password=?", TableNames.tio_boot_admin_system_users);

    boolean exists = Db.existsBySql(sqlTemplate, userId, hashedPassword);
    if (!exists) {
      return RespBodyVo.fail("wrong password");
    }
    sqlTemplate = "update " + TableNames.tio_boot_admin_system_users + " set password=? where id=?";
    int update = Db.updateBySql(sqlTemplate, DigestUtils.sha256Hex(newPassword), userId);

    if (update == 1) {
      return RespBodyVo.ok();
    }

    return RespBodyVo.fail();
  }
}
