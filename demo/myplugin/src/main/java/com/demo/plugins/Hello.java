
package com.demo.plugins;

import com.demo.plugins.persist.MyTbl1Repository;
import com.demo.plugins.persist.model.MyTbl1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

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
@Component( "hello" )
@Pluggable(
    name = "hello",
    version = "1.0.0",
    description = "test only",
    readme = "http://www.baidu.com" )
public class Hello implements PluginLifeCycleAware {

    private static final Logger logger = LoggerFactory.getLogger( Hello.class );
    @Autowired
    private MyTbl1Repository    tbl1Repo;

    @Functional(
        name = "add",
        resultType = "text/plain" )
    public String add( HttpServletRequest request, HttpServletResponse response ) {
        int op1 = Integer.parseInt( request.getParameter( "op1" ) );
        int op2 = Integer.parseInt( request.getParameter( "op2" ) );
        MyTbl1 mt = new MyTbl1();
        mt.setName( new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( new Date() ) );
        tbl1Repo.save( mt );
        logger.warn( "feature \"add\" called" );
        return op1 + op2 + "";
    }

    @Override
    public void pluginDidActivated() {
        // TODO Auto-generated method stub
    }

    @Override
    public void pluginDidDeactivated() {
        // TODO Auto-generated method stub
    }

    @Override
    public void pluginDidLoad() {
        // TODO Auto-generated method stub
    }

    @Override
    public void pluginDidUnload() {
        // TODO Auto-generated method stub
    }
}
