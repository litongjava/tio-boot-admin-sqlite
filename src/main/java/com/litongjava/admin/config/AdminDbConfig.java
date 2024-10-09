package com.litongjava.admin.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.jfinal.template.Engine;
import com.jfinal.template.source.ClassPathSourceFactory;
import com.litongjava.admin.utils.DbTables;
import com.litongjava.annotation.AConfiguration;
import com.litongjava.annotation.AInitialization;
import com.litongjava.db.activerecord.ActiveRecordPlugin;
import com.litongjava.db.activerecord.OrderedFieldContainerFactory;
import com.litongjava.db.activerecord.dialect.Sqlite3Dialect;
import com.litongjava.db.hikaricp.DsContainer;
import com.litongjava.tio.boot.server.TioBootServer;
import com.litongjava.tio.utils.environment.EnvUtils;

import lombok.extern.slf4j.Slf4j;

@AConfiguration
@Slf4j
public class AdminDbConfig {

  public DataSource dataSource() {
    // get parameter from config file
    String jdbcUrl = EnvUtils.get("jdbc.url");
    String jdbcUser = EnvUtils.get("jdbc.user");
    String jdbcPswd = EnvUtils.get("jdbc.pswd");
    String jdbcValidationQuery = EnvUtils.get("jdbc.validationQuery");
    log.info("jdbcUrl:{}", jdbcUrl);

    // SQLite 会自动创建数据库文件，但不会创建不存在的目录
    // Check if the SQLite database path exists, and create it if necessary
    try {
      if (jdbcUrl != null && jdbcUrl.startsWith("jdbc:sqlite:")) {
        String dbPath = jdbcUrl.substring("jdbc:sqlite:".length());
        Path path = Paths.get(dbPath).getParent();
        if (path != null && !Files.exists(path)) {
          Files.createDirectories(path);
          log.info("Created directory for SQLite database: {}", path);
        }
      }
    } catch (IOException e) {
      log.error("Failed to create directories for SQLite database", e);
      throw new RuntimeException(e);
    }

    // create datasource
    DruidDataSource druidDataSource = new DruidDataSource();

    // set basic parameter
    druidDataSource.setUrl(jdbcUrl);
    druidDataSource.setUsername(jdbcUser);
    druidDataSource.setPassword(jdbcPswd);
    druidDataSource.setValidationQuery(jdbcValidationQuery);

    // save datasource
    DsContainer.setDataSource(druidDataSource);

    // close datasource while server close
    TioBootServer.me().addDestroyMethod(druidDataSource::close);
    return druidDataSource;
  }

  /**
   * create ActiveRecordPlugin
   * 
   * @return
   * @throws Exception
   */
  @AInitialization
  public void activeRecordPlugin() {
    // get datasource from DsContainer

    // get parameter from config file
    boolean dev = EnvUtils.isDev();
    boolean jdbcShowSql = EnvUtils.getBoolean("jdbc.showSql", false);

    // create plugin
    ActiveRecordPlugin arp = new ActiveRecordPlugin(dataSource());

    // set parameter
    arp.setDialect(new Sqlite3Dialect());
    arp.setContainerFactory(new OrderedFieldContainerFactory());
    arp.setShowSql(jdbcShowSql);
    arp.setDialect(new Sqlite3Dialect());

    if (dev) {
      arp.setDevMode(true);
    }

    // config engine
    Engine engine = arp.getEngine();
    engine.setSourceFactory(new ClassPathSourceFactory());
    engine.setCompressorOn(' ');
    engine.setCompressorOn('\n');

    arp.addSqlTemplate("/enjoy-sql/all.sql");

    // start plugin
    arp.start();

    // close plugin while server close
    TioBootServer.me().addDestroyMethod(arp::stop);

    DbTables.init();
  }
}
