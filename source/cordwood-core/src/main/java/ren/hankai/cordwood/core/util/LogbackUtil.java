
package ren.hankai.cordwood.core.util;

import org.slf4j.LoggerFactory;

import java.io.File;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ren.hankai.cordwood.core.Preferences;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Oct 10, 2016 2:22:42 PM
 */
public class LogbackUtil {

    private static ConsoleAppender<ILoggingEvent> getConsoleAppender( LoggerContext lc ) {
        ConsoleAppender<ILoggingEvent> ca = new ConsoleAppender<ILoggingEvent>();
        ca.setContext( lc );
        ca.setName( "console" );
        LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<ILoggingEvent>();
        encoder.setContext( lc );
        PatternLayout layout = new PatternLayout();
        layout.setContext( lc );
        layout.setPattern( "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n" );
        layout.start();
        encoder.setLayout( layout );
        ca.setEncoder( encoder );
        ca.start();
        return ca;
    }

    private static RollingFileAppender<ILoggingEvent> getFileAppender( String loggerName,
                    LoggerContext lc, String fileName ) {
        String logDir = Preferences.getLogDir();
        RollingFileAppender<ILoggingEvent> rfa = new RollingFileAppender<>();
        rfa.setContext( lc );
        rfa.setName( loggerName + "-file" );
        rfa.setFile( logDir + File.separator + fileName );
        TimeBasedRollingPolicy<ILoggingEvent> policy = new TimeBasedRollingPolicy<>();
        String namePattern = String.format( "%s/%s.%%d{yyyy-MM-dd}.zip ", logDir, fileName );
        policy.setFileNamePattern( namePattern );
        policy.setMaxHistory( 7 );
        policy.setContext( lc );
        policy.setParent( rfa );
        policy.start();
        rfa.setRollingPolicy( policy );
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext( lc );
        encoder.setPattern( "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n" );
        encoder.start();
        rfa.setEncoder( encoder );
        rfa.start();
        return rfa;
    }

    public static void setupConsoleLoggerFor( String name, Level level ) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = lc.getLogger( name );
        logger.addAppender( getConsoleAppender( lc ) );
        logger.setLevel( level );
    }

    public static void setupFileLoggerFor( String name, Level level, String logFileName ) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = lc.getLogger( name );
        logger.addAppender( getFileAppender( name, lc, logFileName ) );
        logger.setLevel( level );
    }
}
