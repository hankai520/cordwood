
package ren.hankai.cordwood.core.util;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import ren.hankai.cordwood.TestSupport;
import ren.hankai.cordwood.core.ApplicationInitializer;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.util.RuntimeVariables;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 程序运行时变量测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 21, 2016 1:16:46 PM
 */
public class RuntimeVariablesTest extends TestSupport {

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
    RuntimeVariables.setAllVariables(new HashMap<>());
    FileUtils.deleteDirectory(new File(Preferences.getHomeDir()));
  }

  @Test
  public void testSetAndGetVariable() {
    RuntimeVariables.setVariable("testSetVariable", "testSetVariable");
    Assert.assertTrue("testSetVariable".equals(RuntimeVariables.getVariable("testSetVariable")));
    RuntimeVariables.setAllVariables(new HashMap<>());
  }

  @Test
  public void testGetBool() {
    RuntimeVariables.setVariable("bool", "true");
    Assert.assertTrue(RuntimeVariables.getBool("bool"));
    RuntimeVariables.setAllVariables(new HashMap<>());
  }

  @Test
  public void testGetInt() {
    RuntimeVariables.setVariable("int", "1");
    Assert.assertEquals(1, RuntimeVariables.getInt("int"));
    RuntimeVariables.setAllVariables(new HashMap<>());
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
    RuntimeVariables.setAllVariables(new HashMap<>());
  }
}
