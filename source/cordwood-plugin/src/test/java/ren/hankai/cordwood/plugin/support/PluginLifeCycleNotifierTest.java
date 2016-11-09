package ren.hankai.cordwood.plugin.support;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ren.hankai.cordwood.plugin.Plugin;
import ren.hankai.cordwood.plugin.api.PluginEventEmitter;
import ren.hankai.cordwood.plugin.support.PluginLifeCycleNotifier;
import ren.hankai.cordwood.plugin.test.PluginTestSupport;

/**
 * 插件生命周期通知器测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 28, 2016 11:31:52 AM
 */
public class PluginLifeCycleNotifierTest extends PluginTestSupport {

  @Autowired
  private PluginEventEmitter pluginEventEmitter;

  @Autowired
  private PluginLifeCycleNotifier pluginLifeCycleNotifier;

  @Test
  public void testHandleEvent() {
    Plugin plugin = new Plugin();
    pluginEventEmitter.emitEvent(PluginEventEmitter.PLUGIN_LOADED, plugin);
    Assert.assertTrue(pluginLifeCycleNotifier.getLastEventSender() == plugin);
    Assert.assertEquals(PluginEventEmitter.PLUGIN_LOADED,
        pluginLifeCycleNotifier.getLastEventType());

    plugin = new Plugin();
    pluginEventEmitter.emitEvent(PluginEventEmitter.PLUGIN_UNLOADED, plugin);
    Assert.assertTrue(pluginLifeCycleNotifier.getLastEventSender() == plugin);
    Assert.assertEquals(PluginEventEmitter.PLUGIN_UNLOADED,
        pluginLifeCycleNotifier.getLastEventType());

    plugin = new Plugin();
    pluginEventEmitter.emitEvent(PluginEventEmitter.PLUGIN_ACTIVATED, plugin);
    Assert.assertTrue(pluginLifeCycleNotifier.getLastEventSender() == plugin);
    Assert.assertEquals(PluginEventEmitter.PLUGIN_ACTIVATED,
        pluginLifeCycleNotifier.getLastEventType());

    plugin = new Plugin();
    pluginEventEmitter.emitEvent(PluginEventEmitter.PLUGIN_DEACTIVATED, plugin);
    Assert.assertTrue(pluginLifeCycleNotifier.getLastEventSender() == plugin);
    Assert.assertEquals(PluginEventEmitter.PLUGIN_DEACTIVATED,
        pluginLifeCycleNotifier.getLastEventType());
  }
}
