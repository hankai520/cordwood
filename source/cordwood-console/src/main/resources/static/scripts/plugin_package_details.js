require.config({
  paths: {
    'jquery': 'libs/jquery-2.0.3.min',
    'bootstrap': 'libs/bootstrap.min',
    'bootstrap-switch': 'libs/bootstrap-switch.min',
    'main': 'main'
  },
  // 兼容非 AMD 规范的第三方 JS 框架。
  shim: {
    'bootstrap': {
      deps: ['jquery']
    },
    'bootstrap-switch': {
      deps: ['bootstrap']
    }
  }
});

/**
 * 应用初始化。
 * */
requirejs(['main'], function(app) {
  app.loadSidebar('plugins');
});

/**
 * 初始化开关控件。
 * */
requirejs(['bootstrap-switch'], function() {
  $('.make-switch').on('switch-change', function(e, data) {
    var sender = $(data.el);
    var pname = sender.val();
    var enabled = data.value;
    var url = '/admin/plugins/' + pname + '/' + (enabled ? 'on' : 'off');
    var senderId = '#pluginSwitch' + pname;
    $(senderId).bootstrapSwitch('setActive', false);
    $.get(url, function(data, textStatus, jqXHR) {
      if (jqXHR.status == 200) {
        $(senderId).bootstrapSwitch('setActive', true);
      }
    });
  });
});