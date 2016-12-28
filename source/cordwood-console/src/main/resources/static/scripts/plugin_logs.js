require.config({
  paths: {
    'jquery': 'libs/jquery-2.0.3.min',
    'bootstrap': 'libs/bootstrap.min',
    'bootstrap-table': 'libs/bootstrap-table',
    'bootstrap-table-i18n': 'libs/bootstrap-table-zh-CN',
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
    url: '/admin/plugins/logs.json',
    toolbar: '#toolbar',
    sidePagination: 'server',
    showRefresh: true,
    pageSize: 50,
    pageList: [50,100,150],
    showColumns: true,
    search: true,
    pagination: true,
    sortName: 'createTime',
    sortOrder: 'desc',
    columns: [{
        field: 'createTime',
        title: '时间',
        valign: 'middle',
        sortable: true
    }, {
        field: 'pluginDisplayName',
        title: '插件',
        sortable: false,
        valign: 'middle'
    }, {
        field: 'appName',
        title: '应用',
        sortable: false,
        valign: 'middle',
        visible: false
    }, {
        field: 'clientIp',
        title: '客户端IP',
        valign: 'middle',
        sortable: true,
        visible: false
    }, {
        field: 'requestUrl',
        title: '请求 URL',
        valign: 'middle',
        sortable: true,
        'class': 'truncate-tail middle',
        formatter: function(value, row, index) {
          return '<a href="'+value+'">'+value+'</a>';
        }
    }, {
        field: 'requestMethod',
        title: '请求方法',
        valign: 'middle',
        sortable: true,
        visible: false
    }, {
        field: 'requestDigest',
        title: '摘要',
        valign: 'middle',
        sortable: true,
        visible: false
    }, {
        field: 'requestBytes',
        title: '请求 (字节)',
        valign: 'middle',
        sortable: true,
        formatter: function(value, row, index) {
            return value;
        }
    }, {
        field: 'responseCode',
        title: '响应代码',
        valign: 'middle',
        sortable: true,
        visible: false
    }, {
        field: 'responseBytes',
        title: '响应 (字节)',
        valign: 'middle',
        sortable: true,
        formatter: function(value, row, index) {
            return value;
        }
    }, {
        field: 'milliseconds',
        title: '耗时',
        valign: 'middle',
        sortable: true,
        formatter: function(value, row, index) {
            return value + 'ms';
        }
    }, {
        field: 'succeeded',
        title: '结果',
        valign: 'middle',
        sortable: true,
        formatter: function(value, row, index) {
            if (value) {
                if (row.errors) {
                    return '<span class="label label-warning" data-toggle="popover" title="错误详情" data-content="'
                        +row.errors+'">处理成功</span>';
                } else {
                    return '<span class="label label-success">处理成功</span>';
                }
            } else {
                return '<span class="label label-danger" data-toggle="popover" title="错误详情" data-content="'
                    +row.errors+'">处理失败</span>';
            }
        }
    }],
    onLoadSuccess: function(data) {
        $('[data-toggle="popover"]').popover({container: 'body', placement: 'left', trigger: 'hover'}); 
    }
  });
});