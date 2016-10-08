
package ren.hankai.cordwood.core.domain;

import java.lang.reflect.Method;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Sep 30, 2016 1:41:55 PM
 */
public final class PluginFunction {

    private String name;
    private Method method;
    private String resultType;

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod( Method method ) {
        this.method = method;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType( String resultType ) {
        this.resultType = resultType;
    }
}
