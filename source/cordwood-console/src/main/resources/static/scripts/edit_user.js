require.config({
  paths: {
    'jquery': 'libs/jquery-2.0.3.min',
    'bootstrap': 'libs/bootstrap.min',
    'jquery-chosen': 'libs/chosen.jquery',
    'main': 'main'
  },
  //兼容非 AMD 规范的第三方 JS 框架。
  shim: {
    'bootstrap': {
      deps: ['jquery']
    },
    'jquery-chosen': {
      deps: ['jquery']
    }
  }
});

/**
 * 应用初始化。
 * */
requirejs(['main'], function(app) {
  app.loadSidebar('users');
});

/**
 * 初始化下拉搜索框。
 * */
requirejs(['jquery-chosen'], function() {
  $('.chosen-select').chosen({
    no_results_text: '没有搜索到',
    disable_search_threshold: 5
  });
});