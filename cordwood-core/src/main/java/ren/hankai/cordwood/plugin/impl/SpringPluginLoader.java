
package ren.hankai.cordwood.plugin.impl;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import ren.hankai.cordwood.plugin.PluginLoader;
import ren.hankai.cordwood.plugin.api.Pluggable;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Sep 30, 2016 8:55:55 AM
 */
@Component
public class SpringPluginLoader implements PluginLoader {

    private static final Logger                          logger  = LoggerFactory
        .getLogger( SpringPluginLoader.class );
    @Autowired
    private ApplicationContext                           context;
    private final Map<String, GenericApplicationContext> plugins = new HashMap<>();

    private Attributes parseJarManifest( URLClassLoader loader ) {
        Attributes attrs = null;
        JarInputStream jarStream = null;
        try {
            URL[] urls = loader.getURLs();
            if ( ( urls != null ) && ( urls.length > 0 ) ) {
                URL url = urls[0];
                InputStream is = url.openStream();
                jarStream = new JarInputStream( is );
                Manifest manifest = jarStream.getManifest();
                attrs = manifest.getMainAttributes();
            }
        } catch (IOException e) {
            logger.error( String.format( "Failed to read manifest of plugin at urls: %s",
                loader.getURLs().toString() ) );
        } finally {
            if ( jarStream != null ) {
                try {
                    jarStream.close();
                } catch (IOException e) {
                }
            }
        }
        return attrs;
    }

    private AnnotationConfigApplicationContext loadSpringContext( Attributes attrs,
                    URLClassLoader loader ) {
        if ( ( attrs != null ) && !attrs.isEmpty() ) {
            String bootClassName = attrs.getValue( "Boot-Class" );
            try {
                Class<?> bootClass = Class.forName( bootClassName, true, loader );
                if ( bootClass != null ) {
                    AnnotationConfigApplicationContext ctx =
                                                           new AnnotationConfigApplicationContext();
                    ctx.setParent( context );
                    ctx.setClassLoader( loader );
                    ctx.register( bootClass );
                    ctx.refresh();
                    return ctx;
                }
            } catch (ClassNotFoundException e) {
                logger.error( String.format( "Boot class not found in plugin : %s",
                    loader.getURLs().toString() ) );
            }
        }
        return null;
    }

    private List<Object> loadPluginInstances( URLClassLoader loader ) {
        List<Object> instances = new ArrayList<>();
        Attributes attrs = parseJarManifest( loader );
        AnnotationConfigApplicationContext ctx = loadSpringContext( attrs, loader );
        if ( ctx != null ) {
            String str = attrs.getValue( "Plugin-Packages" );
            if ( !StringUtils.isEmpty( str ) ) {
                String[] packages = str.split( "," );
                Reflections reflections = null;
                for ( String packageName : packages ) {
                    reflections = new Reflections( packageName, loader );
                    Set<Class<?>> pluginClasses = reflections
                        .getTypesAnnotatedWith( Pluggable.class );
                    for ( Class<?> clazz : pluginClasses ) {
                        Object obj = ctx.getBean( clazz );
                        if ( obj != null ) {
                            Pluggable pluggable = clazz.getAnnotation( Pluggable.class );
                            plugins.put( pluggable.name(), ctx );
                            instances.add( obj );
                        }
                    }
                }
            }
        }
        return instances;
    }

    @Override
    public List<Object> loadPlugins( URL jarFileUrl ) {
        URLClassLoader loader = new URLClassLoader( new URL[] { jarFileUrl },
            context.getClassLoader() );
        return loadPluginInstances( loader );
    }

    @Override
    public boolean unloadPlugin( Object instance ) {
        Pluggable pluggable = instance.getClass().getAnnotation( Pluggable.class );
        GenericApplicationContext ctx = plugins.get( pluggable.name() );
        if ( ctx != null ) {
            plugins.remove( pluggable.name() );
            BeanDefinitionRegistry registry = ctx;
            String[] names = ctx.getBeanNamesForType( instance.getClass() );
            if ( ( names != null ) && ( names.length > 0 ) ) {
                for ( String beanName : names ) {
                    registry.removeBeanDefinition( beanName );
                }
            }
            Map<String, Object> beans = ctx.getBeansWithAnnotation( Pluggable.class );
            if ( ( beans == null ) || ( beans.size() == 0 ) ) {
                ctx.setParent( null );
                ctx.close();
            }
            return true;
        } else {
            logger.error( String.format( "Plugin with name \"%s\" not found!", pluggable.name() ) );
        }
        return false;
    }
}
