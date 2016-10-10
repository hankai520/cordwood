
package com.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Oct 9, 2016 4:41:18 PM
 */
@Profile( "unit-test" )
@Controller
public class TestController {

    @Autowired
    private ApplicationContext ctx;

    @RequestMapping(
        value = "/{plugin_name}/{function}" )
    @ResponseBody
    public ResponseEntity<Object> testHello(
                    @PathVariable( "plugin_name" ) String pluginName,
                    @PathVariable( "function" ) String function,
                    HttpServletRequest request, HttpServletResponse response ) throws Exception {
        Object bean = ctx.getBean( pluginName );
        Method method = bean.getClass().getMethod( function, HttpServletRequest.class,
            HttpServletResponse.class );
        Object result = method.invoke( bean, request, response );
        return new ResponseEntity<>( result, HttpStatus.OK );
    }
}
