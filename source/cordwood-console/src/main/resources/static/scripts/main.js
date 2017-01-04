/**
 * 应用初始化主模块。
 */
require.config({
  paths: {
    'jquery': 'libs/jquery-2.0.3.min',
    'jquery-cookie': 'libs/jquery.cookie.min',
    'jquery-slimscroll': 'libs/jquery.slimscroll.min',
    'jquery-uniform': 'libs/jquery.uniform.min',
    'jquery-chosen': 'libs/chosen.jquery',
    'jquery-easypiechart': 'libs/jquery.easypiechart',
    'jquery-flot': 'libs/jquery.flot.min',
    'jquery-flot-time': 'libs/jquery.flot.time.min',
    'jquery-blockui': 'libs/jquery.blockUI.min',
    'justgage': 'libs/justgage',
    'raphael': 'libs/raphael.min',
    'bootstrap': 'libs/bootstrap.min',
    'bootstrap-table': 'libs/bootstrap-table',
    'bootstrap-table-i18n': 'libs/bootstrap-table-zh-CN',
    'bootstrap-switch': 'libs/bootstrap-switch.min',
    'crypto-js': 'libs/crypto-js',
    'dropzone': 'libs/dropzone.min',
    'cropper': 'libs/cropper.min',
    'datejs': 'libs/date',
    'controls': 'helper/controls',
    'main': 'main'
  },
  // 兼容非 AMD 规范的第三方 JS 框架。
  shim: {
    'jquery-cookie': {
      deps: ['jquery']
    },
    'jquery-slimscroll': {
      deps: ['jquery']
    },
    'jquery-uniform': {
      deps: ['jquery']
    },
    'jquery-chosen': {
      deps: ['jquery']
    },
    'jquery-flot': {
      deps: ['jquery']
    },
    'jquery-flot-time': {
      deps: ['jquery-flot']
    },
    'jquery-blockui': {
      deps: ['jquery']
    },
    'justgage': {
      deps: ['raphael']
    },
    'bootstrap': {
      deps: ['jquery']
    },
    'bootstrap-table': {
      deps: ['jquery']
    },
    'bootstrap-table-i18n': {
      deps: ['jquery', 'bootstrap-table']
    },
    'bootstrap-switch': {
      deps: ['bootstrap']
    },
    'dropzone': {
      deps: ['jquery']
    },
    'datejs':{}
  }
});
define(['controls','jquery-blockui'], function(controls) {
  
  return {
    blockUI: function(el, loaderOnTop) {
      lastBlockedUI = el;
      $(el).block({
        message: '<img src="../../images/loaders/12.gif" align="absmiddle" />',
        css: {
          border: 'none',
          padding: '2px',
          backgroundColor: 'none'
        },
        overlayCSS: {
          backgroundColor: '#000',
          opacity: 0.05,
          cursor: 'wait'
        }
      });
    },

    unblockUI: function(el) {
      $(el).unblock({
        onUnblock: function() {
          $(el).removeAttr("style");
        }
      });
    },

    /**
     * 加载边栏菜单。
     */
    loadSidebar: function(selectedItemName) {
      $('#sidebar-items').empty();
      $.get('/admin/sidebar.json',
        function(data, status, xhr) {
          if (xhr.status == 200 && data) {
            var itemTemplate = '<li id="##item_name##-item">'
              + '<a href="##item_href##">' + '<i class="##item_icon##"></i> '
              + '<span class="menu-text">##item_text##</span>' + '</a>'
              + '</li>';
            for (var i = 0; i < data.length; i++) {
              var item = data[i];
              var html = itemTemplate.replace('##item_name##', item.name)
                .replace('##item_href##', item.url).replace('##item_icon##',
                  item.iconClasses).replace('##item_text##', item.displayText);
              $('#sidebar-items').append(html);
              if (selectedItemName && item.name == selectedItemName) {
                $('#' + item.name + '-item').addClass('active');
              }
            }
          }
        });
    }
  };
});