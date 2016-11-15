
package ren.hankai.cordwood.plugin.pojo;

import org.junit.Assert;
import org.junit.Test;

/**
 * 示例插件测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 21, 2016 5:23:31 PM
 */
public class PojoPluginTest {

  @Test
  public void testSayHello() {
    final PojoPlugin sd = new PojoPlugin();
    final String result = sd.sum(10, 15);
    Assert.assertTrue(result.equals("Hello, the result is: 25"));
  }
}
