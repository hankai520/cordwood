/*******************************************************************************
 * Copyright (C) 2018 hankai
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

package ren.hankai.cordwood.core.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * 数学工具类测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 30, 2018 4:58:34 PM
 */
@SuppressWarnings("deprecation")
public class MathUtilTest {

  private static final String[] timeUnits = new String[] {"s", "m", "h"};
  private static final String[] storeUnits = new String[] {"B", "KB", "MB", "GB", "TB"};

  @Test
  public void testToHumanReadableString() {
    String result = MathUtil.toHumanReadableString(1, 60, timeUnits);
    Assert.assertEquals("1s", result);
    result = MathUtil.toHumanReadableString(60f, 60, timeUnits);
    Assert.assertEquals("1m", result);
    result = MathUtil.toHumanReadableString(60f * 60, 60, timeUnits);
    Assert.assertEquals("1h", result);
    result = MathUtil.toHumanReadableString(60f * 60 * 1.5, 60, timeUnits);
    Assert.assertEquals("1.5h", result);
    result = MathUtil.toHumanReadableString(60f * 60 * 1.523, 60, timeUnits);
    Assert.assertEquals("1.52h", result);

    result = MathUtil.toHumanReadableString(1f, 1024, storeUnits);
    Assert.assertEquals("1B", result);
    result = MathUtil.toHumanReadableString(1024f, 1024, storeUnits);
    Assert.assertEquals("1KB", result);
    result = MathUtil.toHumanReadableString(1024f * 1024, 1024, storeUnits);
    Assert.assertEquals("1MB", result);
    result = MathUtil.toHumanReadableString(1024f * 1024 * 1024, 1024, storeUnits);
    Assert.assertEquals("1GB", result);
    result = MathUtil.toHumanReadableString(1024f * 1024 * 1024 * 1024, 1024, storeUnits);
    Assert.assertEquals("1TB", result);
    result = MathUtil.toHumanReadableString(1024f * 1024 * 1024 * 1024 * 1.83, 1024, storeUnits);
    Assert.assertEquals("1.83TB", result);
    result = MathUtil.toHumanReadableString(1024f * 1024 * 1024 * 1024 * 1.8356, 1024, storeUnits);
    Assert.assertEquals("1.84TB", result);
  }

  @Test
  public void testShortenNumber() {
    String result = MathUtil.shortenNumber(4520, 60, false, timeUnits);
    Assert.assertEquals("1h15m20s", result);
    result = MathUtil.shortenNumber(3600, 60, false, timeUnits);
    Assert.assertEquals("1h", result);
    result = MathUtil.shortenNumber(80, 60, false, timeUnits);
    Assert.assertEquals("1m20s", result);
    result = MathUtil.shortenNumber(60, 60, false, timeUnits);
    Assert.assertEquals("1m", result);
    result = MathUtil.shortenNumber(20, 60, false, timeUnits);
    Assert.assertEquals("20s", result);
    result = MathUtil.shortenNumber(0, 60, false, timeUnits);
    Assert.assertEquals("0s", result);

    result = MathUtil.shortenNumber((long) (1024 * 1024 * 1024 * 3.576), 1024, false, storeUnits);
    Assert.assertEquals("3GB589MB843KB794B", result);
  }

}
