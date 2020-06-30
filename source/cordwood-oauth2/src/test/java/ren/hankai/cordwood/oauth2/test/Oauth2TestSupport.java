
package ren.hankai.cordwood.oauth2.test;

import ch.qos.logback.classic.Level;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.util.JacksonJsonParser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ren.hankai.cordwood.core.ApplicationInitInfo;
import ren.hankai.cordwood.core.ApplicationInitializer;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.util.LogbackUtil;
import ren.hankai.cordwood.oauth2.core.GrantType;
import ren.hankai.cordwood.oauth2.core.Oauth2Client;

import java.io.File;

/**
 * 单元测试基类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jun 18, 2020 5:31:52 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {Oauth2TestSupport.class})
@ActiveProfiles({Preferences.PROFILE_TEST})
@Configuration
@ComponentScan(basePackages = {"ren.hankai"})
// oauth依赖spring security依赖spring mvc，因此必须启用此注解来导入springmvc配置，否则将
// 找不到合适的HandlerAdapter，导致请求无法转发到 TokenEndpoint
@EnableWebMvc
public abstract class Oauth2TestSupport {

  static {
    System.setProperty(Preferences.ENV_APP_HOME_DIR, "./test-home");

    LogbackUtil.setupConsoleLoggerFor(Level.WARN);
    LogbackUtil.setupConsoleLoggerFor("ren.hankai.cordwood", Level.DEBUG);

    final ApplicationInitInfo initInfo = ApplicationInitInfo.initWithConfigs();
    initInfo.addSupportFiles("system.properties");
    initInfo.addSupportFiles("oauth.jks");
    Assert.assertTrue(
        ApplicationInitializer.initialize(false, initInfo));

    Runtime.getRuntime().addShutdownHook(new Thread() {

      @Override
      public void run() {
        try {
          sleep(1000);
          FileUtils.deleteDirectory(new File(Preferences.getHomeDir()));
        } catch (final Exception ex) {
          // Kindly ignore this exception
          ex.getMessage();
        }
      }
    });
  }

  @Autowired
  protected WebApplicationContext ctx;

  @Qualifier("testClient")
  @Autowired
  protected Oauth2Client testClient;

  protected MockMvc mockMvc;

  @Autowired
  protected PasswordEncoder passwordEncoder;

  /**
   * 初始化spring mvc上下文。
   *
   * @throws Exception 异常
   */
  @Before
  public void setup() throws Exception {
    mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
        .apply(SecurityMockMvcConfigurers.springSecurity())
        .alwaysDo(MockMvcResultHandlers.print())
        .build();
  }

  /**
   * 通过Password授权模式获取访问令牌。
   *
   * @param username 用户名（非client id）
   * @param password 密码（非client secret）
   * @return 令牌
   * @throws Exception 异常
   */
  protected String obtainAccessToken(final String username, final String password) throws Exception {
    final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("grant_type", GrantType.Password.value());
    params.add("client_id", testClient.getId());
    params.add("client_secret", testClient.getSecret());
    params.add("username", username);
    params.add("password", password);
    final ResultActions result = mockMvc.perform(
        MockMvcRequestBuilders.post("/oauth/token")
            .params(params)
            .accept("application/json;charset=UTF-8"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));
    final String resultString = result.andReturn().getResponse().getContentAsString();
    final JacksonJsonParser jsonParser = new JacksonJsonParser();
    return jsonParser.parseMap(resultString).get("access_token").toString();
  }
}
