
package ren.hankai.cordwood.plugin.impl;

import org.springframework.stereotype.Component;

import java.net.URL;
import java.security.InvalidParameterException;

import ren.hankai.cordwood.plugin.PluginDownloader;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Oct 8, 2016 5:41:11 PM
 */
@Component
public class HttpPluginDownloader extends PluginDownloaderSupport implements PluginDownloader {

    @Override
    public URL downloadIfNeeded( URL url ) {
        if ( url == null ) {
            throw new InvalidParameterException( "Cannot download plugin from null url" );
        }
        if ( !checkIfInstalled( url ) ) {
            URL localUrl = copyPluginToLocal( url );
            if ( localUrl == null ) {
                String protocal = url.getProtocol().toLowerCase();
                if ( protocal.equals( "http" ) || protocal.equals( "https" ) ) {
                    // TODO: 通过网络下载插件包
                }
                return null;
            } else {
                return localUrl;
            }
        } else {
            return url;
        }
    }
}
