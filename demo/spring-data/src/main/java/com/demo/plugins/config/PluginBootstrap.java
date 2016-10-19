
package com.demo.plugins.config;

import com.demo.plugins.util.Slf4jSessionLogger;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;
import org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer;
import org.eclipse.persistence.platform.database.MySQLPlatform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

import javax.sql.DataSource;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Sep 30, 2016 8:48:53 AM
 */
@Configuration
@EnableJpaRepositories(
    basePackages = { "com.demo" } )
@EnableTransactionManagement
@ComponentScan(
    basePackages = "com.demo" )
@PropertySource(
    encoding = "utf8",
    value = "classpath:db.properties" )
public class PluginBootstrap {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(
        name = "messageSource" )
    public ReloadableResourceBundleMessageSource getMessageSource() {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setBasenames( "i18n/messages" );
        ms.setDefaultEncoding( "UTF-8" );
        ms.setCacheSeconds( 0 );
        ms.setFallbackToSystemLocale( false );
        ms.setUseCodeAsDefaultMessage( true );
        return ms;
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion( Include.NON_NULL );
        return objectMapper;
    }

    @Bean
    public DataSource dataSource( Environment env ) {
        // TODO: 为什么通过 @Value 的方式注入值，就不行?
        PoolProperties pp = new PoolProperties();
        pp.setUrl( env.getProperty( "db.url" ) );
        pp.setDriverClassName( env.getProperty( "db.driverClassName" ) );
        pp.setUsername( env.getProperty( "db.username" ) );
        pp.setPassword( env.getProperty( "db.password" ) );
        pp.setJmxEnabled( true );
        pp.setTestWhileIdle( false );
        pp.setTestOnBorrow( true );
        pp.setValidationQuery( "select 1" );
        pp.setTestOnReturn( false );
        pp.setValidationInterval( 30000 );
        pp.setTimeBetweenEvictionRunsMillis( 30000 );
        pp.setMaxActive( 100 );
        pp.setInitialSize( 10 );
        pp.setMaxWait( 10000 );
        pp.setRemoveAbandonedTimeout( 60 );
        pp.setMinEvictableIdleTimeMillis( 30000 );
        pp.setMinIdle( 10 );
        pp.setLogAbandoned( true );
        pp.setRemoveAbandoned( true );
        pp.setJdbcInterceptors( ConnectionState.class.getName() + ";"
            + StatementFinalizer.class.getName() );
        org.apache.tomcat.jdbc.pool.DataSource ds =
                                                  new org.apache.tomcat.jdbc.pool.DataSource();
        ds.setPoolProperties( pp );
        return ds;
    }

    @Bean(
        name = "entityManagerFactory" )
    public LocalContainerEntityManagerFactoryBean getEntityManagerFactory( DataSource dataSource ) {
        LocalContainerEntityManagerFactoryBean factory =
                                                       new LocalContainerEntityManagerFactoryBean();
        factory.setPersistenceUnitName( "defaultUnit" );
        factory.setDataSource( dataSource );
        EclipseLinkJpaVendorAdapter adapter = new EclipseLinkJpaVendorAdapter();
        adapter.setDatabasePlatform( MySQLPlatform.class.getName() );
        adapter.setShowSql( true );
        factory.setJpaVendorAdapter( adapter );
        factory.setPackagesToScan( "com.demo" );
        Properties jpaProperties = new Properties();
        jpaProperties.setProperty( "eclipselink.target-database", MySQLPlatform.class.getName() );
        // jpaProperties.setProperty( "eclipselink.ddl-generation", "drop-and-create-tables" );
        // this controls what will be logged during DDL execution
        // jpaProperties.setProperty("eclipselink.ddl-generation.output-mode", "both");
        jpaProperties.setProperty( "eclipselink.weaving", "static" );
        jpaProperties.setProperty( "eclipselink.logging.level", "FINE" );
        jpaProperties.setProperty( "eclipselink.logging.parameters", "true" );
        jpaProperties.setProperty( "eclipselink.logging.logger",
            Slf4jSessionLogger.class.getName() );
        factory.setJpaProperties( jpaProperties );
        return factory;
    }

    @Bean(
        name = "transactionManager" )
    public PlatformTransactionManager getTransactionManager() {
        return new JpaTransactionManager();
    }
}
