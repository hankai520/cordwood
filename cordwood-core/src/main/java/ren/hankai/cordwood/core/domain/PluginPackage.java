
package ren.hankai.cordwood.core.domain;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Oct 9, 2016 10:17:19 AM
 */
public final class PluginPackage implements Serializable {

    private static final long  serialVersionUID = 1L;
    private String             identifier;
    private String             fileName;
    private URL                installUrl;
    private final List<Plugin> plugins          = new ArrayList<>();

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier( String identifier ) {
        this.identifier = identifier;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName( String fileName ) {
        this.fileName = fileName;
    }

    public URL getInstallUrl() {
        return installUrl;
    }

    public void setInstallUrl( URL installUrl ) {
        this.installUrl = installUrl;
    }

    public void addPlugin( Plugin plugin ) {
        plugins.add( plugin );
    }

    public void removePlugin( Plugin plugin ) {
        plugins.remove( plugin );
    }

    public List<Plugin> getPlugins() {
        return new ArrayList<>( plugins );
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
