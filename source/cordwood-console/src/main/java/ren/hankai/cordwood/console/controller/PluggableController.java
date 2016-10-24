
package ren.hankai.cordwood.console.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ren.hankai.cordwood.core.domain.Plugin;
import ren.hankai.cordwood.core.domain.PluginFunction;
import ren.hankai.cordwood.core.util.PathUtil;
import ren.hankai.cordwood.plugin.PluginManager;
import ren.hankai.cordwood.plugin.api.PluginResourceLoader;

/**
 * 插件访问分发控制器
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 29, 2016 5:28:48 PM
 */
@Controller
public class PluggableController {

    private static final Logger logger = LoggerFactory.getLogger( PluggableController.class );

    @Autowired
    private PluginManager       pluginManager;

    @RequestMapping(
        value = "/service/{plugin_name}/{function}" )
    @ResponseBody
    public ResponseEntity<Object> dispatchPluginRequest(
                    @PathVariable( "plugin_name" ) String pluginName,
                    @PathVariable( "function" ) String function,
                    HttpServletRequest request, HttpServletResponse response ) {
        try {
            Plugin plugin = pluginManager.getPlugin( pluginName );
            if ( plugin == null ) {
                return new ResponseEntity<>( "service not found!", HttpStatus.NOT_FOUND );
            } else if ( !plugin.isActive() ) {
                return new ResponseEntity<>( "service not enabled!", HttpStatus.OK );
            } else {
                PluginFunction fun = plugin.getFunctions().get( function );
                Class<?>[] paramTypes = fun.getMethod().getParameterTypes();
                List<Object> args = new ArrayList<>();
                if ( ( paramTypes != null ) && ( paramTypes.length > 0 ) ) {
                    for ( Class<?> paramType : paramTypes ) {
                        if ( paramType.isInstance( request ) ) {
                            args.add( request );
                        } else if ( paramType.isInstance( response ) ) {
                            args.add( response );
                        }
                    }
                }
                Object result = fun.getMethod().invoke( plugin.getInstance(), args.toArray() );
                return new ResponseEntity<>( result, HttpStatus.OK );
            }
        } catch (Exception e) {
            logger.error(
                String.format( "Failed to call plugin { %s#%s }", pluginName, function ), e );
        } catch (Error e) {
            logger.error(
                String.format( "Failed to call plugin { %s#%s }", pluginName, function ), e );
        }
        return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR );
    }

    @RequestMapping(
        value = "/resources/{plugin_name}/**" )
    @ResponseBody
    public ResponseEntity<Object> dispatchPluginResourceRequest(
                    @PathVariable( "plugin_name" ) String pluginName,
                    HttpServletRequest request ) {
        String resourcePath = PathUtil.parseResourcePath( request.getRequestURI() );
        try {
            Plugin plugin = pluginManager.getPlugin( pluginName );
            if ( plugin == null ) {
                return new ResponseEntity<>( "service not found!", HttpStatus.NOT_FOUND );
            } else if ( !plugin.isActive() ) {
                return new ResponseEntity<>( "service not enabled!", HttpStatus.OK );
            } else if ( !( plugin.getInstance() instanceof PluginResourceLoader ) ) {
                return new ResponseEntity<>( "service has no resources!", HttpStatus.NOT_FOUND );
            } else {
                PluginResourceLoader resourceLoader = (PluginResourceLoader) plugin.getInstance();
                InputStream is = resourceLoader.getResource( resourcePath );
                if ( is != null ) {
                    InputStreamResource isr = new InputStreamResource( is );
                    return new ResponseEntity<Object>( isr, HttpStatus.OK );
                } else {
                    return new ResponseEntity<Object>( "Resource not found", HttpStatus.NOT_FOUND );
                }
            }
        } catch (Exception e) {
            logger.error(
                String.format( "Resource not found! Plugin is \"%s\", resource is \"%s\"", pluginName, resourcePath ),
                e );
        } catch (Error e) {
            logger.error(
                String.format( "Resource not found! Plugin is \"%s\", resource is \"%s\"", pluginName, resourcePath ),
                e );
        }
        return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR );
    }
}
