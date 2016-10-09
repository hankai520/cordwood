
package ren.hankai.cordwood.console.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ren.hankai.cordwood.persist.PluginPackageRepository;
import ren.hankai.cordwood.plugin.PluginRegistry;

/**
 * @author hankai
 * @version 0.0.1
 * @since Sep 28, 2016 11:19:57 AM
 */
@Controller
public class DefaultController {

    @Autowired
    private PluginPackageRepository pluginPackageRepository;
    @Autowired
    private PluginRegistry          pluginRegistry;

    @RequestMapping( "/" )
    @ResponseBody
    public String home() throws Exception {
        // URL url = new URL( "file:///Users/hankai/Desktop/cordwood-plugins-0.0.1.RELEASE.jar" );
        // PluginPackage pp = pluginRegistry.register( url );
        // PluginPackageBean ppb = new PluginPackageBean();
        // ppb.setChecksum( pp.getIdentifier() );
        // ppb.setFileName( pp.getFileName() );
        // for ( Plugin plugin : pp.getPlugins() ) {
        // PluginBean pb = new PluginBean();
        // pb.setActive( plugin.isActive() );
        // pb.setDescription( plugin.getDescription() );
        // pb.setName( plugin.getName() );
        // pb.setVersion( plugin.getVersion() );
        // pb.setPluginPackage( ppb );
        // ppb.getPlugins().add( pb );
        // }
        // pluginPackageRepository.save( ppb );
        return "hello";
    }
}
