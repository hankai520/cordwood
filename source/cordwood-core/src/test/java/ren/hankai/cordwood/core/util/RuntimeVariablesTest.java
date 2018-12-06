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

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import ren.hankai.cordwood.core.ApplicationInitializer;
import ren.hankai.cordwood.core.test.CoreTestSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 程序运行时变量测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 21, 2016 1:16:46 PM
 */
public class RuntimeVariablesTest extends CoreTestSupport {

  @After
  public void clearVars() {
    RuntimeVariables.setAllVariables(new HashMap<>());
    RuntimeVariables.saveVariables();
  }

  @Test
  public void testSaveParameters() throws Exception {
    Assert.assertTrue(ApplicationInitializer.initialize());
    RuntimeVariables.setVariable("testKey", "testValue");
    RuntimeVariables.saveVariables();
    final File file = RuntimeVariables.getVariablesFile();
    Assert.assertTrue(file.exists() && file.isFile());
    RuntimeVariables.setAllVariables(new HashMap<>());
    RuntimeVariables.reloadVariables();
    Assert.assertTrue("testValue".equals(RuntimeVariables.getVariable("testKey")));
  }

  @Test
  public void testSetAndGetVariable() {
    RuntimeVariables.setVariable("testSetVariable", "testSetVariable");
    Assert.assertTrue("testSetVariable".equals(RuntimeVariables.getVariable("testSetVariable")));
  }

  @Test
  public void testGetBool() {
    RuntimeVariables.setVariable("bool", "true");
    Assert.assertTrue(RuntimeVariables.getBool("bool"));
  }

  @Test
  public void testGetInt() {
    RuntimeVariables.setVariable("int", "1");
    Assert.assertEquals(1, RuntimeVariables.getInt("int"));
  }

  @Test
  public void testSetAllParams() {
    RuntimeVariables.setVariable("shouldNotExist", "value");
    Assert.assertTrue("value".equals(RuntimeVariables.getVariable("shouldNotExist")));
    final Map<String, String> map = new HashMap<>();
    map.put("exist", "value");
    RuntimeVariables.setAllVariables(map);
    Assert.assertNull(RuntimeVariables.getVariable("shouldNotExist"));
    Assert.assertTrue("value".equals(RuntimeVariables.getVariable("exist")));
  }

  @Test
  public void testSetCacheSeconds() throws Exception {
    RuntimeVariables.setCacheSeconds(2);
    RuntimeVariables.setVariable("testSetCacheSeconds", "123");
    Assert.assertEquals("123", RuntimeVariables.getVariable("testSetCacheSeconds"));
    RuntimeVariables.saveVariables();

    // 绕过内存，直接修改文件，测试缓存
    final Properties props = new Properties();
    props.load(new FileInputStream(RuntimeVariables.savePath));
    props.setProperty("testSetCacheSeconds", "567");
    props.store(new FileOutputStream(RuntimeVariables.savePath), null);
    Assert.assertEquals("123", RuntimeVariables.getVariable("testSetCacheSeconds"));

    Thread.sleep(1000 * 3);
    Assert.assertEquals("567", RuntimeVariables.getVariable("testSetCacheSeconds"));
  }

  @Test
  public void testGetAllVariables() {
    RuntimeVariables.setVariable("testGetAllVariables1", "1");
    RuntimeVariables.setVariable("testGetAllVariables2", "2");
    final Map<String, String> vars = RuntimeVariables.getAllVariables();
    Assert.assertEquals(2, vars.size());
    Assert.assertEquals("1", vars.get("testGetAllVariables1"));
    Assert.assertEquals("2", vars.get("testGetAllVariables2"));
  }

  @Test
  public void testRemoveVariable() {
    RuntimeVariables.setVariable("testRemoveVariable", "1");
    Assert.assertEquals("1", RuntimeVariables.getVariable("testRemoveVariable"));
    RuntimeVariables.removeVariable("testRemoveVariable");
    Assert.assertNull(RuntimeVariables.getVariable("testRemoveVariable"));
  }
}
