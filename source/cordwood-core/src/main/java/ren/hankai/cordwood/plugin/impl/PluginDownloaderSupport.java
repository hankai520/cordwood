
package ren.hankai.cordwood.plugin.impl;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import ren.hankai.cordwood.core.Preferences;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Oct 8, 2016 5:44:03 PM
 */
public abstract class PluginDownloaderSupport {

    private static final Logger logger = LoggerFactory.getLogger( PluginDownloaderSupport.class );

    private String convertToLocalPath( URL url ) {
        String protocal = url.getProtocol().toLowerCase();
        if ( protocal.equals( "file" ) ) {
            String name = FilenameUtils.getName( url.getPath() );
            return Preferences.getPluginsDir() + File.separator + name;
        }
        return null;
    }

    protected URL copyPluginToLocal( URL url ) {
        String path = convertToLocalPath( url );
        if ( path != null ) {
            InputStream is = null;
            OutputStream os = null;
            try {
                is = url.openStream();
                os = new FileOutputStream( path );
                FileCopyUtils.copy( url.openStream(), os );
                return new URL( "file://" + path );
            } catch (Exception e) {
                logger.error( "Failed to copy plugin package to local!", e );
                logger.error( String.format( "Target package url is \"%s\"", url.toString() ) );
            } finally {
                try {
                    if ( is != null ) {
                        is.close();
                    }
                    if ( os != null ) {
                        os.close();
                    }
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    protected boolean checkIfInstalled( URL url ) {
        String name = FilenameUtils.getName( url.getPath() );
        File file = new File( Preferences.getPluginsDir() + File.separator + name );
        boolean installed = file.exists() && !file.isDirectory();
        return installed;
    }
}
