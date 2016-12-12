
package ren.hankai.cordwood.console.test;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ren.hankai.cordwood.console.PluginInitializer;
import ren.hankai.cordwood.console.persist.PluginPackageRepository;
import ren.hankai.cordwood.console.persist.PluginRepository;
import ren.hankai.cordwood.console.persist.PluginRequestRepository;
import ren.hankai.cordwood.console.persist.model.PluginBean;
import ren.hankai.cordwood.console.persist.model.PluginPackageBean;
import ren.hankai.cordwood.console.persist.model.PluginRequestBean;
import ren.hankai.cordwood.core.ApplicationInitializer;
import ren.hankai.cordwood.core.Preferences;

import java.io.File;
import java.util.Date;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Dec 12, 2016 9:19:41 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ConsoleTestSupport.class}) // CoreSpringConfig.class
@ActiveProfiles({Preferences.PROFILE_TEST})
@Configuration
@ComponentScan(basePackages = {"ren.hankai"})
public abstract class ConsoleTestSupport {

  static {
    System.setProperty(Preferences.ENV_APP_HOME_DIR, "./test-home");
    Assert.assertTrue(ApplicationInitializer.initialize("ehcache.xml", "system.yml"));
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        try {
          FileUtils.deleteDirectory(new File(Preferences.getHomeDir()));
        } catch (final Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  @Autowired
  private PluginInitializer pluginInitializer;
  @Autowired
  protected PluginPackageRepository pluginPackageRepo;
  @Autowired
  protected PluginRepository pluginRepo;
  @Autowired
  protected PluginRequestRepository pluginRequestRepo;
  protected PluginPackageBean pluginPackageBean;
  protected PluginBean pluginBean;

  @Before
  public void setup() throws Exception {
    pluginInitializer.suspend();

    pluginPackageBean = new PluginPackageBean();
    pluginPackageBean.setId("org.test:test:1.0.0");
    pluginPackageBean.setCreateTime(new Date());
    pluginPackageBean.setFileName("test.jar");
    pluginPackageRepo.save(pluginPackageBean);

    pluginBean = new PluginBean();
    pluginBean.setActive(true);
    pluginBean.setDescription("test");
    pluginBean.setDeveloper("test@test.com");
    pluginBean.setDisplayName("test only");
    pluginBean.setName("hello");
    pluginBean.setVersion("1.0.0");
    pluginBean.setPluginPackage(pluginPackageBean);
    pluginRepo.save(pluginBean);

    final PluginRequestBean pluginRequest = new PluginRequestBean();
    pluginRequest.setClientIp("127.0.0.1");
    pluginRequest.setCreateTime(new Date());
    pluginRequest.setMilliseconds(23L);
    pluginRequest.setPlugin(pluginBean);
    pluginRequest.setRequestBytes(1L);
    pluginRequest.setRequestMethod("GET");
    pluginRequest.setRequestUrl("http://test.org/test");
    pluginRequest.setResponseBytes(12L);
    pluginRequest.setResponseCode(200);
    pluginRequest.setSucceeded(true);
    pluginRequestRepo.save(pluginRequest);
  }

  @After
  public void teardown() {
    pluginRequestRepo.deleteAll();
    pluginRepo.deleteAll();
    pluginPackageRepo.deleteAll();
  }

}
