
package ren.hankai.cordwood.core.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * 路径工具类测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 24, 2016 5:40:03 PM
 */
public class PathUtilTest {

  @Test
  public void testParseResourcePath() {
    String result = PathUtil.parseResourcePath("/resources/demo_plugin/css/light/style.css");
    Assert.assertEquals("css/light/style.css", result);
  }
}
