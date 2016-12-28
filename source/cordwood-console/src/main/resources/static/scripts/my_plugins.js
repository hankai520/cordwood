require.config({
  paths: {
    'jquery': 'libs/jquery-2.0.3.min',
    'bootstrap': 'libs/bootstrap.min',
    'bootstrap-switch': 'libs/bootstrap-switch.min',
    'dropzone': 'libs/dropzone.min',
    'main': 'main'
  },
  //兼容非 AMD 规范的第三方 JS 框架。
  shim: {
    'bootstrap': {
      deps: ['jquery']
    },
    'bootstrap-switch': {
      deps: ['bootstrap']
    },
    'dropzone': {
      deps: ['jquery']
    }
  }
});

/**
 * 应用初始化。
 * */
requirejs(['main'], function(app) {
  app.loadSidebar();
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
/**
 * 初始化 dropzone。
 * */
require(['dropzone'], function(Dropzone) {
  Dropzone.autoDiscover = false;
  $('#dropzone').dropzone({
    url: '/admin/plugin_packages/upload',
    paramName: 'files',
    acceptedFiles: '.jar,.JAR',
    maxFiles: 5,
    autoProcessQueue: false,
    uploadMultiple: true,
    maxFilesize: 10, // MB
    addRemoveLinks: true,
    dictInvalidFileType: '文件格式不正确，请上传 .jar 格式的插件包文件！',
    dictRemoveFile: '删除',
    dictResponseError: '上传出现错误！',
    dictFileTooBig: '文件太大！',
    dictFallbackMessage: '您的浏览器不支持拖拽上传！',
    dictDefaultMessage: '拖拽文件来上传',
    dictMaxFilesExceeded: '文件个数超出限制！',
    init: function() {
      myDropzone = this;
      $('#submitBtn').click(function(event) {
        myDropzone.processQueue();
      });
      this.on("sending", function(file, jqXHR, data) {
        var param = $("meta[name='_csrf_param']").attr("content");
        var token = $("meta[name='_csrf']").attr("content");
        data.append(param, token);
      });
      this.on("success", function(file) {
        $("#uploadPlugin").modal('hide');
        alert('插件上传成功!');
        location.reload();
      });
    }
  });
});