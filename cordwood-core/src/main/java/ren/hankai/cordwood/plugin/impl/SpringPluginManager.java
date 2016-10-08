
package ren.hankai.cordwood.plugin.impl;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.domain.Plugin;
import ren.hankai.cordwood.core.domain.PluginFunction;
import ren.hankai.cordwood.persist.PluginRepository;
import ren.hankai.cordwood.persist.util.EntitySpecs;
import ren.hankai.cordwood.plugin.PluginLoader;
import ren.hankai.cordwood.plugin.PluginManager;
import ren.hankai.cordwood.plugin.PluginValidator;
import ren.hankai.cordwood.plugin.api.Functional;
import ren.hankai.cordwood.plugin.api.Pluggable;
import ren.hankai.cordwood.plugin.api.PluginLifeCycleAware;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Sep 29, 2016 6:01:21 PM
 */
@Component
public class SpringPluginManager implements PluginManager {

    private static final Logger       logger  =
                                             LoggerFactory.getLogger( SpringPluginManager.class );
    @Autowired
    private PluginLoader              pluginLoader;
    @Autowired
    private PluginValidator           pluginValidator;
    @Autowired
    private PluginRepository          pluginRepository;
    private final Map<String, Plugin> plugins = new HashMap<>();

    private boolean changeActivationStatus( String pluginName, boolean active ) {
        Plugin plugin = plugins.get( pluginName );
        if ( plugin != null ) {
            plugin.setActive( active );
            plugin = pluginRepository.save( plugin );
            plugins.put( plugin.getName(), plugin );
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

    @Override
    public boolean installPlugin( URL pluginUrl ) {
        if ( pluginValidator.validatePackage( pluginUrl ) ) {
            List<Object> instances = pluginLoader.loadPlugins( pluginUrl );
            if ( ( instances != null ) && !instances.isEmpty() ) {
                for ( Object instance : instances ) {
                    PluginLifeCycleAware aware = null;
                    if ( instance instanceof PluginLifeCycleAware ) {
                        aware = (PluginLifeCycleAware) instance;
                    }
                    Plugin plugin = wrapPlugin( instance );
                    Plugin existPlugin = pluginRepository
                        .findOne( EntitySpecs.<Plugin>field( "name", plugin.getName() ) );
                    if ( existPlugin == null ) {
                        plugin = pluginRepository.save( plugin );
                    }
                    plugins.put( plugin.getName(), plugin );
                    aware.pluginDidLoad();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean uninstallPlugin( String pluginId ) {
        // 从内存中卸载
        // 在数据库中删除插件信息
        // 删除插件包物理文件
        return false;
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

    @Override
    public void initializeInstalledPlugins() {
        // 读取数据库中所有的插件信息
        // 扫描插件包物理文件（忽略未在数据库记录的插件；忽略插件包不存在的插件）
        // 将插件包载入内存
        File dir = new File( Preferences.getPluginsDir() );
        if ( dir.exists() && dir.isDirectory() ) {
            File[] plugins = dir.listFiles();
            if ( ( plugins != null ) && ( plugins.length > 0 ) ) {
                for ( File file : plugins ) {
                    if ( !FilenameUtils.isExtension( file.getName(), "jar" ) ) {
                        continue;
                    }
                    try {
                        logger.info(
                            String.format( "Loading plugin package %s ...", file.getName() ) );
                        installPlugin( file.toURI().toURL() );
                        logger.info( String.format( "Plugin package %s loaded successfully!",
                            file.getName() ) );
                    } catch (MalformedURLException e) {
                        logger.warn( String.format( "Failed to load plugin package at \"%s\"",
                            file.getAbsolutePath() ), e );
                    }
                }
            }
        }
    }

    @Override
    public Iterator<Plugin> getPluginIterator() {
        return new Iterator<Plugin>() {

            @Override
            public boolean hasNext() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Plugin next() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void remove() {
                // TODO Auto-generated method stub
            }
        };
    }
}
