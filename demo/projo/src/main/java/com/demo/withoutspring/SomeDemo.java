
package com.demo.withoutspring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ren.hankai.cordwood.plugin.api.Functional;
import ren.hankai.cordwood.plugin.api.Pluggable;
import ren.hankai.cordwood.plugin.api.PluginLifeCycleAware;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Sep 30, 2016 3:51:07 PM
 */
@Pluggable(
    name = "demo",
    version = "1.0.0",
    description = "test only",
    readme = "http://www.baidu.com" )
public class SomeDemo implements PluginLifeCycleAware {

    private static final Logger logger = LoggerFactory.getLogger( SomeDemo.class );

    @Functional(
        name = "hello",
        resultType = "text/plain" )
    public String sayHello( HttpServletRequest request, HttpServletResponse response ) {
        int op1 = Integer.parseInt( request.getParameter( "op1" ) );
        int op2 = Integer.parseInt( request.getParameter( "op2" ) );
        logger.warn( "feature \"sayHello\" called" );
        return "Hi, the result is: " + ( op1 + op2 );
    }

    @Override
    public void pluginDidActivated() {
        logger.warn( "plugin activated" );
    }

    @Override
    public void pluginDidDeactivated() {
        logger.warn( "plugin deactivated" );
    }

    @Override
    public void pluginDidLoad() {
        logger.warn( "plugin did load" );
    }

    @Override
    public void pluginDidUnload() {
        logger.warn( "plugin did unload" );
    }
}
