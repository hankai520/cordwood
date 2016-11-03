function loadSidebarItems() {
  $('#sidebar-items').empty();
  var allItems = {
    'dashboard': '<li id="dashboard-item"><a href="/admin/dashboard"><i class="fa fa-tachometer fa-fw"></i> <span class="menu-text">仪表盘</span></a></li>',
    'plugins': '<li id="plugins-item"><a href="/admin/plugins"><i class="fa fa-tachometer fa-fw"></i> <span class="menu-text">插件</span></a></li>',
    'users': '<li id="users-item"><a href="/admin/users"><i class="fa fa-tachometer fa-fw"></i> <span class="menu-text">用户</span></a></li>'
  };
  var visibleItems = JSON.parse('[# th:utext="${visibleItems}" /]');
  var selectedItem = '[# th:utext="${selectedItem}" /]';
  var current;
  for (var i = 0; i < visibleItems.length; i++) {
    var item = visibleItems[i];
    current = allItems[item];
    if (current) {
      $('#sidebar-items').append(current);
      if (selectedItem == item) {
        $('#' + item + '-item').addClass('active');
      }
    }
  }
}

loadSidebarItems();