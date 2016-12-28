require.config({
  paths: {
    'jquery': 'libs/jquery-2.0.3.min',
    'bootstrap': 'libs/bootstrap.min',
    'main': 'main'
  },
  //兼容非 AMD 规范的第三方 JS 框架。
  shim: {
    bootstrap: {
      deps: ['jquery']
    }
  }
});

/**
 * 应用初始化。
 * */
requirejs(['main'], function(app) {
  app.loadSidebar('dashboard');
});

requirejs(['bootstrap']);