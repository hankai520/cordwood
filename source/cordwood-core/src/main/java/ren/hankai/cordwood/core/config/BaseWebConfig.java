/**
 *
 */

package ren.hankai.cordwood.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * web 配置基类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 31, 2016 10:53:27 PM
 *
 */
public abstract class BaseWebConfig extends WebMvcConfigurerAdapter {

  private static final Logger logger = LoggerFactory.getLogger(BaseWebConfig.class);
  /**
   * 数据交换时，默认的日期格式。
   */
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  /**
   * 数据交换式时，默认的日期时间格式。
   */
  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  /**
   * 会话数据键：当前登录用户。
   */
  public static final String SESSION_KEY_USER = "current_user";
  /**
   * 会话数据键：用户登录前访问的 URL。
   */
  public static final String SESSION_KEY_LAST_URL = "last_url";
  /**
   * Cookie数据键：登录名。
   */
  public static final String COOKIE_KEY_LOGIN_ID = "loginId";
  /**
   * Cookie数据键：密码。
   */
  public static final String COOKIE_KEY_PASSWORD = "password";
  /**
   * 用于在页面渲染前传递页面级提示消息。
   */
  public static final String WEB_PAGE_MESSAGE = "pageMessage";
  /**
   * 用于在页面渲染前传递页面级错误。
   */
  public static final String WEB_PAGE_ERROR = "pageError";

  @Override
  public void addFormatters(FormatterRegistry registry) {
    // 处理表单录入日期
    registry.addConverter(new Converter<String, Date>() {

      @Override
      public Date convert(String source) {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(DATE_TIME_FORMAT);
        Date date = null;
        try {
          date = dateFormatter.parse(source);
        } catch (final ParseException ex) {
          try {
            date = dateTimeFormatter.parse(source);
          } catch (final ParseException e1) {
            logger.warn(String.format("Failed to parse input date string \"%s\".", source), ex);
          }
        }
        return date;
      }
    });
  }

  @Bean
  public MappingJackson2HttpMessageConverter getJackson2HttpMessageConverter(ObjectMapper om) {
    return new MappingJackson2HttpMessageConverter(om);
  }
}
