/*******************************************************************************
 * Copyright (C) 2019 hankai
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/

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
