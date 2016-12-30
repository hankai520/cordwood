/**
 * 应用初始化。
 * */
requirejs(['main'], function(app) {
  app.loadSidebar('plugins');
  
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
});