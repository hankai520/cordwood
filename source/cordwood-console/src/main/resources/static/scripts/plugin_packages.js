require.config({
  paths: {
    'jquery': 'libs/jquery-2.0.3.min',
    'bootstrap': 'libs/bootstrap.min',
    'bootstrap-table': 'libs/bootstrap-table',
    'bootstrap-table-i18n': 'libs/bootstrap-table-zh-CN',
    'dropzone': 'libs/dropzone.min',
    'main': 'main'
  },
  // 兼容非 AMD 规范的第三方 JS 框架。
  shim: {
    'bootstrap': {
      deps: ['jquery']
    },
    'bootstrap-table': {
      deps: ['jquery']
    },
    'bootstrap-table-i18n': {
      deps: ['jquery', 'bootstrap-table']
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
  app.loadSidebar('plugins');
});

/**
 * 初始化数据表。
 * */
requirejs(['bootstrap','bootstrap-table-i18n'], function() {
  $('#dataTable').bootstrapTable({
    url: '/admin/plugin_packages.json',
    toolbar: '#toolbar',
    sidePagination: 'server',
    showRefresh: true,
    pageSize: 20,
    pageList: [20,50,100],
    showColumns: true,
    search: true,
    pagination: true,
    sortName: 'createTime',
    sortOrder: 'desc',
    columns: [{
        field: 'id',
        title: '标识符',
        sortable: true,
        valign: 'middle',
        visible: false
    }, {
        field: 'fileName',
        title: '文件名',
        valign: 'middle',
        sortable: true,
        visible:false
    }, {
        field: 'description',
        title: '插件包',
        valign: 'middle',
        sortable: true,
        formatter: function (value, row, index) {
            return '<a href="/admin/plugin_package_details?package_id=' + row.id + '">' + value + '</a>';
        }
    }, {
        field: 'developer',
        title: '开发者',
        valign: 'middle',
        sortable: true
    }, {
        field: 'createTime',
        title: '创建时间',
        valign: 'middle',
        sortable: true
    }, {
        title: '操作',
        valign: 'middle',
        sortable: false,
        formatter: function (value, row, index) {
            return '<a pid="'+row.id+'" class="text-danger uninstall-plugin" href="#">卸载</a>';
        }
    }],
    onLoadSuccess: function() {
      $('.uninstall-plugin').click(function() {
         var pid = $(this).attr('pid');
         if (confirm('确定要卸载此插件包?')) {
           var url = '/admin/plugin_packages/uninstall?package_id='+pid;
           $.get(url, function(data, status, xhr) {
             if (xhr.status == 200) {
               var param = {field: 'id', values: [pid]};
               $('#dataTable').bootstrapTable('remove', param);
             }
           });
         }
      });
    }
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
        $('#dataTable').bootstrapTable('refresh', {silent: true});
        myDropzone.removeFile(file);
        alert('插件上传成功!');
      });
    }
  });
});