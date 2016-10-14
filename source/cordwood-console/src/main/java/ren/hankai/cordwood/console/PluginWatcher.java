
package ren.hankai.cordwood.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import ren.hankai.cordwood.core.Preferences;

/**
 * 插件目录观察器，监视插件目录的文件变化
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 14, 2016 4:20:38 PM
 */
public final class PluginWatcher extends Thread {

    private static final Logger  logger = LoggerFactory.getLogger( PluginWatcher.class );
    private boolean              shouldStop;
    private boolean              watching;
    private static PluginWatcher pluginWatcher;

    private PluginWatcher() {
    }

    private static PluginWatcher getInstance() {
        if ( pluginWatcher == null ) {
            pluginWatcher = new PluginWatcher();
            pluginWatcher.setName( "Plugin home watcher" );
        }
        return pluginWatcher;
    }

    public static void watch() {
        if ( getInstance().watching ) {
            return;
        }
        getInstance().shouldStop = false;
        getInstance().start();
        getInstance().watching = true;
    }

    public static void stopWatching() {
        if ( getInstance().watching ) {
            getInstance().shouldStop = true;
        }
    }

    @Override
    public void run() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path pluginFolder = Paths.get( Preferences.getPluginsDir() );
            pluginFolder.register( watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY );
            boolean valid = true;
            do {
                WatchKey watchKey = watchService.take();
                for ( WatchEvent<?> event : watchKey.pollEvents() ) {
                    String path = event.context().toString();
                    Kind<?> kind = event.kind();
                    if ( kind.equals( StandardWatchEventKinds.ENTRY_CREATE ) ) {
                        logger.info( "New File: " + path );
                    } else if ( kind.equals( StandardWatchEventKinds.ENTRY_DELETE ) ) {
                        logger.info( "Deleted File: " + path );
                    } else if ( kind.equals( StandardWatchEventKinds.ENTRY_MODIFY ) ) {
                        logger.info( "Modified File: " + path );
                    }
                    if ( shouldStop ) {
                        break;
                    }
                }
                valid = watchKey.reset();
            } while ( valid && !shouldStop );
        } catch (Exception e) {
        }
        watching = false;
    }
}
