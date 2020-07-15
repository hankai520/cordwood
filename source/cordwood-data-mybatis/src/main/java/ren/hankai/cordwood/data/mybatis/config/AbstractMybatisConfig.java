
package ren.hankai.cordwood.data.mybatis.config;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.data.AbstractDataSourceConfig;

import java.io.IOException;

import javax.sql.DataSource;

/**
 * Mybatis数据源配置类。
 *
 * @author hankai
 * @since 1.0.0
 */
public abstract class AbstractMybatisConfig extends AbstractDataSourceConfig {

  /**
   * 获取Mybatis配置文件。
   *
   * @param baseName 基础名称
   * @param profile 当前配置
   * @return 配置文件
   */
  private Resource getConfigFile(final String baseName, final String profile) {
    final String[] fileNames = {baseName + "-" + profile + ".xml", baseName + ".xml"};
    FileSystemResource configResource = null;
    for (final String fileName : fileNames) {
      configResource = new FileSystemResource(Preferences.getConfigFilePath(fileName));
      if (configResource.exists()) {
        return configResource;
      }
    }
    return null;
  }

  /**
   * 配置SqlSessionFactory的内部方法。
   *
   * @param dataSource 数据源
   * @param profile 当前激活的配置
   * @return MyBatis SqlSessionFactory
   * @throws IOException 读取mapper xml时出现异常
   * @throws Exception 通过工厂bean获取工厂实例出现异常
   */
  private SqlSessionFactory getSqlSessionFactoryInternal(final DataSource dataSource, final String profile,
      final boolean failfast) throws IOException, Exception {
    final SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource);
    final Resource configResource = getConfigFile(getConfigFileBaseName(), profile);
    factoryBean.setConfigLocation(configResource);
    factoryBean.setFailFast(failfast);
    factoryBean.setTypeAliasesPackage(getTypeAliasPackage());
    final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    final Resource[] mapperLocations =
        resolver.getResources("classpath:" + getMapperPackage().replaceAll("\\.", "/") + "/*" + getMapperXmlSuffix());
    factoryBean.setMapperLocations(mapperLocations);
    return factoryBean.getObject();
  }

  /**
   * 指定Mybatis SqlSessionFactory 配置文件基础名称（内部根据当前激活的配置构造完整名称）。
   *
   * @return 配置文件基础名称
   * @see Preferences.PROFILE_PRODUCTION
   * @see Preferences.PROFILE_TEST
   */
  protected String getConfigFileBaseName() {
    // 默认不实用xml配置session factory
    return null;
  }

  /**
   * 指定Mybatis Mapper所在包名，扫描包下带有 @Mapper 注解的类并自动组册为Mapper。
   *
   * @return Mapper包名
   */
  protected abstract String getMapperPackage();

  /**
   * 指定Mapper xml配置文件的通配后缀（默认为 "-mapper.xml"）。
   *
   * @return 通配后缀
   */
  protected String getMapperXmlSuffix() {
    return "-mapper.xml";
  }

  protected abstract String getTypeAliasPackage();

  @Profile(Preferences.PROFILE_PRODUCTION)
  @Bean
  public SqlSessionFactory productionSqlSessionFactory(final DataSource dataSource) throws Exception {
    return getSqlSessionFactoryInternal(dataSource, Preferences.PROFILE_PRODUCTION, false);
  }

  @Profile(Preferences.PROFILE_TEST)
  @Bean
  public SqlSessionFactory testSqlSessionFactory(final DataSource dataSource) throws Exception {
    return getSqlSessionFactoryInternal(dataSource, Preferences.PROFILE_TEST, true);
  }

  @Bean
  public MapperScannerConfigurer mapperScannerConfigurer(final SqlSessionFactory factory) {
    final MapperScannerConfigurer scanner = new MapperScannerConfigurer();
    scanner.setAnnotationClass(Mapper.class);
    scanner.setBasePackage(getMapperPackage());
    return scanner;
  }
}
