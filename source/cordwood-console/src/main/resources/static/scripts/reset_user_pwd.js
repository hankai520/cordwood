/**
 * 应用初始化。
 * */
requirejs(['main'], function(app) {
  app.loadSidebar('users');
  
  requirejs(['jquery','crypto-js'], function($, CryptoJS) {
    $('form').submit(function() {
      var pwd = $('#password').val();
      if (!pwd) {
        $('#pwd-group').addClass('has-error');
        alert('请输入新密码!');
        $('#password').focus();
        return false;
      }
      var pwd2 = $('#pwd2').val();
      if (!pwd2) {
        $('#pwd-group2').addClass('has-error');
        alert('请再次输入密码!');
        $('#pwd2').focus();
        return false;
      }
      if (pwd2 != pwd) {
        $('#pwd-group1').addClass('has-error');
        $('#pwd-group2').addClass('has-error');
        alert('两次输入的密码不一致!');
        $('#pwd2').val('')
        $('#pwd2').focus();
        return false;
      }
      var cryptedPwd = CryptoJS.SHA1(pwd);
      $('#password').val(cryptedPwd);
      $('#pwd2').val(cryptedPwd);
      return true;
    });
  });
});