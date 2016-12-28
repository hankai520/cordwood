require.config({
  paths: {
    'jquery': 'libs/jquery-2.0.3.min',
    'bootstrap': 'libs/bootstrap.min',
    'bootstrap-table': 'libs/bootstrap-table',
    'bootstrap-table-i18n': 'libs/bootstrap-table-zh-CN',
    'jquery-easypiechart': 'libs/jquery.easypiechart',
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
  app.loadSidebar();
});

/**
 * 初始化环形饼图。
 * */
requirejs([
  'jquery-easypiechart'
], function() {
  $('.piechart').each(function(index, obj) {
    $(this).easyPieChart({
      easing: 'easeOutBounce',
      onStep: function(from, to, percent) {
        $(this.el).find('.percent').text(Math.round(percent) + "%");
      },
      lineWidth: 6,
      barColor: obj.dataset.barcolor
    });
  });
});

/**
 * 初始化访问统计表。
 * */
requirejs(['bootstrap','bootstrap-table-i18n'], function() {
  $('#dataTable').bootstrapTable({
    url: '/admin/my_plugin_stats.json',
    toolbar: '#toolbar',
    sidePagination: 'client',
    showRefresh: true,
    pageSize: 5,
    pageList: [10, 20, 50],
    pagination: true,
    columns: [{
      field: 'pluginName',
      title: '插件',
      valign: 'middle',
      formatter: function(value, row, index) {
        if (row.pluginIsActive) {
          return '<span class="label label-success">' + value + '</span>';
        } else {
          return '<span class="label label-warning">' + value + '</span>';
        }
      }
    }, {
      field: 'inboundBytes',
      title: '请求（字节）',
      valign: 'middle'
    }, {
      field: 'outboundBytes',
      title: '响应（字节）',
      valign: 'middle'
    }, {
      field: 'accessCount',
      title: '访问次数',
      valign: 'middle'
    }, {
      field: 'timeUsageAvg',
      title: '响应时间',
      valign: 'middle',
      formatter: function(value, row, index) {
        return value + ' ms';
      }
    }, {
      field: 'lastAccessTime',
      title: '最近访问',
      valign: 'middle'
    }]
  });
});