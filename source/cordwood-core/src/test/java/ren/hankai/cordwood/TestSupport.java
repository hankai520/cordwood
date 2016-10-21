
package ren.hankai.cordwood;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

import ren.hankai.cordwood.core.ApplicationInitializer;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.domain.Plugin;
import ren.hankai.cordwood.core.domain.PluginPackage;
import ren.hankai.cordwood.plugin.PluginLoader;
import ren.hankai.cordwood.plugin.PluginManager;
import ren.hankai.cordwood.plugin.PluginRegistry;
import ren.hankai.cordwood.plugin.PluginValidator;

/**
 * @author hankai
 * @version 1.0.0
 * @since Oct 21, 2016 1:05:07 PM
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration(
    classes = { TestSupport.class } )
@ComponentScan(
    basePackages = { "ren.hankai" } )
public class TestSupport {

    @Autowired
    protected PluginRegistry  pluginRegistry;
    @Autowired
    protected PluginManager   pluginManager;
    @Autowired
    protected PluginLoader    pluginLoader;
    @Autowired
    protected PluginValidator pluginValidator;
    protected PluginPackage   pluginPackage;
    protected Plugin          plugin;

    @Before
    public void setup() throws Exception {
        System.setProperty( Preferences.ENV_APP_HOME_DIR, "./test-home" );
        Assert.assertTrue( ApplicationInitializer.initialize( "testSupport.txt" ) );
    }

    @After
    public void teardown() throws Exception {
        FileUtils.deleteDirectory( new File( Preferences.getHomeDir() ) );
    }
}
