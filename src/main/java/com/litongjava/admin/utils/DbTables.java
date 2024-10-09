package com.litongjava.admin.utils;

import java.net.URL;
import java.util.List;

import com.litongjava.db.activerecord.Db;
import com.litongjava.tio.utils.hutool.FileUtil;
import com.litongjava.tio.utils.hutool.ResourceUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DbTables {

  public static void init() {
    String userTableName = "tio_boot_admin_system_users";
    String downloadTableName = "meituan_app_download";

    boolean created = createTable(userTableName);
    if (created) {
      URL url = ResourceUtil.getResource("sql/tio_boot_admin_system_users_init.sql");
      StringBuilder stringBuilder = FileUtil.readURLAsString(url);
      int update = Db.update(stringBuilder.toString());
      log.info("add user:{},{}", userTableName, update);
    }
    
    createTable(downloadTableName);

  }

  private static boolean createTable(String userTableName) {
    String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
    List<String> tables = Db.queryListString(sql, userTableName);
    int size = tables.size();
    if (size < 1) {
      URL url = ResourceUtil.getResource("sql/" + userTableName + ".sql");
      StringBuilder stringBuilder = FileUtil.readURLAsString(url);
      int update = Db.update(stringBuilder.toString());
      log.info("created:{},{}", userTableName, update);
      return true;
    }
    return false;
  }

}
