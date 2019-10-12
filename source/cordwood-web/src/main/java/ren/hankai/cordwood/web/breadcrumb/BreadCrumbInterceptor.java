/*******************************************************************************
 * Copyright (C) 2019 hankai
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/

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
 * <p>面包屑导航拦截器。分组类似命名空间，用于提高导航项检索效率，同时避免类似的导航项重名。通过为控制器方法增加
 * <b>NavigationItem</b> 注解，构建不同页面之间的导航关系。拦截器在收到请求后，会构建导航关系并设置到<b>用户会话</b>中。
 * 通过 {@link BreadCrumbInterceptor#BREAD_CRUMB_LINKS BREAD_CRUMB_LINKS} 可获取到整个导航中所有项，通过
 * {@link BreadCrumbInterceptor#CURRENT_BREAD_CRUMB CURRENT_BREAD_CRUMB} 可获取当前访问的页面对应的导航项。</p>
 * <p>初始状态：{} </p>
 *
 * <pre>
 * 访问 "首页"：
 * "分组" &lt;=&gt; LinkedHashMap {
 *     "首页" &lt;=&gt; BreadCrumbLink("分组", "首页",     true, null)
 * }
 *
 * 访问 “用户管理”：
 * "分组(family)" &lt;=&gt; LinkedHashMap {
 *    "用户管理" &lt;=&gt; BreadCrumbLink("分组", "用户管理", true, "首页"),
 *    "首页" &lt;=&gt; BreadCrumbLink("分组", "首页",     false, null)
 * }
 *
 * 访问 “添加用户”：
 * "分组(family)" &lt;=&gt; LinkedHashMap {
 *    "添加用户(label)" &lt;=&gt; BreadCrumbLink("分组", "添加用户", true, "用户管理"),
 *    "用户管理(label)" &lt;=&gt; BreadCrumbLink("分组", "用户管理", false, "首页"),
 *    "首页(label)" &lt;=&gt; BreadCrumbLink("分组", "首页",  false, null)
 * }
 *
 * 此时，页面可利用面包屑导航数据生成导航路径：首页 \ 用户管理 \ <b>添加用户</b>。
 *
 * 返回 “用户管理”：
 * "分组(family)" &lt;=&gt; LinkedHashMap {
 *    "用户管理" &lt;=&gt; BreadCrumbLink("分组", "用户管理", true, "首页"),
 *    "首页" &lt;=&gt; BreadCrumbLink("分组", "首页", false, null)
 * }
 * </pre>
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 6, 2016 2:56:50 PM
 * @see ren.hankai.cordwood.web.breadcrumb.NavigationItem
 * @see ren.hankai.cordwood.web.breadcrumb.BreadCrumbLink
 */
public class BreadCrumbInterceptor implements HandlerInterceptor {

  /**
   * 面包屑导航项集合，类型为 <code>Map&lt;String, LinkedHashMap&lt;String, BreadCrumbLink&gt;&gt;</code>。
   */
  public static final String BREAD_CRUMB_LINKS = "breadCrumb";
  /**
   * 当前访问的页面所在导航项，类型为 <code>LinkedList&lt;BreadCrumbLink&gt;</code>。
   */
  public static final String CURRENT_BREAD_CRUMB = "currentBreadCrumb";

  @Override
  public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
      final Object handler)
      throws Exception {
    final Annotation[] declaredAnnotations = getDeclaredAnnotationsForHandler(handler);
    if ((declaredAnnotations != null) && (declaredAnnotations.length > 0)) {
      final HttpSession session = request.getSession();
      for (final Annotation annotation : declaredAnnotations) {
        if (annotation.annotationType().equals(NavigationItem.class)) {
          emptyCurrentBreadCrumb(session);
          processAnnotation(request, session, annotation);
          break;
        }
      }
    }
    return true;
  }

  @Override
  public void postHandle(final HttpServletRequest request, final HttpServletResponse response,
      final Object handler,
      final ModelAndView modelAndView) throws Exception {}

  @Override
  public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
      final Object handler, final Exception ex) throws Exception {}

  private void emptyCurrentBreadCrumb(final HttpSession session) {
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
  private void processAnnotation(final HttpServletRequest request, final HttpSession session,
      final Annotation annotation) {
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
  private BreadCrumbLink getBreadCrumbLink(final HttpServletRequest request,
      final NavigationItem link,
      final LinkedHashMap<String, BreadCrumbLink> familyMap) {
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
      final HttpSession session) {
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
  private Annotation[] getDeclaredAnnotationsForHandler(final Object handler) {
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
  private void resetBreadCrumbs(final LinkedHashMap<String, BreadCrumbLink> familyMap) {
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
  private void generateBreadCrumbsRecursively(final BreadCrumbLink link,
      final LinkedList<BreadCrumbLink> breadCrumbLinks) {
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
  private void createRelationships(final LinkedHashMap<String, BreadCrumbLink> familyMap,
      final BreadCrumbLink newLink) {
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
