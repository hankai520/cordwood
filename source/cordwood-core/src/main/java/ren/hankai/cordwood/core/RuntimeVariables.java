
package ren.hankai.cordwood.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DefaultPropertiesPersister;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 运行时变量，用于在程序运行期间读写一些配置，并可以将这些配置持久化。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jun 21, 2016 4:12:14 PM
 */
public final class RuntimeVariables {

    private static final Logger        logger    =
                                              LoggerFactory.getLogger( RuntimeVariables.class );
    private static Map<String, String> variables = null;
    // 运行时变量保存路径
    private static final String        savePath  = Preferences.getDataDir() + "/runtime.properties";

    private RuntimeVariables() {
    }

    private static Map<String, String> getVariables() {
        if ( variables == null ) {
            variables = new HashMap<String, String>();
            try {
                DefaultPropertiesPersister dpp = new DefaultPropertiesPersister();
                Properties props = new Properties();
                dpp.load( props, new FileReader( getVariablesFile() ) );
                Set<String> keyset = props.stringPropertyNames();
                for ( String key : keyset ) {
                    variables.put( key, props.getProperty( key ) );
                }
            } catch (FileNotFoundException e) {
                logger.error( String.format( "Runtime variables file \"%s\" not found!", savePath ),
                    e );
            } catch (IOException e) {
                logger.error(
                    String.format( "Failed to load runtime variables from file \"%s\"!", savePath ),
                    e );
            }
        }
        return variables;
    }

    public static File getVariablesFile() {
        return new File( savePath );
    }

    public static void saveVariables() {
        try {
            DefaultPropertiesPersister dpp = new DefaultPropertiesPersister();
            String header =
                          "These are the runtime variables for the application, do not change it manually!";
            Properties props = new Properties();
            props.putAll( variables );
            dpp.store( props, new FileWriter( savePath ), header );
        } catch (IOException e) {
            logger.error(
                String.format( "Failed to save runtime variables to file \"%s\"!", savePath ), e );
        }
    }

    public static void setVariable( String key, String value ) {
        getVariables().put( key, value );
    }

    public static String getVariable( String key ) {
        return getVariables().get( key );
    }

    public static boolean getBool( String key ) {
        String s = getVariable( key );
        try {
            return Boolean.parseBoolean( s );
        } catch (Exception e) {
            logger.warn( String.format( "Failed to get boolean variable \"%s\"", key ), e );
        }
        return false;
    }

    public static int getInt( String key ) {
        String s = getVariable( key );
        try {
            return Integer.parseInt( s );
        } catch (Exception e) {
            logger.warn( String.format( "Failed to get int variable \"%s\"", key ), e );
        }
        return 0;
    }

    public static Map<String, String> getAllVariables() {
        Map<String, String> map = new HashMap<>();
        map.putAll( variables );
        return map;
    }

    public static void setAllVariables( Map<String, String> map ) {
        if ( ( map != null ) && ( map.size() > 0 ) ) {
            variables = map;
        }
    }

    public static void reloadVariables() {
        variables = null;
        getVariables();
    }
}
