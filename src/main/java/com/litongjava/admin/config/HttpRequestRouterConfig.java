package com.litongjava.admin.config;

import com.litongjava.admin.handler.ApiLoginHandler;
import com.litongjava.admin.handler.GeographicHandler;
import com.litongjava.admin.handler.SystemHandler;
import com.litongjava.admin.handler.UserHandler;
import com.litongjava.annotation.AConfiguration;
import com.litongjava.annotation.AInitialization;
import com.litongjava.jfinal.aop.Aop;
import com.litongjava.tio.boot.server.TioBootServer;
import com.litongjava.tio.http.server.router.HttpRequestRouter;

@AConfiguration
public class HttpRequestRouterConfig {

  @AInitialization
  public void httpRoutes() {
    HttpRequestRouter r = TioBootServer.me().getRequestRouter();
    // 创建handler
    UserHandler userHandler = Aop.get(UserHandler.class);
    GeographicHandler geographicHandler = Aop.get(GeographicHandler.class);
    SystemHandler systemHandler = Aop.get(SystemHandler.class);

    ApiLoginHandler apiLoginHandler = Aop.get(ApiLoginHandler.class);

    // 添加action
    r.add("/api/login/account", apiLoginHandler::account);
    r.add("/api/login/outLogin", apiLoginHandler::outLogin);
    r.add("/api/login/validateLogin", apiLoginHandler::validateLogin);
    r.add("/api/currentUser", userHandler::currentUser);
    r.add("/api/accountSettingCurrentUser", userHandler::accountSettingCurrentUser);

    r.add("/api/system/changeUserPassword", systemHandler::changeUserPassword);
    r.add("/api/geographic/province", geographicHandler::province);

  }
}
