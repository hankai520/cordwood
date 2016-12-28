define(['jquery'],function () {
  return {
    loadSidebar: function(selectedItemName) {
      $('#sidebar-items').empty();
      $.get('/admin/sidebar.json', function(data, status, xhr) {
        if (xhr.status == 200 && data) {
          var itemTemplate = '<li id="##item_name##-item"><a href="##item_href##"><i class="##item_icon##"></i> <span class="menu-text">##item_text##</span></a></li>';
          for (var i = 0; i < data.length; i++) {
            var item = data[i];
            var html = itemTemplate.replace('##item_name##', item.name)
                                   .replace('##item_href##', item.url)
                                   .replace('##item_icon##', item.iconClasses)
                                   .replace('##item_text##', item.displayText);
            $('#sidebar-items').append(html);
            if(selectedItemName && item.name == selectedItemName) {
              $('#'+item.name+'-item').addClass('active');
            }
          }
        }
      });
    }
  };
});