/**
 * 应用初始化。
 * */
requirejs(['main'], function(app) {
  app.loadSidebar('apps');
  
  /**
   * 初始化数据表。
   * */
  requirejs(['bootstrap-table-i18n'], function() {
    $('#dataTable').bootstrapTable({
      url: '/admin/apps.json',
      toolbar: '#toolbar',
      sidePagination: 'server',
      showRefresh: true,
      pageSize: 15,
      pageList: [15,30,50],
      showColumns: true,
      search: true,
      pagination: true,
      sortName: 'createTime',
      sortOrder: 'desc',
      columns: [{
          field: 'id',
          title: 'ID',
          sortable: true,
          valign: 'middle',
          visible: false
      }, {
          field: 'name',
          title: '名称',
          sortable: true,
          valign: 'middle',
          formatter: function (value, row, index) {
              return '<a href="/admin/apps/' + row.id + '/edit">' + value + '</a>';
          }
      }, {
          field: 'platformName',
          title: '平台',
          valign: 'middle',
          sortable: false
      }, {
          field: 'appKey',
          title: '应用标识',
          valign: 'middle',
          sortable: true
      }, {
          field: 'secretKey',
          title: '秘钥',
          valign: 'middle',
          sortable: true
      }, {
          field: 'statusName',
          title: '状态',
          valign: 'middle',
          sortable: false
      }, {
          field: 'updateTime',
          title: '最近更新',
          valign: 'middle',
          sortable: true,
          visible: false
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
              return '<a appid="'+row.id+'" class="text-danger delete-app" href="#">删除</a>';
          }
      }],
      onLoadSuccess: function() {
        $('.delete-app').click(function() {
           var appid = $(this).attr('appid');
           if (confirm('确定要删除这个应用？')) {
             var url = '/admin/apps/' + appid + '/delete';
             $.get(url, function(data, status, xhr) {
                 if (xhr.status == 200) {
                     var param = {field: 'id', values: [parseInt(appid)]};
                     $('#dataTable').bootstrapTable('remove', param);
                 }
             });
           }
        });
      }
    });
  });
});