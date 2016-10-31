/*
 * Copyright © 2016 hankai.ren, All rights reserved.
 *
 * http://www.hankai.ren
 */

package ren.hankai.cordwood.persist.config;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.jta.JtaTransactionManager;

import ren.hankai.cordwood.persist.util.Slf4jSessionLogger;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

/**
 * JPA 配置基类。
 *
 * @author hankai
 * @version 1.0
 * @since Aug 18, 2016 9:57:22 AM
 */
@Configuration
public class JpaConfiguration extends JpaBaseConfiguration {

  /**
   * 初始化 JPA 配置。
   *
   * @param dataSource 数据源
   * @param properties JPA 属性配置
   * @param jtaTransactionManagerProvider 事务管理器提供者
   */
  protected JpaConfiguration(DataSource dataSource, JpaProperties properties,
      ObjectProvider<JtaTransactionManager> jtaTransactionManagerProvider) {
    super(dataSource, properties, jtaTransactionManagerProvider);
  }

  protected static Logger logger = LoggerFactory.getLogger(JpaConfiguration.class);
  @Autowired
  private DataSourceInfo dataSourceInfo;

  @Override
  protected String[] getPackagesToScan() {
    final String[] packages = super.getPackagesToScan();
    final String[] customPackages = dataSourceInfo.getEntityBasePackages();
    if ((customPackages != null) && (customPackages.length > 0)) {
      return (String[]) ArrayUtils.addAll(packages, customPackages);
    }
    return packages;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration#createJpaVendorAdapter()
   */
  @Override
  protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
    final EclipseLinkJpaVendorAdapter adapter = new EclipseLinkJpaVendorAdapter();
    adapter.setDatabasePlatform(dataSourceInfo.getDatabasePlatform());
    adapter.setShowSql(true);
    return adapter;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration#getVendorProperties()
   */
  @Override
  protected Map<String, Object> getVendorProperties() {
    final Map<String, Object> jpaProperties = new HashMap<>();
    jpaProperties.put("eclipselink.target-database", dataSourceInfo.getDatabasePlatform());
    // jpaProperties.put( "eclipselink.ddl-generation", "drop-and-create-tables" );
    // this controls what will be logged during DDL execution
    // jpaProperties.put("eclipselink.ddl-generation.output-mode", "both");
    jpaProperties.put("eclipselink.weaving", "static");
    jpaProperties.put("eclipselink.logging.level", "FINE");
    jpaProperties.put("eclipselink.logging.parameters", "true");
    jpaProperties.put("eclipselink.logging.logger", Slf4jSessionLogger.class.getName());
    return jpaProperties;
  }
}
