
package ren.hankai.cordwood.plugin.impl;

import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ren.hankai.cordwood.core.domain.Plugin;
import ren.hankai.cordwood.plugin.PluginEventEmitter;
import ren.hankai.cordwood.plugin.PluginEventListener;

/**
 * 默认的插件事件发射器
 *
 * @author hankai
 * @version 1.0
 * @since Oct 8, 2016 5:00:48 PM
 */
@Component
public class DefaultPluginEventEmitter implements PluginEventEmitter {

    private final Map<String, Set<PluginEventListener>> registry = new HashMap<>();

    @Override
    public void addListener( String event, PluginEventListener listener ) {
        if ( ( event == null ) || ( event.length() == 0 ) ) {
            throw new InvalidParameterException( "event name must not be empty!" );
        }
        Set<PluginEventListener> listeners = registry.get( event );
        if ( listeners == null ) {
            listeners = new HashSet<>( 4 );
            registry.put( event, listeners );
        }
        if ( listener != null ) {
            listeners.add( listener );
        }
    }

    @Override
    public synchronized void removeListener( String event, PluginEventListener listener ) {
        if ( ( event == null ) || ( event.length() == 0 ) ) {
            throw new InvalidParameterException( "event name must not be empty!" );
        }
        Set<PluginEventListener> listeners = registry.get( event );
        if ( ( listeners != null ) && ( listener != null ) ) {
            listeners.remove( listener );
            if ( listeners.size() == 0 ) {
                registry.remove( event );
            }
        }
    }

    @Override
    public synchronized void removeListener( PluginEventListener listener ) {
        if ( listener == null ) {
            throw new InvalidParameterException( "event listener must not be null!" );
        }
        Set<PluginEventListener> listeners = null;
        for ( String event : registry.keySet() ) {
            listeners = registry.get( event );
            if ( ( listeners != null ) && ( listeners.size() > 0 ) ) {
                if ( listeners.contains( listener ) ) {
                    listeners.remove( listener );
                    if ( listeners.size() == 0 ) {
                        registry.remove( event );
                    }
                }
            }
        }
    }

    @Override
    public void emitEvent( String event, Plugin sender ) {
        if ( ( event == null ) || ( event.length() == 0 ) ) {
            throw new InvalidParameterException( "event name must not be empty!" );
        }
        Set<PluginEventListener> listeners = registry.get( event );
        if ( listeners != null ) {
            for ( PluginEventListener listener : listeners ) {
                if ( listener instanceof PluginEventListener ) {
                    listener.handleEvent( sender, event );
                }
            }
        }
    }
}
