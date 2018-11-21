/*
 * Copyright © 2016 hankai.ren, All rights reserved.
 * http://www.hankai.ren
 */

package ren.hankai.cordwood.data.jpa.config;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.data.jpa.logging.Slf4jSessionLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据源平台信息封装。
 *
 * @author hankai
 * @version 1.0
 * @since Aug 18, 2016 2:29:26 PM
 */
public class JpaDataSourceInfo {

  private String databasePlatform; // 数据库平台
  private String[] entityBasePackages; // 实体类基包

  public JpaDataSourceInfo() {}

  public JpaDataSourceInfo(String platform, String... basePackages) {
    databasePlatform = platform;
    entityBasePackages = basePackages;
  }

  /**
   * 构造JPA实现供应商的适配器。
   *
   * @param showSql 是否打印sql语句
   * @return 适配器
   * @author hankai
   * @since Mar 29, 2018 10:59:51 PM
   */
  public AbstractJpaVendorAdapter createJpaVendorAdapter(boolean showSql) {
    final EclipseLinkJpaVendorAdapter adapter = new EclipseLinkJpaVendorAdapter();
    adapter.setDatabasePlatform(this.databasePlatform);
    adapter.setShowSql(showSql);
    return adapter;
  }

  /**
   * 构造JPA实现供应商的特性。
   *
   * @return 特性
   * @author hankai
   * @since Mar 29, 2018 10:58:12 PM
   */
  public Map<String, Object> getVendorProperties() {
    final Map<String, Object> jpaProperties = new HashMap<>();
    jpaProperties.put("eclipselink.target-database", this.databasePlatform);
    /*
     * create-tables, create-or-extend-tables, drop-and-create-tables, none
     */
    jpaProperties.put("eclipselink.ddl-generation", "create-or-extend-tables");
    /*
     * both, database, sql-script
     */
    jpaProperties.put("eclipselink.ddl-generation.output-mode", "sql-script");
    jpaProperties.put("eclipselink.application-location", Preferences.getTempDir());
    jpaProperties.put("eclipselink.create-ddl-jdbc-file-name", "eclipselink_create.sql");
    jpaProperties.put("eclipselink.drop-ddl-jdbc-file-name", "eclipselink_drop.sql");
    jpaProperties.put("eclipselink.weaving", "static");
    jpaProperties.put("eclipselink.logging.level", "FINE");
    jpaProperties.put("eclipselink.logging.parameters", "true");
    jpaProperties.put("eclipselink.logging.logger", Slf4jSessionLogger.class.getName());
    return jpaProperties;
  }

  /**
   * 获取设置的数据库平台。
   *
   * @return 数据库平台
   * @author hankai
   * @since Mar 29, 2018 11:02:39 PM
   */
  public String getDatabasePlatform() {
    return databasePlatform;
  }

  /**
   * 设置数据库平台。
   *
   * @param databasePlatform 数据库平台
   * @author hankai
   * @since Mar 29, 2018 11:02:25 PM
   */
  public void setDatabasePlatform(String databasePlatform) {
    this.databasePlatform = databasePlatform;
  }

  /**
   * 获取需要扫描的基包。
   *
   * @return 基包数组
   * @author hankai
   * @since Mar 29, 2018 11:02:11 PM
   */
  public String[] getPackagesToScan() {
    return entityBasePackages;
  }

  /**
   * 获取需要扫描的基包。
   *
   * @param defaultPackages 默认基包
   * @return 基包数组
   * @author hankai
   * @since Mar 29, 2018 11:01:46 PM
   */
  public String[] getPackagesToScan(String... defaultPackages) {
    if ((entityBasePackages != null) && (entityBasePackages.length > 0)) {
      if (defaultPackages != null) {
        return ArrayUtils.addAll(defaultPackages, entityBasePackages);
      } else {
        return entityBasePackages;
      }
    }
    return defaultPackages;
  }

  /**
   * 设置要扫描的基包。
   *
   * @param entityBasePackages 实体类基包
   * @author hankai
   * @since Mar 29, 2018 11:01:23 PM
   */
  public void setPackagesToScan(String[] entityBasePackages) {
    this.entityBasePackages = entityBasePackages;
  }
}
