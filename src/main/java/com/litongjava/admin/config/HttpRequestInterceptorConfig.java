package com.litongjava.admin.config;
// 导入必要的类和注解

import com.litongjava.admin.inteceptor.AuthInterceptor;
import com.litongjava.annotation.AConfiguration;
import com.litongjava.annotation.AInitialization;
import com.litongjava.tio.boot.http.interceptor.HttpInteceptorConfigure;
import com.litongjava.tio.boot.http.interceptor.HttpInterceptorModel;
import com.litongjava.tio.boot.server.TioBootServer;

@AConfiguration
public class HttpRequestInterceptorConfig {

  @AInitialization
  public void config() {
    // 登录 拦截器实例
    AuthInterceptor authTokenInterceptor = new AuthInterceptor();
    HttpInterceptorModel model = new HttpInterceptorModel();
    model.setInterceptor(authTokenInterceptor);

    // 拦截所有路由
    model.addblockeUrl("/**");

    // 设置例外路由
    // index
    model.addAlloweUrls("", "/");
    // user
    model.addAlloweUrls("/register/*", "/api/login/account", "/api/login/outLogin");
    // app
    model.addAlloweUrls("/app/*", "/joinCacheByUser");

    HttpInteceptorConfigure inteceptorConfigure = new HttpInteceptorConfigure();
    inteceptorConfigure.add(model);
    // 将拦截器配置添加到 Tio 服务器
    TioBootServer.me().setHttpInteceptorConfigure(inteceptorConfigure);
  }
}
