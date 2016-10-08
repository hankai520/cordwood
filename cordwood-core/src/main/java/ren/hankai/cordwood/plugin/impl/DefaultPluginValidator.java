
package ren.hankai.cordwood.plugin.impl;

import org.springframework.stereotype.Component;

import java.net.URL;

import ren.hankai.cordwood.plugin.PluginValidator;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Sep 30, 2016 4:07:10 PM
 */
@Component
public class DefaultPluginValidator implements PluginValidator {

    /*
     * (non-Javadoc)
     * @see ren.hankai.cordwood.plugin.PluginValidator#validatePackage(java.net.URL)
     */
    @Override
    public boolean validatePackage( URL jarUrl ) {
        return true;
    }
}
