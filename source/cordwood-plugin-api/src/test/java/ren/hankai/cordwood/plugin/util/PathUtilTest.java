package ren.hankai.cordwood.plugin.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * 路径工具类测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 26, 2016 1:09:35 PM
 */
public class PathUtilTest {

  @Test
  public void testParseResourcePath() {
    String url = "http://localhost:8000/resources/demo_plugin/css/style.css";
    String path = PathUtil.parseResourcePath(url);
    Assert.assertEquals("css/style.css", path);

    url = "http://localhost:8000/impossible/resources/demo_plugin/css/style.css";
    path = PathUtil.parseResourcePath(url);
    Assert.assertEquals("css/style.css", path);

    url = "/resources/demo_plugin/css/style.css";
    path = PathUtil.parseResourcePath(url);
    Assert.assertEquals("css/style.css", path);

    url = "/demo_plugin/css/style.css";
    path = PathUtil.parseResourcePath(url);
    Assert.assertNull(path);

    url = "/demo_plugin/css/resources/style.css";
    path = PathUtil.parseResourcePath(url);
    Assert.assertNull(path);
  }

}
