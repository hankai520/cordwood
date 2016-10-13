
package ren.hankai.cordwood.console.service;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import ren.hankai.cordwood.core.domain.Plugin;
import ren.hankai.cordwood.core.domain.PluginPackage;
import ren.hankai.cordwood.persist.PluginPackageRepository;
import ren.hankai.cordwood.persist.model.PluginBean;
import ren.hankai.cordwood.persist.model.PluginPackageBean;
import ren.hankai.cordwood.plugin.PluginRegistry;

/**
 * 插件业务逻辑
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 13, 2016 1:20:47 PM
 */
@Component
public class PluginService {

    private static final Logger     logger = LoggerFactory.getLogger( PluginService.class );
    @Autowired
    private PluginRegistry          pluginRegistry;
    @Autowired
    private PluginPackageRepository pluginPackageRepository;

    public boolean installPlugin( URL url ) {
        try {
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
        } catch (Exception e) {
            logger.error( String.format( "Failed to install plugin from: %s", url.toString() ),
                e );
        }
        return false;
    }

    /**
     * 插件包上传
     *
     * @param tempPath
     * @return
     * @author hankai
     * @since Oct 13, 2016 1:28:21 PM
     */
    public boolean installPluginFromLocal( String tempPath ) {
        File file = new File( tempPath );
        if ( file.exists() ) {
            try {
                URL url = file.toURI().toURL();
                if ( installPlugin( url ) ) {
                    FileUtils.deleteQuietly( file );
                    return true;
                }
            } catch (MalformedURLException e) {
                logger.error( "Failed to get url of plugin file!", e );
            }
        }
        return false;
    }

    public boolean installPluginFromWeb( String webUrl ) {
        URL url = null;
        try {
            url = new URL( webUrl );
        } catch (MalformedURLException e) {
        }
        if ( url != null ) {
            return installPlugin( url );
        } else {
            logger.error( "Failed to install plugin from web due to invalid web url!" );
        }
        return false;
    }
}
