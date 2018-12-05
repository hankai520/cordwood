
package ren.hankai.cordwood.core.test;

/**
 * 仅用于测试的 Web Service 路由信息。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 23, 2018 3:36:53 PM
 */
public class Route {

  public static final String MOCK_PRIFIX = "/test/mock";

  // 测试webservice基类提供的异常处理
  public static final String S1 = MOCK_PRIFIX + "/s1";
  public static final String S2 = MOCK_PRIFIX + "/s2";
  public static final String S3 = MOCK_PRIFIX + "/s3";

  // 测试控制器基类提供的功能
  public static final String S4 = MOCK_PRIFIX + "/s4";

  // 测试稳定器
  public static final String S5_1 = MOCK_PRIFIX + "/s5_1";
  public static final String S5_2 = MOCK_PRIFIX + "/s5_2";

  // 测试面包屑导航
  public static final String S6_1 = MOCK_PRIFIX + "/s6_1";
  public static final String S6_2 = MOCK_PRIFIX + "/s6_2";

}
