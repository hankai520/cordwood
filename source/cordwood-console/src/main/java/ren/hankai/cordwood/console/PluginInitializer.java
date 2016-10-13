
package ren.hankai.cordwood.console;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

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
 * 插件初始化器，定时扫描插件安装目录，并初始化已注册的插件。若发现有未注册的插件包，则自动
 * 完成插件的安装和注册，并在结束后载入内存。
 *
 * @author hankai
 * @version 1.0.0
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
    @Autowired
    private PluginRegistry          pluginRegistry;

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

    private void installPlugin( URL url ) {
        PluginPackage pp = pluginRegistry.register( url );
        PluginPackageBean ppb = new PluginPackageBean();
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
        pluginPackageRepository.save( ppb );
    }

    @Scheduled(
        fixedRate = 1000 * 60,
        initialDelay = 1000 * 10 )
    public synchronized void installUnregisteredPlugins() {
        /*
         * 具体加载工作，还是会交由 initializeInstalledPlugins() 来完成，这里只需要将插件信息注册到数据库即可。
         */
        File file = new File( Preferences.getPluginsDir() );
        if ( file.exists() && file.isDirectory() ) {
            File[] files = file.listFiles();
            if ( ( files != null ) && ( files.length > 0 ) ) {
                FileInputStream fis = null;
                String checksum = null;
                PluginPackageBean ppb = null;
                for ( File pluginFile : files ) {
                    if ( !FilenameUtils.isExtension( pluginFile.getName(), "jar" ) ) {
                        continue;
                    }
                    try {
                        fis = new FileInputStream( pluginFile );
                        checksum = DigestUtils.sha1Hex( fis );
                        ppb = pluginPackageRepository
                            .findOne( EntitySpecs.field( "checksum", checksum ) );
                        if ( ppb == null ) {
                            installPlugin( pluginFile.toURI().toURL() );
                        }
                    } catch (Exception e) {
                    } finally {
                        if ( fis != null ) {
                            try {
                                fis.close();
                            } catch (IOException e) {
                            }
                        }
                    }
                }
            }
        }
    }

    @Scheduled(
        fixedRate = 1000 * 60 * 60 * 24,
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
