function loadSidebarItems(selectedItemName, sidebarId='#sidebar-items') {
  $(sidebarId).empty();
  var itemTemplate = '<li id="##item_name##-item"><a href="##item_href##"><i class="##item_icon##"></i> <span class="menu-text">##item_text##</span></a></li>';
  var visibleItems = JSON.parse('[# th:utext="${visibleItems}" /]');
  for (var i = 0; i < visibleItems.length; i++) {
    var item = visibleItems[i];
    var html = itemTemplate.replace('##item_name##', item.name)
                           .replace('##item_href##', item.url)
                           .replace('##item_icon##', item.iconClasses)
                           .replace('##item_text##', item.displayText);
    $(sidebarId).append(html);
    if(item.name == selectedItemName) {
      $(item.name).addClass('active');
    }
  }
}