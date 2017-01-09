
package ren.hankai.cordwood.console.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ren.hankai.cordwood.console.api.exception.AppNotFoundException;
import ren.hankai.cordwood.console.api.payload.BusinessErrors;
import ren.hankai.cordwood.console.config.Route;
import ren.hankai.cordwood.console.persist.model.AppBean;
import ren.hankai.cordwood.console.service.AppService;
import ren.hankai.cordwood.core.api.support.ApiCode;
import ren.hankai.cordwood.core.api.support.ApiResponse;
import ren.hankai.cordwood.core.api.support.WebServiceSupport;
import ren.hankai.cordwood.web.security.AccessAuthenticator;
import ren.hankai.cordwood.web.security.AccessAuthenticator.TokenInfo;

import java.util.HashMap;

/**
 * 身份认证接口。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 8, 2016 1:51:26 PM
 */
@Controller
public class AuthenticationApi extends WebServiceSupport {

  @Autowired
  private AccessAuthenticator accessAuthenticator;
  @Autowired
  private AppService appService;

  /**
   * 获取鉴权码。
   *
   * @param appKey 应用标识
   * @param appSk 应用秘钥
   * @param interval 鉴权码有效时长（分钟）
   * @return 鉴权码信息
   * @author hankai
   * @since Dec 24, 2016 9:46:46 AM
   */
  @PostMapping(Route.API_AUTHENTICATE)
  @ResponseBody
  public ApiResponse authenticate(@RequestParam("app_key") String appKey,
      @RequestParam("app_sk") String appSk, @RequestParam("interval") Integer interval) {
    final ApiResponse response = new ApiResponse();
    final AppBean app = appService.getIdentifiedApp(appKey, appSk);
    if (app == null) {
      throw new AppNotFoundException(BusinessErrors.APP_NOT_FOUND, "App not found!");
    }
    interval = ((interval <= 0) || (interval > (60 * 24 * 2))) ? 60 * 24 : interval;
    final TokenInfo tokenInfo = TokenInfo.withinMinutes(appKey, appSk, interval);
    final String appToken = accessAuthenticator.generateAccessToken(tokenInfo);
    final HashMap<String, String> data = new HashMap<>();
    data.put("accessToken", appToken);
    data.put("expiry", tokenInfo.getExpiryTime() + "");
    response.getBody().setData(data);
    response.getBody().setSuccess(true);
    response.setCode(ApiCode.Success);
    return response;
  }
}
