
package ren.hankai.cordwood.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import ch.qos.logback.classic.Level;
import ren.hankai.cordwood.core.domain.Plugin;
import ren.hankai.cordwood.core.util.LogbackUtil;
import ren.hankai.cordwood.persist.PluginPackageRepository;
import ren.hankai.cordwood.persist.model.PluginPackageBean;
import ren.hankai.cordwood.plugin.PluginEventEmitter;
import ren.hankai.cordwood.plugin.PluginEventListener;
import ren.hankai.cordwood.plugin.PluginManager;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Oct 9, 2016 11:31:14 AM
 */
@Component
public class PluginInitializer {

    @Autowired
    private PluginPackageRepository pluginPackageRepository;
    @Autowired
    private PluginManager           pluginManager;
    @Autowired
    private PluginEventEmitter      pluginEventEmmitter;

    @PostConstruct
    private void setupLoggersForPlugins() {
        pluginEventEmmitter.addListener( PluginEventEmitter.PLUGIN_LOADED,
            new PluginEventListener() {

                @Override
                public void handleEvent( Plugin plugin, String eventType ) {
                    String clazz = plugin.getInstance().getClass().getName();
                    LogbackUtil.setupFileLoggerFor( clazz, Level.WARN, plugin.getName() + ".txt" );
                }
            } );
    }

    @Scheduled(
        fixedRate = 1000 * 60 * 30,
        initialDelay = 1000 * 2 )
    public synchronized void initializeInstalledPlugins() {
        List<PluginPackageBean> list = pluginPackageRepository.findAll();
        if ( list != null ) {
            List<String> names = new ArrayList<>();
            for ( PluginPackageBean ppb : list ) {
                names.add( ppb.getFileName() );
            }
            pluginManager.initializePlugins( names );
        }
    }
}
