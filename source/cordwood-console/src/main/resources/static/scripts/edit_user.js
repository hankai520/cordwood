/**
 * 应用初始化。
 * */
requirejs(['main'], function(app) {
  app.loadSidebar('users');
  
  /**
   * 初始化下拉搜索框。
   * */
  requirejs(['jquery-chosen'], function() {
    $('.chosen-select').chosen({
      no_results_text: '没有搜索到',
      disable_search_threshold: 5
    });
  });
});