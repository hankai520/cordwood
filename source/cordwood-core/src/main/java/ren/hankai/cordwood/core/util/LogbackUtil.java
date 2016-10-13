
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
 * Logback 日志助手类
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 10, 2016 2:22:42 PM
 */
public final class LogbackUtil {

    /**
     * 获取一个控制台日志追加器
     *
     * @param lc 日志上下文
     * @return 追加器
     * @author hankai
     * @since Oct 13, 2016 10:20:37 AM
     */
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

    /**
     * 获取一个可自动归档的文件日志追加器
     *
     * @param loggerName 日志器名（即要记录日志的类名）
     * @param lc 日志上下文
     * @param fileName 日志物理文件名
     * @return 日志追加器
     * @author hankai
     * @since Oct 13, 2016 10:21:07 AM
     */
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

    /**
     * 为指定的类或包添加控制台日志器
     *
     * @param name 类或包名
     * @param level 日志级别
     * @author hankai
     * @since Oct 13, 2016 10:22:28 AM
     */
    public static void setupConsoleLoggerFor( String name, Level level ) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = lc.getLogger( name );
        logger.addAppender( getConsoleAppender( lc ) );
        logger.setLevel( level );
    }

    /**
     * 为指定的类或包添加文件日志器
     *
     * @param name 类或包名
     * @param level 日志级别
     * @param logFileName 日志物理文件名
     * @author hankai
     * @since Oct 13, 2016 10:23:04 AM
     */
    public static void setupFileLoggerFor( String name, Level level, String logFileName ) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = lc.getLogger( name );
        logger.addAppender( getFileAppender( name, lc, logFileName ) );
        logger.setLevel( level );
    }
}
