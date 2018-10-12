
package ren.hankai.cordwood.web.security.support;

import com.google.common.util.concurrent.RateLimiter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import ren.hankai.cordwood.web.security.AccessLimiter;
import ren.hankai.cordwood.web.security.annotation.Stabilized;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

/**
 * 默认的访问限制器实现，基于 google guava 令牌桶限流器实现。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 12, 2018 10:59:23 AM
 */
@Component
public class DefaultAccessLimiter implements AccessLimiter {

  private static final Logger logger = LoggerFactory.getLogger(DefaultAccessLimiter.class);

  /**
   * 限流接口映射表，缓存需要限流的接口。
   */
  private final HashMap<HandlerMethod, RateLimitInfo> rateLimitMappings = new HashMap<>(64);

  /**
   * 获取稳定器配置描述，用于打印日志跟踪配置变化。
   *
   * @param stabilized 稳定器配置
   * @return 稳定器配置描述
   * @author hankai
   * @since Oct 12, 2018 11:46:00 AM
   */
  private String getStabilizationDescription(Stabilized stabilized) {
    return String.format(
        "{ maxQps=%.2f, timeout=%d, warmupPeriod=%d, fusingInterval=%d, fusingThreshold=%d }",
        stabilized.maxQps(), stabilized.timeout(), stabilized.warmupPeriod(),
        stabilized.fusingInterval(), stabilized.fusingThreshold());
  }

  /**
   * 获取处理请求的方法上的稳定器注解，若方法无注解，则从其所属类及父类中查找。
   *
   * @param handlerMethod 处理请求的方法
   * @return 稳定器注解
   * @author hankai
   * @since Oct 12, 2018 11:03:07 AM
   */
  private Stabilized getStabilizations(HandlerMethod handlerMethod) {
    // 方法级别的注解优先
    final Stabilized methodAnno = handlerMethod.getMethodAnnotation(Stabilized.class);
    if (null != methodAnno) {
      return methodAnno;
    }
    // 搜索类级别注解
    final Class<?> beanType = handlerMethod.getBeanType();
    final Stabilized anno = beanType.getAnnotation(Stabilized.class);
    if (anno != null) {
      logger.debug(String.format("Access limit configuration: \n\n%s\n\n",
          getStabilizationDescription(anno)));
    }
    return anno;
  }

  @Override
  public boolean handleAccess(HandlerExecutionChain requestHandler, HttpServletResponse response) {
    final Object obj = requestHandler.getHandler();
    if (null != obj) {
      if (obj instanceof HandlerMethod) {
        final HandlerMethod hm = (HandlerMethod) obj;
        final Stabilized anno = getStabilizations(hm);
        // 启用了接口稳定器
        if (null != anno) {
          // 从缓存中获取限流配置
          RateLimitInfo limitInfo = rateLimitMappings.get(hm);
          if (null == limitInfo) {
            // 无缓存，则新建并缓存限流配置
            final RateLimiter limiter =
                RateLimiter.create(anno.maxQps(), anno.warmupPeriod(), TimeUnit.SECONDS);
            limitInfo = new RateLimitInfo(limiter);
            rateLimitMappings.put(hm, limitInfo);
          } else {
            logger.debug(
                String.format("Access hit limit mapping, selected handler mapping was:\n\n %s\n",
                    hm.toString()));
          }
          if (limitInfo.isFused(anno)) {
            // 已熔断，直接返回
            logger.debug("Fused, service will be unavailable temporarily.");
            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            return false;
          } else {
            // 从限流器获取令牌
            final boolean success =
                limitInfo.getLimiter().tryAcquire(1, anno.timeout(), TimeUnit.MICROSECONDS);
            if (success) {
              limitInfo.resetFailures(); // 重置熔断状态，恢复工作
            } else {
              logger.debug("Failed to acquire token, service will be unavailable temporarily.");
              limitInfo.addFailures(); // 增加失败次数，达到熔断阈值则触发熔断
              response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
              return false;
            }
          }
        }
      }
    }
    return true;
  }

}
