
package ren.hankai.cordwood.plugin.advance;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ren.hankai.cordwood.plugin.ApplicationTests;
import ren.hankai.cordwood.plugin.advance.model.MyTbl1;
import ren.hankai.cordwood.plugin.advance.persist.MyTbl1Repository;

import java.util.List;

/**
 * 插件测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 9, 2016 4:49:18 PM
 */
public class AdvancePluginTest extends ApplicationTests {

  @Autowired
  private AdvancePlugin advancePlugin;

  @Autowired
  private MyTbl1Repository myTbl1Repo;

  @Test
  public void testAdd() throws Exception {
    advancePlugin.pluginDidLoad();
    final String result = advancePlugin.add(12, 11, null);
    Assert.assertNotNull(result);
    final List<MyTbl1> list = myTbl1Repo.findAll();
    Assert.assertTrue(list.size() == 1);
    final MyTbl1 record = list.get(0);
    Assert.assertEquals("12 + 11 = 23", record.getExp());
  }
}
