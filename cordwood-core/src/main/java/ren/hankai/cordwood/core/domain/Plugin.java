
package ren.hankai.cordwood.core.domain;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Sep 30, 2016 11:04:40 AM
 */
@Entity
@Table(
    name = "plugins" )
@Cacheable( false )
public final class Plugin implements Serializable {

    private static final long           serialVersionUID = 1L;
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY )
    private BigInteger                  id;
    private String                      name;
    private String                      version;
    private String                      description;
    private boolean                     isActive;
    @Transient
    private Object                      instance;
    @Transient
    private Map<String, PluginFunction> functions        = new HashMap<>();

    public BigInteger getId() {
        return id;
    }

    public void setId( BigInteger id ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion( String version ) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive( boolean isActive ) {
        this.isActive = isActive;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance( Object instance ) {
        this.instance = instance;
    }

    public Map<String, PluginFunction> getFunctions() {
        return functions;
    }

    public void setFunctions( Map<String, PluginFunction> functions ) {
        this.functions = functions;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
