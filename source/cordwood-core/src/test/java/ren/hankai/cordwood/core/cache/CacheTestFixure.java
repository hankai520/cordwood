
package ren.hankai.cordwood.core.cache;

import org.springframework.stereotype.Component;

@Component
public class CacheTestFixure {

  public static int m1InvokeTimes = 0;

  @LightWeight
  public String m1(final String p1) {
    m1InvokeTimes++;
    return "m1";
  }

}
