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
  app.loadSidebar('users');
});

/**
 * 初始化数据表。
 * */
requirejs(['bootstrap','bootstrap-table-i18n'], function() {
  $('#dataTable').bootstrapTable({
    url: '/admin/users.json',
    toolbar: '#toolbar',
    sidePagination: 'server',
    showRefresh: true,
    pageSize: 50,
    pageList: [50,100,150],
    showColumns: true,
    search: true,
    pagination: true,
    sortName: 'email',
    sortOrder: 'asc',
    columns: [{
        field: 'id',
        title: 'ID',
        sortable: true,
        valign: 'middle',
        visible: false
    }, {
        field: 'email',
        title: '邮箱',
        sortable: true,
        valign: 'middle',
        formatter: function (value, row, index) {
            return '<a href="/admin/users/' + row.id + '/edit">' + value + '</a>';
        }
    }, {
        field: 'name',
        title: '姓名',
        valign: 'middle',
        sortable: true
    }, {
        field: 'mobile',
        title: '手机号',
        valign: 'middle',
        sortable: true
    }, {
        field: 'createTime',
        title: '创建时间',
        valign: 'middle',
        sortable: true,
        visible: false
    }, {
        field: 'updateTime',
        title: '更新时间',
        valign: 'middle',
        sortable: true,
        visible: false
    }, {
        field: 'statusName',
        title: '状态',
        valign: 'middle',
        sortable: false
    }, {
        title: '操作',
        valign: 'middle',
        sortable: false,
        formatter: function (value, row, index) {
            return '<a uid="'+row.id+'" class="text-danger delete-user" href="#">删除</a>';
        }
    }],
    onLoadSuccess: function() {
      $('.delete-user').click(function() {
         var uid = $(this).attr('uid');
         if (confirm('确定要删除这个用户？')) {
           var url = '/admin/users/' + uid + '/delete';
           $.get(url, function(data, status, xhr) {
               if (xhr.status == 200) {
                 var param = {field: 'id', values: [parseInt(uid)]};
                 $('#dataTable').bootstrapTable('remove', param);
               }
           });
         }
      });
    }
  });
});