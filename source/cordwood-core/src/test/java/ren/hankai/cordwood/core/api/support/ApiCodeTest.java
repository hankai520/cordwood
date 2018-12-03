
package ren.hankai.cordwood.core.api.support;

import org.junit.Assert;
import org.junit.Test;

/**
 * API响应代码测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 1, 2018 11:07:17 PM
 */
public class ApiCodeTest {

  @Test
  public void testFromInteger() {
    final int[] codes = new int[] {-2, -1, 1, 2, 3, 4, 5};
    for (final int i : codes) {
      final ApiCode code = ApiCode.fromInteger(i);
      Assert.assertEquals(i, code.value());
    }
  }

}
