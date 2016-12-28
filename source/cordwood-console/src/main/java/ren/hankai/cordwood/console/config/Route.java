package ren.hankai.cordwood.console.config;

/**
 * 路由信息。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 31, 2016 5:16:56 PM
 */
public final class Route {

  /*** 通用。 ***/
  public static final String ERROR_PREFIX = "/error";
  public static final String ERROR_HTTP = ERROR_PREFIX + "/{error_code}";
  /*** API根地址。 ***/
  public static final String API_PREFIX = "/api";
  public static final String API_AUTHENTICATE = API_PREFIX + "/authenticate";
  /*** 后台根地址。 ***/
  public static final String BACKGROUND_PREFIX = "/admin";
  public static final String BG_LOGIN = BACKGROUND_PREFIX + "/login";
  public static final String BG_LOGOUT = BACKGROUND_PREFIX + "/logout";
  public static final String BG_MY_ACCOUNT = BACKGROUND_PREFIX + "/my_account";
  public static final String BG_MY_PLUGIN_STATS = BACKGROUND_PREFIX + "/my_plugin_stats.json";
  public static final String BG_MY_PLUGINS = BACKGROUND_PREFIX + "/my_plugins";
  public static final String BG_MY_PLUGIN_LOGS = BACKGROUND_PREFIX + "/my_plugin_logs";
  public static final String BG_MY_PLUGIN_LOGS_JSON = BACKGROUND_PREFIX + "/my_plugin_logs.json";
  public static final String BG_MY_PROFILE = BACKGROUND_PREFIX + "/my_profile";
  public static final String BG_MY_AVATAR = BACKGROUND_PREFIX + "/my_avatar";
  public static final String BG_DASHBOARD = BACKGROUND_PREFIX + "/dashboard";
  public static final String BG_SYS_SETTINGS = BACKGROUND_PREFIX + "/settings";
  public static final String BG_USERS = BACKGROUND_PREFIX + "/users";
  public static final String BG_USERS_JSON = BACKGROUND_PREFIX + "/users.json";
  public static final String BG_ADD_USER = BACKGROUND_PREFIX + "/users/add";
  public static final String BG_EDIT_USER = BACKGROUND_PREFIX + "/users/{user_id}/edit";
  public static final String BG_DELETE_USER = BACKGROUND_PREFIX + "/users/{user_id}/delete";
  public static final String BG_CHANGE_USER_PWD = BACKGROUND_PREFIX + "/users/{user_id}/change_pwd";
  public static final String BG_SIDEBAR_JSON = BACKGROUND_PREFIX + "/sidebar.json";
  public static final String BG_PLUGIN_PACKAGES = BACKGROUND_PREFIX + "/plugin_packages";
  public static final String BG_PLUGIN_PACKAGES_JSON = BACKGROUND_PREFIX + "/plugin_packages.json";
  public static final String BG_PLUGIN_PACKAGE_DETAILS =
      BACKGROUND_PREFIX + "/plugin_package_details";
  public static final String BG_PLUGIN_PACKAGES_UPLOAD =
      BACKGROUND_PREFIX + "/plugin_packages/upload";
  public static final String BG_PLUGIN_PACKAGES_UNINSTALL =
      BACKGROUND_PREFIX + "/plugin_packages/uninstall";
  public static final String BG_PLUGINS_ON = BACKGROUND_PREFIX + "/plugins/{plugin_name}/on";
  public static final String BG_PLUGINS_OFF = BACKGROUND_PREFIX + "/plugins/{plugin_name}/off";
  public static final String BG_PLUGIN_LOGS = BACKGROUND_PREFIX + "/plugins/logs";
  public static final String BG_PLUGIN_LOGS_JSON = BACKGROUND_PREFIX + "/plugins/logs.json";
  public static final String BG_APPS = BACKGROUND_PREFIX + "/apps";
  public static final String BG_APPS_JSON = BACKGROUND_PREFIX + "/apps.json";
  public static final String BG_ADD_APP = BACKGROUND_PREFIX + "/apps/add";
  public static final String BG_EDIT_APP = BACKGROUND_PREFIX + "/apps/{app_id}/edit";
  public static final String BG_DELETE_APP = BACKGROUND_PREFIX + "/apps/{app_id}/delete";

  /*** 前台根地址。 ***/
  public static final String FOREGROUND_PREFIX = "/home";
}
