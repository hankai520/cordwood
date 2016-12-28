require.config({
  paths: {
    'jquery': 'libs/jquery-2.0.3.min',
    'bootstrap': 'libs/bootstrap.min',
    'jquery-chosen': 'libs/chosen.jquery',
    'crypto-js': 'libs/crypto-js',
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
 * 表单密码加密。
 * */
requirejs(['jquery','crypto-js'], function($, CryptoJS) {
  $('form').submit(function() {
    var pwd = $('#password').val();
    if (pwd) {
        pwd = CryptoJS.SHA1(pwd);
        $('#password').val(pwd);
        return true;
    } else {
        $('#pwd-group').addClass('has-error');
        alert('请设置密码!');
        return false;
    }
  });
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