
package ren.hankai.cordwood.console;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ren.hankai.cordwood.core.ApplicationInitializer;

/**
 * 控制台程序入口
 *
 * @author hankai
 * @version 0.0.1
 * @since Sep 28, 2016 11:18:12 AM
 */
@SpringBootApplication
@EnableSpringConfigured
@EnableAspectJAutoProxy
@EnableConfigurationProperties
@EnableJpaRepositories(
    basePackages = { "ren.hankai" } )
@EnableTransactionManagement
@EnableScheduling
@ComponentScan(
    basePackages = { "ren.hankai.cordwood" } )
public class Application {

    public static void main( String[] args ) {
        if ( ApplicationInitializer.initialize() ) {
            SpringApplication.run( Application.class, args );
        }
    }
}
