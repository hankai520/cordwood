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
package ren.hankai.cordwood.core;

import org.junit.Assert;
import org.junit.Test;
import ren.hankai.cordwood.core.test.CoreTestSupport;

import java.io.File;

/**
 * 程序初始化器测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 21, 2016 1:02:16 PM
 */
public class ApplicationInitializerTest extends CoreTestSupport {

  @Test
  public void testInitialize() throws Exception {
    final String[] subDirs = {Preferences.getConfigDir(), Preferences.getDataDir(),
        Preferences.getCacheDir(), Preferences.getLogDir(), Preferences.getTempDir(),
        Preferences.getAttachmentDir(), Preferences.getBackupDir(), Preferences.getDbDir(),
        Preferences.getPluginsDir(), Preferences.getLibsDir()};
    for (final String dir : subDirs) {
      final File file = new File(dir);
      Assert.assertTrue(file.exists() && file.isDirectory());
    }
    final File file = new File(Preferences.getConfigFilePath("testSupport.txt"));
    Assert.assertTrue(file.exists() && file.isFile());
  }
}
