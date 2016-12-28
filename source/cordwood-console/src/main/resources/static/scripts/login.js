require.config({
  paths: {
    'jquery': 'libs/jquery-2.0.3.min',
    'bootstrap': 'libs/bootstrap.min',
    'uniform': 'libs/jquery.uniform.min'
  },
  //兼容非 AMD 规范的第三方 JS 框架。
  shim: {
    'bootstrap': {
      deps: ['jquery']
    },
    'uniform': {
      deps: ['jquery']
    }
  }
});

requirejs(['bootstrap','uniform'], function() {
  $(".uniform").uniform();
  
  if (!$('#email').val()) {
    $('#email').focus();
  }
  
  $('#forgotPwd').click(function() {
    $('.visible').removeClass('visible animated fadeInUp');
    $('#forgot').addClass('visible animated fadeInUp');
    return false;
  });
  
  $('#signUp').click(function() {
    $('.visible').removeClass('visible animated fadeInUp');
    $('#register').addClass('visible animated fadeInUp');
    return false;
  });
  
  $('.backToLogin').click(function() {
    $('.visible').removeClass('visible animated fadeInUp');
    $('#login').addClass('visible animated fadeInUp');
    return false;
  });
});