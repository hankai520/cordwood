/**
 * 应用初始化。
 * */
requirejs(['main'], function(app) {
  app.loadSidebar('users');
  

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
});