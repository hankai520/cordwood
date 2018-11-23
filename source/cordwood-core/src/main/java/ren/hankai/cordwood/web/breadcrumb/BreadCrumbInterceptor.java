
package ren.hankai.cordwood.web.breadcrumb;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 面包屑导航拦截器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 6, 2016 2:56:50 PM
 */
public class BreadCrumbInterceptor implements HandlerInterceptor {

  private static final String BREAD_CRUMB_LINKS = "breadCrumb";
  private static final String CURRENT_BREAD_CRUMB = "currentBreadCrumb";

  // TODO: 面包屑导航单元测试
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    final Annotation[] declaredAnnotations = getDeclaredAnnotationsForHandler(handler);
    if ((declaredAnnotations != null) && (declaredAnnotations.length > 0)) {
      final HttpSession session = request.getSession();
      emptyCurrentBreadCrumb(session);
      for (final Annotation annotation : declaredAnnotations) {
        if (annotation.annotationType().equals(NavigationItem.class)) {
          processAnnotation(request, session, annotation);
        }
      }
    }
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {}

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {}

  private void emptyCurrentBreadCrumb(HttpSession session) {
    session.setAttribute(CURRENT_BREAD_CRUMB, new LinkedList<BreadCrumbLink>());
  }

  /**
   * 根据HTTP请求对应的Handler中的注解，解析导航配置并加入导航分组。
   *
   * @param request HTTP请求
   * @param session HTTP会话
   * @param annotation handler的注解
   * @author hankai
   * @since Nov 22, 2018 3:54:10 PM
   */
  private void processAnnotation(HttpServletRequest request, HttpSession session,
      Annotation annotation) {
    final NavigationItem link = (NavigationItem) annotation;
    final String family = link.family();

    Map<String, LinkedHashMap<String, BreadCrumbLink>> breadCrumb =
        getBreadCrumbLinksFromSession(session);

    if (breadCrumb == null) {
      breadCrumb = new HashMap<>();
    }

    LinkedHashMap<String, BreadCrumbLink> familyMap = breadCrumb.get(family);

    if (familyMap == null) {
      familyMap = new LinkedHashMap<>();
      breadCrumb.put(family, familyMap);
    }

    final BreadCrumbLink breadCrumbLink = getBreadCrumbLink(request, link, familyMap);
    final LinkedList<BreadCrumbLink> currentBreadCrumb = new LinkedList<>();
    generateBreadCrumbsRecursively(breadCrumbLink, currentBreadCrumb);
    session.setAttribute(CURRENT_BREAD_CRUMB, currentBreadCrumb);
    session.setAttribute(BREAD_CRUMB_LINKS, breadCrumb);
  }

  /**
   * 根据当前导航项对应的面包屑导航数据。
   *
   * @param request HTTP请求
   * @param link 当前导航项
   * @param familyMap 导航项分组
   * @return 面包屑导航数据
   * @author hankai
   * @since Nov 22, 2018 3:55:39 PM
   */
  private BreadCrumbLink getBreadCrumbLink(HttpServletRequest request, NavigationItem link,
      LinkedHashMap<String, BreadCrumbLink> familyMap) {
    BreadCrumbLink breadCrumbLink;
    final BreadCrumbLink breadCrumbObject = familyMap.get(link.label());
    resetBreadCrumbs(familyMap);
    if (breadCrumbObject != null) {
      breadCrumbObject.setCurrentPage(true);
      breadCrumbLink = breadCrumbObject;
    } else {
      breadCrumbLink = new BreadCrumbLink(link.family(), link.label(), true, link.parent());
      final StringBuffer relativeUrl = new StringBuffer(request.getRequestURI());
      if (StringUtils.isNotEmpty(request.getQueryString())) {
        relativeUrl.append("?" + request.getQueryString());
      }
      breadCrumbLink.setUrl(relativeUrl.toString());
      familyMap.put(link.label(), breadCrumbLink);
    }
    createRelationships(familyMap, breadCrumbLink);
    return breadCrumbLink;
  }

  /**
   * 从HTTP会话中获取面包屑导航项。
   *
   * @param session HTTP会话
   * @return 面包屑导航数据集合
   * @author hankai
   * @since Nov 22, 2018 3:57:00 PM
   */
  @SuppressWarnings("unchecked")
  private Map<String, LinkedHashMap<String, BreadCrumbLink>> getBreadCrumbLinksFromSession(
      HttpSession session) {
    final Map<String, LinkedHashMap<String, BreadCrumbLink>> breadCrumb =
        (Map<String, LinkedHashMap<String, BreadCrumbLink>>) session
            .getAttribute(BREAD_CRUMB_LINKS);
    return breadCrumb;
  }

  /**
   * 获取HTTP请求Handler中的注解。
   *
   * @param handler HTTP请求Handler
   * @return 注解数组
   * @author hankai
   * @since Nov 22, 2018 3:58:22 PM
   */
  private Annotation[] getDeclaredAnnotationsForHandler(Object handler) {
    if (handler instanceof HandlerMethod) {
      final HandlerMethod handlerMethod = (HandlerMethod) handler;
      final Method method = handlerMethod.getMethod();
      final Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
      return declaredAnnotations;
    } else {
      return null;
    }
  }

  /**
   * 重置面包屑导航。
   *
   * @param familyMap 导航数据表
   * @author hankai
   * @since Nov 22, 2018 3:47:13 PM
   */
  private void resetBreadCrumbs(LinkedHashMap<String, BreadCrumbLink> familyMap) {
    for (final BreadCrumbLink breadCrumbLink : familyMap.values()) {
      breadCrumbLink.setCurrentPage(false);
    }
  }

  /**
   * 递归地生成面包屑导航。
   *
   * @param link 导航项
   * @param breadCrumbLinks 导航项集合
   * @author hankai
   * @since Nov 22, 2018 3:48:35 PM
   */
  private void generateBreadCrumbsRecursively(BreadCrumbLink link,
      LinkedList<BreadCrumbLink> breadCrumbLinks) {
    if (link.getPrevious() != null) {
      generateBreadCrumbsRecursively(link.getPrevious(), breadCrumbLinks);
    }
    breadCrumbLinks.add(link);
  }

  /**
   * 为新的导航项构建关系链接。
   *
   * @param familyMap 导航项分组
   * @param newLink 新项
   * @author hankai
   * @since Nov 22, 2018 3:50:12 PM
   */
  private void createRelationships(LinkedHashMap<String, BreadCrumbLink> familyMap,
      BreadCrumbLink newLink) {
    final Collection<BreadCrumbLink> values = familyMap.values();
    for (final BreadCrumbLink breadCrumbLink : values) {
      if (breadCrumbLink.getLabel().equalsIgnoreCase(newLink.getParentKey())) {
        breadCrumbLink.addNext(newLink);
        newLink.setPrevious(breadCrumbLink);
        newLink.setParent(breadCrumbLink);
      }
    }
  }
}
