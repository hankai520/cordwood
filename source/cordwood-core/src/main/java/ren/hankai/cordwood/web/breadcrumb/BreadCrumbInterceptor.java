
package ren.hankai.cordwood.web.breadcrumb;

import org.apache.commons.lang.StringUtils;
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


  @SuppressWarnings("unchecked")
  private Map<String, LinkedHashMap<String, BreadCrumbLink>> getBreadCrumbLinksFromSession(
      HttpSession session) {
    final Map<String, LinkedHashMap<String, BreadCrumbLink>> breadCrumb =
        (Map<String, LinkedHashMap<String, BreadCrumbLink>>) session
            .getAttribute(BREAD_CRUMB_LINKS);
    return breadCrumb;
  }

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

  private void resetBreadCrumbs(LinkedHashMap<String, BreadCrumbLink> familyMap) {
    for (final BreadCrumbLink breadCrumbLink : familyMap.values()) {
      breadCrumbLink.setCurrentPage(false);
    }
  }

  private void generateBreadCrumbsRecursively(BreadCrumbLink link,
      LinkedList<BreadCrumbLink> breadCrumbLinks) {
    if (link.getPrevious() != null) {
      generateBreadCrumbsRecursively(link.getPrevious(), breadCrumbLinks);
    }
    breadCrumbLinks.add(link);
  }


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
