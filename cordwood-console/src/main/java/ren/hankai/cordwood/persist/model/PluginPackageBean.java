
package ren.hankai.cordwood.persist.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Oct 9, 2016 11:34:32 AM
 */
@Entity
@Table(
    name = "plugin_packages" )
@Cacheable( false )
public final class PluginPackageBean implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY )
    private Integer           id;
    private String            checksum;
    private String            fileName;
    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = CascadeType.PERSIST,
        mappedBy = "pluginPackage" )
    private List<PluginBean>  plugins          = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum( String checksum ) {
        this.checksum = checksum;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName( String fileName ) {
        this.fileName = fileName;
    }

    public List<PluginBean> getPlugins() {
        return plugins;
    }

    public void setPlugins( List<PluginBean> plugins ) {
        this.plugins = plugins;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
