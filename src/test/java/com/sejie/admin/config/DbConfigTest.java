package com.sejie.admin.config;

import java.util.List;

import org.junit.Test;

import com.litongjava.admin.config.AdminDbConfig;
import com.litongjava.db.activerecord.Db;
import com.litongjava.tio.utils.environment.EnvUtils;

public class DbConfigTest {

  @Test
  public void test() {
    EnvUtils.load();
    new AdminDbConfig().activeRecordPlugin();
    String sql = "SELECT name FROM sqlite_master WHERE type='table'";
    List<String> queryListString = Db.queryListString(sql);
    for (String string : queryListString) {
      System.out.println(string);
    }
  }

  @Test
  public void testTableExists() {
    EnvUtils.load();
    new AdminDbConfig().activeRecordPlugin();
    
    String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='tio_boot_admin_system_users'";
    List<String> queryListString = Db.queryListString(sql);
    //[tio_boot_admin_system_users]
    System.out.println(queryListString);
  }

}
