package com.litongjava.admin.handler;

import com.litongjava.admin.services.LoginService;
import com.litongjava.admin.vo.LoginAccountVo;
import com.litongjava.jfinal.aop.Aop;
import com.litongjava.model.body.RespBodyVo;
import com.litongjava.tio.boot.http.TioRequestContext;
import com.litongjava.tio.http.common.HttpRequest;
import com.litongjava.tio.http.common.HttpResponse;
import com.litongjava.tio.http.server.model.HttpCors;
import com.litongjava.tio.http.server.util.CORSUtils;
import com.litongjava.tio.http.server.util.Resps;
import com.litongjava.tio.utils.json.Json;
import com.litongjava.tio.utils.token.TokenManager;

public class ApiLoginHandler {
  public HttpResponse account(HttpRequest request) {

    HttpResponse httpResponse = TioRequestContext.getResponse();
    CORSUtils.enableCORS(httpResponse, new HttpCors());

    String bodyString = request.getBodyString();
    LoginAccountVo loginAccountVo = Json.getJson().parse(bodyString, LoginAccountVo.class);
    LoginService loginService = Aop.get(LoginService.class);
    RespBodyVo respVo = loginService.login(loginAccountVo);
    return httpResponse.setJson(respVo);
  }

  public HttpResponse outLogin(HttpRequest request) {
    HttpResponse httpResponse = TioRequestContext.getResponse();
    CORSUtils.enableCORS(httpResponse, new HttpCors());
    Long userIdLong = TioRequestContext.getUserIdLong();
    //remove
    TokenManager.logout(userIdLong);

    return Resps.json(httpResponse, RespBodyVo.ok());
  }

  /**
   * 因为拦击器已经经过了验证,判断token是否存在即可
   * @param request
   * @return
   */
  public HttpResponse validateLogin(HttpRequest request) {
    HttpResponse httpResponse = TioRequestContext.getResponse();
    CORSUtils.enableCORS(httpResponse, new HttpCors());

    Long userIdLong = TioRequestContext.getUserIdLong();
    boolean login = TokenManager.isLogin(userIdLong);
    return Resps.json(httpResponse, RespBodyVo.ok(login));

  }
}
