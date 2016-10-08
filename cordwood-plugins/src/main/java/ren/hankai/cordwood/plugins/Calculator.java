
package ren.hankai.cordwood.plugins;

import org.springframework.stereotype.Component;

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
@Component
@Pluggable(
    name = "cordwood_calc",
    version = "1.0.0",
    description = "test only",
    readme = "http://www.baidu.com" )
public class Calculator implements PluginLifeCycleAware {

    @Override
    public void pluginDidLoad() {
        // TODO Auto-generated method stub
    }

    @Override
    public void pluginDidDestroy() {
        // TODO Auto-generated method stub
    }

    @Functional(
        name = "add",
        resultType = "text/plain" )
    public String add( HttpServletRequest request, HttpServletResponse response ) {
        int op1 = Integer.parseInt( request.getParameter( "op1" ) );
        int op2 = Integer.parseInt( request.getParameter( "op2" ) );
        return op1 + op2 + "";
    }
}
