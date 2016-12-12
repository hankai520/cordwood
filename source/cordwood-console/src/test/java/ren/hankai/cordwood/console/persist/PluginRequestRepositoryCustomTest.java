
package ren.hankai.cordwood.console.persist;

import org.junit.Test;

import ren.hankai.cordwood.console.test.ConsoleTestSupport;

import java.util.Calendar;
import java.util.Date;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Dec 12, 2016 9:18:47 AM
 */
public class PluginRequestRepositoryCustomTest extends ConsoleTestSupport {


  /**
   * Test method for
   * {@link ren.hankai.cordwood.console.persist.PluginRequestRepositoryCustom#getRequestsGroupByPlugin(java.util.Date, java.util.Date)}.
   */
  @Test
  public void testGetRequestStatistics() {
    final Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    final Date beginTime = cal.getTime();

    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    cal.set(Calendar.MILLISECOND, 999);
    final Date endTime = cal.getTime();
    pluginRequestRepo.getRequestsGroupByPlugin(pluginBean.getDeveloper(), beginTime, endTime);
  }

}
