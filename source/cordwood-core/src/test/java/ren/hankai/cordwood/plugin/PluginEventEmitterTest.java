
package ren.hankai.cordwood.plugin;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ren.hankai.cordwood.TestSupport;
import ren.hankai.cordwood.core.domain.Plugin;
import ren.hankai.cordwood.plugin.PluginEventEmitter;
import ren.hankai.cordwood.plugin.PluginEventListener;
import ren.hankai.cordwood.plugin.impl.DefaultPluginEventEmitter;

/**
 * @author hankai
 * @version 1.0.0
 * @since Oct 21, 2016 2:22:19 PM
 */
public class PluginEventEmitterTest extends TestSupport {

  @Autowired
  private PluginEventEmitter emitter;

  /**
   * Test method for
   * {@link ren.hankai.cordwood.plugin.impl.DefaultPluginEventEmitter#emitEvent(java.lang.String, ren.hankai.cordwood.core.domain.Plugin)}
   * .
   */
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
