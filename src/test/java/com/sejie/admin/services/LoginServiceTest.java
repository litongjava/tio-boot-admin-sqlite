package com.sejie.admin.services;

import java.util.List;

import org.junit.Test;

import com.litongjava.admin.config.AdminDbConfig;
import com.litongjava.db.activerecord.Db;
import com.litongjava.db.activerecord.Record;
import com.litongjava.tio.utils.environment.EnvUtils;
import com.litongjava.tio.utils.json.JsonUtils;

public class LoginServiceTest {

  @Test
  public void test() {
    EnvUtils.load();
    new AdminDbConfig().activeRecordPlugin();
    List<Record> findAll = Db.findAll("tio_boot_admin_system_users");
    for (Record record : findAll) {
      System.out.println(JsonUtils.toJson(record));
    }
  }

  @Test
  public void allStudents() {
    EnvUtils.load();
    new AdminDbConfig().activeRecordPlugin();
    
    List<Record> findAll = Db.findAll("student");
    System.out.println(findAll.size());
  }
}
