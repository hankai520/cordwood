
package ren.hankai.cordwood.plugin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ren.hankai.cordwood.core.domain.Plugin;
import ren.hankai.cordwood.plugin.PluginEventEmitter;
import ren.hankai.cordwood.plugin.PluginEventListener;
import ren.hankai.cordwood.plugin.api.PluginLifeCycleAware;

import javax.annotation.PostConstruct;

/**
 * 插件生命周期时间通知器，用于将插件实现方和插件事件发射机制解耦。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 13, 2016 9:21:40 AM
 */
@Component
public class PluginLifeCycleNotifier implements PluginEventListener {

  @Autowired
  private PluginEventEmitter eventEmitter;

  private Plugin lastEventSender;

  private String lastEventType;

  @PostConstruct
  private void observePluginEvents() {
    eventEmitter.addListener(PluginEventEmitter.PLUGIN_LOADED, this);
    eventEmitter.addListener(PluginEventEmitter.PLUGIN_UNLOADED, this);
    eventEmitter.addListener(PluginEventEmitter.PLUGIN_ACTIVATED, this);
    eventEmitter.addListener(PluginEventEmitter.PLUGIN_DEACTIVATED, this);
  }

  @Override
  public void handleEvent(Plugin plugin, String eventType) {
    lastEventSender = plugin;
    lastEventType = eventType;
    if (plugin.getInstance() instanceof PluginLifeCycleAware) {
      final PluginLifeCycleAware plca = (PluginLifeCycleAware) plugin.getInstance();
      if (eventType == PluginEventEmitter.PLUGIN_LOADED) {
        plca.pluginDidLoad();
      } else if (eventType == PluginEventEmitter.PLUGIN_UNLOADED) {
        plca.pluginDidUnload();
      } else if (eventType == PluginEventEmitter.PLUGIN_ACTIVATED) {
        plca.pluginDidActivated();
      } else if (eventType == PluginEventEmitter.PLUGIN_DEACTIVATED) {
        plca.pluginDidDeactivated();
      }
    }
  }

  public Plugin getLastEventSender() {
    return lastEventSender;
  }

  public String getLastEventType() {
    return lastEventType;
  }

}
