/**
 *
 */
package ren.hankai.cordwood.console.config;

/**
 * 路由信息。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 31, 2016 5:16:56 PM
 */
public final class Route {

  /*** API根地址 ***/
  public static final String API_PREFIX = "/api";
  public static final String API_LOGIN = API_PREFIX + "/login";
  /*** 后台根地址 ***/
  public static final String BACKGROUND_PREFIX = "/admin";
  public static final String BG_SYS_SETTINGS = BACKGROUND_PREFIX + "/settings";
  public static final String BG_USERS = BACKGROUND_PREFIX + "/users";
  public static final String BG_USERS_JSON = BACKGROUND_PREFIX + "/users.json";
  public static final String BG_ADD_USER = BACKGROUND_PREFIX + "/users/add";
  public static final String BG_EDIT_USER = BACKGROUND_PREFIX + "/users/{user_id}/edit";
  public static final String BG_DELETE_USER = BACKGROUND_PREFIX + "/users/{user_id}/delete";
  public static final String BG_CHANGE_USER_PWD = BACKGROUND_PREFIX + "/users/{user_id}/change_pwd";
  public static final String BG_SIDEBAR_JS = BACKGROUND_PREFIX + "/sidebar.js";
  /*** 前台根地址 ***/
  public static final String FOREGROUND_PREFIX = "/home";
  public static final String FG_LOGIN = FOREGROUND_PREFIX + "/login";
}