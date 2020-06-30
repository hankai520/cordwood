
package ren.hankai.cordwood.data.util;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

/**
 * 分页工具类测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 3, 2018 3:18:06 PM
 */
public class PageUtilTest {

  @Test
  public void testPageWithIndexAndSizeIntInt() {
    final Pageable pageable = PageUtil.pageWithIndexAndSize(2, 10);
    Assert.assertEquals(1, pageable.getPageNumber());
    Assert.assertEquals(10, pageable.getPageSize());
    Assert.assertEquals(10, pageable.getOffset());
  }

  @Test
  public void testPageWithIndexAndSizeIntIntBooleanStringArray() {
    final Pageable pageable = PageUtil.pageWithIndexAndSize(3, 10, true, "name");
    Assert.assertEquals(2, pageable.getPageNumber());
    Assert.assertEquals(10, pageable.getPageSize());
    Assert.assertEquals(20, pageable.getOffset());
    Assert.assertEquals(Direction.ASC, pageable.getSort().getOrderFor("name").getDirection());
  }

  @Test
  public void testPageWithOffsetAndCountIntInt() {
    final Pageable pageable = PageUtil.pageWithOffsetAndCount(15, 5);
    Assert.assertEquals(3, pageable.getPageNumber());
    Assert.assertEquals(5, pageable.getPageSize());
    Assert.assertEquals(15, pageable.getOffset());
  }

  @Test
  public void testPageWithOffsetAndCountIntIntStringBoolean() {
    final Pageable pageable = PageUtil.pageWithOffsetAndCount(15, 5, "name", false);
    Assert.assertEquals(3, pageable.getPageNumber());
    Assert.assertEquals(5, pageable.getPageSize());
    Assert.assertEquals(15, pageable.getOffset());
    Assert.assertEquals(Direction.DESC, pageable.getSort().getOrderFor("name").getDirection());
  }

}
