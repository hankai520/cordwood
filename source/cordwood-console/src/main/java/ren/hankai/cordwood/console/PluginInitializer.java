
package ren.hankai.cordwood.console;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import ch.qos.logback.classic.Level;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.domain.Plugin;
import ren.hankai.cordwood.core.domain.PluginPackage;
import ren.hankai.cordwood.core.util.LogbackUtil;
import ren.hankai.cordwood.persist.PluginPackageRepository;
import ren.hankai.cordwood.persist.model.PluginBean;
import ren.hankai.cordwood.persist.model.PluginPackageBean;
import ren.hankai.cordwood.persist.util.EntitySpecs;
import ren.hankai.cordwood.plugin.PluginEventEmitter;
import ren.hankai.cordwood.plugin.PluginEventListener;
import ren.hankai.cordwood.plugin.PluginManager;
import ren.hankai.cordwood.plugin.PluginRegistry;

/**
 * 插件初始化器
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 9, 2016 11:31:14 AM
 */
@Component
public class PluginInitializer {

    private static final Logger     logger = LoggerFactory.getLogger( PluginInitializer.class );
    @Autowired
    private PluginPackageRepository pluginPackageRepository;
    @Autowired
    private PluginManager           pluginManager;
    @Autowired
    private PluginEventEmitter      pluginEventEmmitter;
    @Autowired
    private PluginRegistry          pluginRegistry;
    private PluginWatcher           pluginWatcher;

    @PostConstruct
    private void setupLoggersForPlugins() throws Exception {
        // 注册插件事件监听
        pluginEventEmmitter.addListener( PluginEventEmitter.PLUGIN_LOADED,
            new PluginEventListener() {

                @Override
                public void handleEvent( Plugin plugin, String eventType ) {
                    String clazz = plugin.getInstance().getClass().getName();
                    LogbackUtil.setupFileLoggerFor( clazz, Level.WARN, plugin.getName() + ".txt" );
                }
            } );
        pluginWatcher = new PluginWatcher();
        pluginWatcher.watch();
    }

    @PreDestroy
    private void destory() {
        pluginWatcher.stopWatching();
    }

