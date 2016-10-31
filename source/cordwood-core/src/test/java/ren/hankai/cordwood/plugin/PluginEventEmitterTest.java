
package ren.hankai.cordwood.plugin;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ren.hankai.cordwood.TestSupport;
import ren.hankai.cordwood.core.domain.Plugin;

/**
 * 插件事件发射器测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 21, 2016 2:22:19 PM
 */
public class PluginEventEmitterTest extends TestSupport {

  @Autowired
  private PluginEventEmitter emitter;

  @Test
  public void testEmitEvent() {
    emitter.addListener("testEvent", new PluginEventListener() {

      @Override
      public void handleEvent(Plugin plugin, String event) {
        Assert.assertTrue(event.equals("testEvent"));
      }
    });
    emitter.emitEvent("testEvent", new Plugin());
  }
}
