/**
 * 应用初始化。
 * */
requirejs(['main'], function(app) {
  app.loadSidebar('dashboard');
  requirejs(['bootstrap']);
});