    private String getChecksum( File file ) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream( file );
            return DigestUtils.sha1Hex( fis );
        } catch (Exception e) {
            logger.error( String.format( "Failed to calculate checksum of file \"%s\"",
                file.getAbsolutePath() ) );
        } finally {
            IOUtils.closeQuietly( fis );
        }
        return null;
    }

    /**
     * 安装插件包文件
     *
     * @param url 插件本地路径
     * @author hankai
     * @since Oct 14, 2016 1:18:16 PM
     */
    private void installPlugin( File file, String checksum ) {
        try {
            PluginPackageBean ppb = pluginPackageRepository
                .findOne( EntitySpecs.field( "checksum", checksum ) );
            if ( ppb == null ) {
                PluginPackage pp = pluginRegistry.register( file.toURI().toURL() );
                ppb = new PluginPackageBean();
                ppb.setChecksum( pp.getIdentifier() );
                ppb.setFileName( pp.getFileName() );
                for ( Plugin plugin : pp.getPlugins() ) {
                    PluginBean pb = new PluginBean();
                    pb.setActive( plugin.isActive() );
                    pb.setDescription( plugin.getDescription() );
                    pb.setName( plugin.getName() );
                    pb.setVersion( plugin.getVersion() );
                    pb.setPluginPackage( ppb );
                    ppb.getPlugins().add( pb );
                }
            }
            pluginPackageRepository.save( ppb );
        } catch (Exception e) {
        }
    }

    private void uninstallPlugin( String checksum ) {
        if ( pluginRegistry.isRegistered( checksum ) ) {
            pluginRegistry.unregister( checksum );
            PluginPackageBean ppb = pluginPackageRepository
                .findOne( EntitySpecs.field( "checksum", checksum ) );
            if ( ppb != null ) {
                pluginPackageRepository.delete( ppb.getId() );
            }
        }
    }

    /**
     * 注册尚未注册的物理插件包文件，通常用来安装通过文件复制而不是控制台途径安装的插件。
     *
     * @author hankai
     * @since Oct 14, 2016 11:53:38 AM
     */
    private void installCopiedPlugins() {
        File file = new File( Preferences.getPluginsDir() );
        File[] files = null;
        if ( file.exists() && file.isDirectory() ) {
            files = file.listFiles();
        }
        if ( ( files != null ) && ( files.length > 0 ) ) {
            for ( File pluginFile : files ) {
                if ( !FilenameUtils.isExtension( pluginFile.getName(), "jar" ) ) {
                    continue;
                }
                installPlugin( pluginFile, getChecksum( pluginFile ) );
            }
        }
    }

    /**
     * 读取在数据库中注册的插件信息，初始化已安装的插件。
     *
     * @author hankai
     * @since Oct 14, 2016 2:06:35 PM
     */
    @Scheduled(
        fixedRate = 1000 * 60,
        initialDelay = 1000 * 2 )
    public void initializeInstalledPlugins() {
        List<PluginPackageBean> list = pluginPackageRepository.findAll();
        if ( list != null ) {
            List<String> names = new ArrayList<>();
            for ( PluginPackageBean ppb : list ) {
                names.add( ppb.getFileName() );
            }
            pluginManager.initializePlugins( names );
        }
        installCopiedPlugins();
    }

    /**
     * 插件目录观察器，监视插件目录的文件变化
     *
     * @author hankai
     * @version 1.0.0
     * @since Oct 14, 2016 4:20:38 PM
     */
    private class PluginWatcher extends Thread {

        private final Logger logger = LoggerFactory.getLogger( PluginWatcher.class );
        private boolean      shouldStop;
        private boolean      watching;
        private WatchService watchService;

        public PluginWatcher() {
            shouldStop = false;
            watching = false;
            setName( "Plugin home watcher" );
            try {
                watchService = FileSystems.getDefault().newWatchService();
                Path pluginFolder = Paths.get( Preferences.getPluginsDir() );
                pluginFolder.register( watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY );
            } catch (IOException e) {
                logger.warn( "Failed to start plugin home watcher!", e );
            }
        }

        public void watch() {
            if ( watching ) {
                return;
            }
            shouldStop = false;
            start();
            watching = true;
        }

        public void stopWatching() {
            if ( watching ) {
                shouldStop = true;
            }
        }

        private void handleCreation( String path ) {
            File file = new File( path );
            if ( file.exists() && !file.isDirectory() ) {
                logger.info( "Detected new plugin package: " + file.getName() );
                String checksum = getChecksum( file );
                installPlugin( file, checksum );
            }
        }

        private void handleDeletion( String path ) {
            File file = new File( path );
            logger.info( "Detected deletion of plugin package: " + file.getName() );
            if ( !file.exists() ) {
                String fileName = FilenameUtils.getName( path );
                PluginPackageBean ppb = pluginPackageRepository
                    .findOne( EntitySpecs.field( "fileName", fileName ) );
                uninstallPlugin( ppb.getChecksum() );
            }
        }

        private void handleModification( String path ) {
            File file = new File( path );
            logger.info( "Detected modification of plugin package: " + file.getName() );
            if ( !file.exists() ) {
                String checksum = getChecksum( file );
                uninstallPlugin( checksum );
                installPlugin( file, checksum );
            }
        }

        @Override
        public void run() {
            while ( !shouldStop ) {
                try {
                    boolean valid = true;
                    String pluginHome = Preferences.getPluginsDir();
                    do {
                        WatchKey watchKey = watchService.take();
                        Map<String, Kind<?>> changes = new HashMap<>();
                        for ( WatchEvent<?> event : watchKey.pollEvents() ) {
                            String fileName = event.context().toString();
                            if ( !FilenameUtils.isExtension( fileName, "jar" ) ) {
                                continue;
                            }
                            Kind<?> kind = event.kind();
                            changes.put( fileName, kind );
                        }
                        for ( String fileName : changes.keySet() ) {
                            String path = pluginHome + File.separator + fileName;
                            Kind<?> kind = changes.get( fileName );
                            if ( kind.equals( StandardWatchEventKinds.ENTRY_CREATE ) ) {
                                handleCreation( path );
                            } else if ( kind.equals( StandardWatchEventKinds.ENTRY_DELETE ) ) {
                                handleDeletion( path );
                            } else if ( kind.equals( StandardWatchEventKinds.ENTRY_MODIFY ) ) {
                                handleModification( path );
                            }
                        }
                        valid = watchKey.reset();
                    } while ( valid );
                } catch (Exception e) {
                    logger.error( "Plugin home watcher error!", e );
                }
            }
            watching = false;
        }
    }
}
