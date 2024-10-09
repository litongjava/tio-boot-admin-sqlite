package com.litongjava.admin.services;

import com.litongjava.db.SqlPara;
import com.litongjava.db.activerecord.Db;
import com.litongjava.db.activerecord.DbTemplate;
import com.litongjava.db.activerecord.Record;
import com.litongjava.model.body.RespBodyVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserService {
  public RespBodyVo currentUser(Long userId) {
    log.info("userId:{}", userId);
    // template
    DbTemplate template = Db.template("user.getUserById");

    // sqlPara 是一个包含了sql和para的对象
    SqlPara sqlPara = template.getSqlPara();
    sqlPara.addPara(userId);

    // 执行查询
    Record first = Db.findFirst(sqlPara);
    // 返回数据
    return RespBodyVo.ok(first.toKv());
  }
}
