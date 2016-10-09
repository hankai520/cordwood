
package ren.hankai.cordwood.plugin.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.domain.Plugin;
import ren.hankai.cordwood.core.domain.PluginFunction;
import ren.hankai.cordwood.core.domain.PluginPackage;
import ren.hankai.cordwood.plugin.PluginDownloader;
import ren.hankai.cordwood.plugin.PluginEventEmitter;
import ren.hankai.cordwood.plugin.PluginLoader;
import ren.hankai.cordwood.plugin.PluginManager;
import ren.hankai.cordwood.plugin.PluginRegistry;
import ren.hankai.cordwood.plugin.PluginValidator;
import ren.hankai.cordwood.plugin.api.Functional;
import ren.hankai.cordwood.plugin.api.Pluggable;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Sep 29, 2016 6:01:21 PM
 */
@Component
public class SpringPluginManager implements PluginManager, PluginRegistry {

    private static final Logger              logger   =
                                                    LoggerFactory
                                                        .getLogger( SpringPluginManager.class );
    @Autowired
    private PluginLoader                     pluginLoader;
    @Autowired
    private PluginValidator                  pluginValidator;
    @Autowired
    private PluginEventEmitter               pluginEventEmitter;
    @Autowired
    private PluginDownloader                 pluginDownloader;
    private final Map<String, Plugin>        plugins  = new HashMap<>();
    private final Map<String, PluginPackage> packages = new HashMap<>();

    private boolean changeActivationStatus( String pluginName, boolean active ) {
        Plugin plugin = plugins.get( pluginName );
        if ( plugin != null ) {
            plugin.setActive( active );
            plugins.put( plugin.getName(), plugin );
            if ( active ) {
                pluginEventEmitter.emitEvent( PluginEventEmitter.PLUGIN_ACTIVATED, plugin );
            } else {
                pluginEventEmitter.emitEvent( PluginEventEmitter.PLUGIN_DEACTIVATED, plugin );
            }
            return true;
        } else {
            logger.warn(
                String.format( "Attempting to %s nonexistent plugin: \"%s\"",
                    ( active ? "activate" : "deactivate" ), pluginName ) );
        }
        return false;
    }

    private Plugin wrapPlugin( Object pluginInstance ) {
        final Class<?> clazz = pluginInstance.getClass();
        Pluggable pluggable = clazz.getAnnotation( Pluggable.class );
        final Plugin plugin = new Plugin();
        plugin.setName( pluggable.name() );
        plugin.setVersion( pluggable.version() );
        plugin.setDescription( pluggable.description() );
        plugin.setInstance( pluginInstance );
        plugin.setActive( true );
        // 扫描插件标记的功能
        ReflectionUtils.doWithMethods( clazz, new ReflectionUtils.MethodCallback() {

            @Override
            public void doWith( final Method method )
                            throws IllegalArgumentException, IllegalAccessException {
                Functional functional = AnnotationUtils.getAnnotation( method, Functional.class );
                if ( functional != null ) {
                    PluginFunction function = new PluginFunction();
                    function.setMethod( method );
                    function.setName( functional.name() );
                    function.setResultType( functional.resultType() );
                    plugin.getFunctions().put( functional.name(), function );
                }
            }
        }, new ReflectionUtils.MethodFilter() {

            @Override
            public boolean matches( final Method method ) {
                return ( method.getDeclaringClass() == clazz );
            }
        } );
        return plugin;
    }

    private PluginPackage extractPackageInfo( URL localPath ) {
        InputStream is = null;
        try {
            PluginPackage p = new PluginPackage();
            p.setFileName( FilenameUtils.getName( localPath.getPath() ) );
            p.setInstallUrl( localPath );
            is = localPath.openStream();
            p.setIdentifier( DigestUtils.sha1Hex( is ) );
            return p;
        } catch (IOException e) {
            logger.error(
                String.format( "Failed to calculate the checksum of package \"%s\"", localPath ),
                e );
        } finally {
            try {
                if ( is != null ) {
                    is.close();
                }
            } catch (Exception e2) {
            }
        }
        return null;
    }

    @Override
    public PluginPackage register( URL packageUrl ) {
        URL localPath = pluginDownloader.downloadIfNeeded( packageUrl );
        if ( localPath == null ) {
            logger.error(
                String.format( "Failed to download plugin package at %s", packageUrl.toString() ) );
        } else if ( !pluginValidator.validatePackage( localPath ) ) {
            logger.error(
                String.format( "Failed to verify plugin package at %s", localPath.toString() ) );
        } else {
            PluginPackage pack = extractPackageInfo( localPath );
            PluginPackage loadedPack = packages.get( pack.getIdentifier() );
            if ( loadedPack != null ) {
                return loadedPack;
            }
            String name = FilenameUtils.getName( localPath.getPath() );
            logger.info( String.format( "Loading plugin package %s ...", name ) );
            List<Object> instances = pluginLoader.loadPlugins( localPath );
            if ( ( instances != null ) && !instances.isEmpty() ) {
                for ( Object instance : instances ) {
                    Plugin plugin = wrapPlugin( instance );
                    pack.addPlugin( plugin );
                    plugins.put( plugin.getName(), plugin );
                    pluginEventEmitter.emitEvent( PluginEventEmitter.PLUGIN_LOADED, plugin );
                }
                packages.put( pack.getIdentifier(), pack );
                logger.info( String.format( "Plugin package %s loaded successfully!", name ) );
                return pack;
            }
        }
        return null;
    }

    @Override
    public boolean unregister( String packageId ) {
        PluginPackage pp = packages.get( packageId );
        if ( pp != null ) {
            for ( Plugin p : pp.getPlugins() ) {
                pluginLoader.unloadPlugin( p.getInstance() );
                pluginEventEmitter.emitEvent( PluginEventEmitter.PLUGIN_UNLOADED, p );
            }
            plugins.remove( packageId );
            try {
                File file = new File( pp.getInstallUrl().toURI() );
                return FileUtils.deleteQuietly( file );
            } catch (Exception e) {
                logger.error( String.format( "Failed to unregister plugin package \"%s\"",
                    pp.getInstallUrl().toString() ), e );
            }
        }
        return false;
    }

    @Override
    public void initializePlugins( List<String> packageNames ) {
        File dir = new File( Preferences.getPluginsDir() );
        if ( dir.exists() && dir.isDirectory() ) {
            File[] plugins = dir.listFiles( new FilenameFilter() {

                @Override
                public boolean accept( File dir, String name ) {
                    return packageNames.contains( name );
                }
            } );
            if ( ( plugins != null ) && ( plugins.length > 0 ) ) {
                for ( File file : plugins ) {
                    try {
                        register( file.toURI().toURL() );
                    } catch (MalformedURLException e) {
                        logger.warn( String.format( "Invalid plugin package url: \"%s\"",
                            file.getAbsolutePath() ), e );
                    }
                }
            }
        }
    }

    @Override
    public boolean activatePlugin( String pluginName ) {
        return changeActivationStatus( pluginName, true );
    }

    @Override
    public boolean deactivatePlugin( String pluginName ) {
        return changeActivationStatus( pluginName, false );
    }

    @Override
    public Plugin getPlugin( String pluginName ) {
        Plugin plugin = plugins.get( pluginName );
        if ( plugin == null ) {
            logger.error( String.format( "Plugin with name \"%s\" not found!", pluginName ) );
        }
        return plugin;
    }
}
