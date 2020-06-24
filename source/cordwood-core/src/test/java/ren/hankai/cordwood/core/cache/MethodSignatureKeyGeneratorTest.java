
package ren.hankai.cordwood.core.cache;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

public class MethodSignatureKeyGeneratorTest {

  @Test
  public void testGenerate() throws Exception {
    final MethodSignatureKeyGeneratorTest target = new MethodSignatureKeyGeneratorTest();
    final Method method = target.getClass().getMethod("testGenerate");
    final MethodSignatureKeyGenerator gen = new MethodSignatureKeyGenerator();
    final String p1 = "abc";
    final Object key = gen.generate(target, method, new Object[] {p1});
    Assert.assertTrue(key instanceof String);
    final String exp =
        target.getClass().toString() + method.getName() + String.class.toString() + p1.hashCode();
    Assert.assertEquals(exp, key);
  }

}
